// Bilet kilidi (ticket lock): next bilet dagitir, serving sirasi geleni
// gosterir. Kilit adildir: is parcaciklari bilet sirasiyla girer.
// Beklenen cikti: sayac = 200000
import java.util.concurrent.atomic.*;

public class Bilet {
	static AtomicInteger next = new AtomicInteger();
	static volatile int serving = 0;
	static int sayac = 0;

	static void lock() {
		int my = next.getAndIncrement();
		while (serving != my) Thread.onSpinWait();
	}

	static void unlock() { serving++; }   // yalniz kilit sahibi yazar

	public static void main(String[] a)
			throws Exception {
		Runnable is = () -> {
			for (int i = 0; i < 100_000; i++) {
				lock(); sayac++; unlock(); } };
		Thread t0 = new Thread(is);
		Thread t1 = new Thread(is);
		t0.start(); t1.start(); t0.join(); t1.join();
		System.out.println("sayac = " + sayac);
	}
}
