# Operating Systems in Java

Java code examples for the undergraduate **Operating Systems (İşletim Sistemleri)** course at Giresun University. Most of the code on the course slides is C and lives in [operating-systems-labs](https://github.com/sercankulcu/operating-systems-labs); the slides that use Java are collected here. Each `BolumNN/src` folder holds the Java code of chapter NN as small runnable programs, with names that follow the slides.

Course page and slides: <https://sercankulcu.github.io/teaching/operating-systems>

## Chapters

| Folder | Chapter | Programs |
|---|---|---|
| `Bolum04` | Threads | `Thread`, `start` and `join`; virtual threads (Java 21); `ExecutorService`; fork/join sum; thread-local storage; observing a race condition |
| `Bolum06` | Synchronization | Peterson's algorithm with `volatile`, `CyclicBarrier`, `AtomicInteger` versus `synchronized`, `synchronized`/`wait`/`notifyAll`, a bounded buffer |
| `Bolum07` | Deadlocks | A deadlock and its detection with `ThreadMXBean`, lock order hidden by encapsulation, `tryLock` with back-off, lock ordering |
| `Bolum12` | Multiprocessor Systems | Ticket lock |

The other chapters have no Java code on the slides.

## Running the examples

Requires **JDK 21**. Every file is a standalone program with a `main` method in the default package, and no program waits for keyboard input. Programs that demonstrate a deadlock detect it and finish. To compile and run one chapter:

```bash
cd Bolum06/src
javac -d ../bin *.java
java -cp ../bin Peterson
```

Each folder is also an Eclipse project (`File > Import > Existing Projects into Workspace`).

Comments and identifiers are in Turkish, written without Turkish characters so the files compile with any platform encoding.

## Automatic check

`ci/run_examples.py` compiles every `BolumNN` folder with JDK 21, runs every program and compares its output with the expected output at the end of the file. GitHub Actions runs it on every push. A program whose output changes from run to run (timings, random numbers, thread order, the network) says so above its expected output and only has to run; the window programs of a Swing chapter are skipped, since the check runs without a screen. To run the check locally:

```bash
python ci/run_examples.py            # every chapter
python ci/run_examples.py Bolum03    # one chapter
```

## Contact

For questions or suggestions, contact [Sercan Külcü](https://github.com/sercankulcu) or open an issue on GitHub.
