import java.util.concurrent.atomic.AtomicInteger;

/*
 * Atomik degiskenler: iki is parcacigi 100_000'er kez AtomicInteger'i ve
 * synchronized ile korunan sayaci artirir; ikisi de 200000 olur.
 * compareAndSet yalniz beklenen deger tutarsa yazar.
 */
public class Atomik {
	static AtomicInteger atom = new AtomicInteger();
	static int sync = 0;
	static final Object kilit = new Object();

	static void ikiIsParcacigi(Runnable r) throws InterruptedException {
		Thread a = new Thread(r), b = new Thread(r);
		a.start(); b.start();
		a.join();  b.join();
	}

	public static void main(String[] args) throws InterruptedException {
		/* iki is parcacigi, her biri 100_000 kez: */
		ikiIsParcacigi(() -> {
			for (int k = 0; k < 100_000; k++)
				atom.incrementAndGet();
		});
		ikiIsParcacigi(() -> {
			for (int k = 0; k < 100_000; k++)
				synchronized (kilit) { sync++; }
		});
		System.out.println("AtomicInteger = " + atom.get());
		System.out.println("synchronized  = " + sync);

		var v = new AtomicInteger(5);
		boolean r1 = v.compareAndSet(5, 6);   /* true,  v = 6 */
		System.out.println(r1 + " " + v.get());
		boolean r2 = v.compareAndSet(5, 7);   /* false, v = 6 */
		System.out.println(r2 + " " + v.get());
	}
}

/* Beklenen cikti:
AtomicInteger = 200000
synchronized  = 200000
true 6
false 6
*/
