# -*- coding: utf-8 -*-
"""Compiles and runs every example program and compares its output with the
expected output written at the end of the file.

    python ci/run_examples.py              # every BolumNN folder (or src/)
    python ci/run_examples.py Bolum03      # only the given folders

Java: each BolumNN folder is compiled on its own (with BolumNN/lib/*.jar on the
classpath); every class with a main method is run. Python: every BolumNN/*.py is run.
Programs read nothing from the keyboard (stdin is empty; a program that does read
it gets <Name>.girdi next to the source as input) and must finish within 30 seconds
without leaving files behind.

The expected output is the block at the end of a file:
    Java:   /* Beklenen cikti:  ...  */
    Python: # Beklenen çıktı:  followed by '# ' lines
A file whose output changes on every run or with the operating system says so in a
note above the block ("her calistirmada degisir", "isletim sistemine gore degisir");
it only has to run. Identity hash codes in Object.toString output (Kopek@1b6d3586)
and OS-specific error texts are treated as equal. Swing programs that need a
screen are reported as skipped. Output is produced with the Turkish locale
(decimal comma), as in the course.
"""
import glob, os, re, shutil, subprocess, sys, tempfile
from concurrent.futures import ThreadPoolExecutor

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
TIMEOUT = 30
JAVA_FLAGS = ['-Djava.awt.headless=true', '-Dfile.encoding=UTF-8', '-Dstdout.encoding=UTF-8',
              '-Dstderr.encoding=UTF-8', '-Duser.language=tr', '-Duser.country=TR']
VARIES = re.compile(r'(her [cç]al[ıi][sş]t[ıi]rmada|i[sş]letim sistemine g[oö]re) de[gğ]i[sş]ir', re.I)


def tool(name):
    home = os.environ.get('JAVA_HOME')
    if home:
        for ext in ('', '.exe'):
            p = os.path.join(home, 'bin', name + ext)
            if os.path.exists(p):
                return p
    return shutil.which(name) or name


def lines(text):
    out = [l.rstrip() for l in text.replace('\r\n', '\n').replace('\r', '\n').split('\n')]
    while out and not out[-1]:
        out.pop()
    while out and not out[0]:
        out.pop(0)
    return out


def expected_java(src):
    m = re.search(r'/\*\s*Beklenen [cç][ıi]kt[ıi]\s*:?[ \t]*\r?\n(.*?)\*/', src, re.S | re.I)
    return lines(m.group(1)) if m else None


def expected_python(src):
    m = re.search(r'^# Beklenen [cç][ıi]kt[ıi]\s*:?\s*\n((?:#.*(?:\n|$))*)', src, re.M)
    if not m:
        return None
    return lines('\n'.join(re.sub(r'^# ?', '', l) for l in m.group(1).split('\n')))


# Messages that differ only by operating system; the Windows wording is mapped to Linux.
OS_TEXT = [('(The system cannot find the file specified)', '(No such file or directory)'),
           ('(The system cannot find the path specified)', '(No such file or directory)')]


def neutral(ls):
    out = []
    for l in ls:
        for a, b in OS_TEXT:
            l = l.replace(a, b)
        l = re.sub(r'@[0-9a-f]{5,8}\b', '@<hash>', l)   # Object.toString: identity hash differs per JVM
        out.append(l.replace('*/', '* /'))   # the block cannot contain the comment end
    return out


def run(cmd, cwd, stdin_file=None, merge=True):
    """merge=True: stdout and stderr in one stream, in the order the program wrote them."""
    try:
        stdin = open(stdin_file, 'rb') if stdin_file else subprocess.DEVNULL
        p = subprocess.run(cmd, cwd=cwd, stdin=stdin, stdout=subprocess.PIPE,
                           stderr=subprocess.STDOUT if merge else subprocess.PIPE, timeout=TIMEOUT,
                           env=dict(os.environ, PYTHONIOENCODING='utf-8'))
    except subprocess.TimeoutExpired:
        return None, '', ''
    return p.returncode, p.stdout.decode('utf-8', 'replace'), (p.stderr or b'').decode('utf-8', 'replace')


def judge(rel, src, exp, cmd):
    # A program that reads the keyboard gets its input from <Name>.girdi next to it.
    girdi = os.path.join(ROOT, os.path.splitext(rel)[0] + '.girdi')
    stdin_file = girdi if os.path.exists(girdi) else None
    work = tempfile.mkdtemp()
    try:
        rc, out, _ = run(cmd, work, stdin_file)
        left = os.listdir(work)
    finally:
        shutil.rmtree(work, ignore_errors=True)
    if rc is None:
        return rel, 'TIMEOUT', ['did not finish in %d s' % TIMEOUT]
    if 'HeadlessException' in out:          # needs a screen; may still exit with 0 (event thread)
        return rel, 'skipped (window)', []
    if rc != 0:
        return rel, 'FAILED', ['exit code %d' % rc] + lines(out)[-4:]
    if left:
        return rel, 'LEFT FILES', left[:5]
    if exp is None or VARIES.search(src):
        return rel, 'ok (ran)', []
    exp = neutral(exp)
    got = neutral(lines(out))
    if exp == got:
        return rel, 'ok', []
    # Some older blocks hold stdout alone, or stdout followed by stderr: try those too.
    work = tempfile.mkdtemp()
    try:
        _, o2, e2 = run(cmd, work, stdin_file, merge=False)
    finally:
        shutil.rmtree(work, ignore_errors=True)
    if exp in (neutral(lines(o2)), neutral(lines(o2 + e2))):
        return rel, 'ok', []
    diff = ['expected: %s' % e for e, g in zip(exp, got + [''] * len(exp)) if e != g][:2]
    diff += ['got:      %s' % g for e, g in zip(exp + [''] * len(got), got) if e != g][:2]
    return rel, 'OUTPUT DIFFERS', diff


def java_folder(folder):
    srcs = [p for p in glob.glob(os.path.join(folder, '**', '*.java'), recursive=True)
            if os.sep + 'bin' + os.sep not in p]
    if not srcs:
        return []
    classes = tempfile.mkdtemp()
    cp = os.pathsep.join([classes] + glob.glob(os.path.join(folder, 'lib', '*.jar')))
    listing = os.path.join(classes, 'sources.txt')
    with open(listing, 'w', encoding='utf-8') as f:
        f.write('\n'.join('"%s"' % p.replace('\\', '/') for p in srcs))
    c = subprocess.run([tool('javac'), '-encoding', 'UTF-8', '-nowarn', '-d', classes, '-cp', cp, '@' + listing],
                       capture_output=True, text=True, errors='replace')
    name = os.path.relpath(folder, ROOT).replace(os.sep, '/')
    if c.returncode:
        shutil.rmtree(classes, ignore_errors=True)
        return [(name, 'NOT COMPILED', lines(c.stdout + c.stderr)[:8])]
    jobs = []
    for p in sorted(srcs):
        src = open(p, encoding='utf-8', errors='replace').read()
        if not re.search(r'static\s+void\s+main\s*\(', src):
            continue
        pkg = re.search(r'^\s*package\s+([\w.]+)\s*;', src, re.M)
        fq = (pkg.group(1) + '.' if pkg else '') + os.path.splitext(os.path.basename(p))[0]
        rel = os.path.relpath(p, ROOT).replace(os.sep, '/')
        jobs.append((rel, src, expected_java(src), [tool('java')] + JAVA_FLAGS + ['-cp', cp, fq]))
    with ThreadPoolExecutor(2) as ex:
        res = list(ex.map(lambda j: judge(*j), jobs))
    shutil.rmtree(classes, ignore_errors=True)
    return res


def python_folder(folder):
    jobs = []
    for p in sorted(glob.glob(os.path.join(folder, '*.py'))):
        src = open(p, encoding='utf-8').read()
        rel = os.path.relpath(p, ROOT).replace(os.sep, '/')
        jobs.append((rel, src, expected_python(src), [sys.executable, os.path.abspath(p)]))
    with ThreadPoolExecutor(2) as ex:
        return list(ex.map(lambda j: judge(*j), jobs))


def main():
    args = sys.argv[1:]
    folders = [os.path.join(ROOT, a) for a in args] or sorted(glob.glob(os.path.join(ROOT, 'Bolum*'))) \
        or [os.path.join(ROOT, 'src')]
    results = []
    for f in folders:
        has_java = glob.glob(os.path.join(f, '**', '*.java'), recursive=True)
        results += java_folder(f) if has_java else python_folder(f)
    bad = [r for r in results if not r[1].startswith(('ok', 'skipped'))]
    for rel, status, detail in results:
        if not status.startswith('ok'):
            print('%-17s %s' % (status, rel))
            for d in detail:
                print('    ' + d)
    counts = {}
    for _, s, _ in results:
        counts[s] = counts.get(s, 0) + 1
    print('\n%d programs: %s' % (len(results), ', '.join('%s %d' % kv for kv in sorted(counts.items()))))
    summary = os.environ.get('GITHUB_STEP_SUMMARY')
    if summary:
        with open(summary, 'a', encoding='utf-8') as f:
            f.write('## Example programs\n\n%d programs: %s\n\n' % (len(results), ', '.join(
                '%s %d' % kv for kv in sorted(counts.items()))))
            if bad:
                f.write('| Program | Result |\n|---|---|\n' + ''.join('| `%s` | **%s** |\n' % (r, s) for r, s, _ in bad))
    if bad and os.environ.get('GITHUB_ACTIONS'):
        msg = '%0A'.join('%s: %s %s' % (r, s, ' / '.join(d[:2])) for r, s, d in bad[:30])
        print('::error title=Example programs failed::' + msg.replace('\n', ' '))
    sys.exit(1 if bad else 0)


if __name__ == '__main__':
    main()
