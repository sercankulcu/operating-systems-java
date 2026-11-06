import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

/*
 * Java'da kilitlenme: T1 once a sonra b, T2 once b sonra a monitorunu alir.
 * Kilitlenme ThreadMXBean.findDeadlockedThreads ile bulunur (jstack'in yaptigi
 * gibi); T1 ve T2 daemon oldugu icin program yine de biter.
 * Ayni raporu dis aractan almak icin: jstack <pid>  ya da  jcmd <pid> Thread.print
 */
public class Asili {

	/* uyu(10) zamanlamayi cok olasi kilar ama garanti etmez; bu yuzden uyu()
	 * ayrica iki ilk kilit alinana dek bekler (sunumdaki CountDownLatch'li surum). */
	static final CountDownLatch ikiIlkKilit = new CountDownLatch(2);

	static void uyu(int ms) {
		ikiIlkKilit.countDown();
		try {
			ikiIlkKilit.await();
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	static void isle() {
		System.out.println(Thread.currentThread().getName() + " isledi");
	}

	static void calis() throws InterruptedException {
		Object a = new Object(), b = new Object();

		Thread t1 = new Thread(() -> {
			synchronized (a) {
				uyu(10);   /* pencereyi buyut */
				synchronized (b) { isle(); }
			}
		}, "T1");
		Thread t2 = new Thread(() -> {
			synchronized (b) {
				uyu(10);
				synchronized (a) { isle(); }
			}
		}, "T2");
		t1.setDaemon(true);
		t2.setDaemon(true);
		t1.start(); t2.start();

		/* Kilitlenmeyi bulmak: JVM bekleme cizgesinde dongu arar */
		ThreadMXBean mx =
			ManagementFactory.getThreadMXBean();
		long[] ids = null;
		for (int i = 0; i < 100 && ids == null; i++) {
			Thread.sleep(20);
			ids = mx.findDeadlockedThreads();
		}
		if (ids != null) {
			Arrays.sort(ids);   /* T1 once yazilsin */
			for (ThreadInfo ti : mx.getThreadInfo(ids))
				System.out.println(ti.getThreadName()
					+ " bekliyor, kilit sahibi "
					+ ti.getLockOwnerName());
		} else {
			System.out.println("kilitlenme olusmadi");
		}
		System.out.println("T1: " + t1.getState() + ", T2: " + t2.getState());
	}

	public static void main(String[] args) throws InterruptedException {
		calis();
		System.out.println("Tespit eder ama kurtarmaz; daemon is parcaciklari JVM ile biter.");
	}
}

/*
 * Beklenen cikti:
 * T1 bekliyor, kilit sahibi T2
 * T2 bekliyor, kilit sahibi T1
 * T1: BLOCKED, T2: BLOCKED
 * Tespit eder ama kurtarmaz; daemon is parcaciklari JVM ile biter.
 */
