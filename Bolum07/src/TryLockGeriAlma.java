import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/*
 * "Kesme yok" kosuluna saldiri: tryLock zaman asimi ve geri alma.
 * Ikinci kilidi 50 ms icinde alamayan, elindekini kendisi birakir ve rastgele
 * bir sure sonra yeniden dener. 2 is parcacigi x 1000 ters yonlu transfer.
 */
public class TryLockGeriAlma {

	static class Hesap {
		final int id;
		int bakiye;
		final ReentrantLock kilit = new ReentrantLock();

		Hesap(int id, int bakiye) {
			this.id = id;
			this.bakiye = bakiye;
		}
	}

	static final int TUR = 1000;

	static boolean dene(Hesap k, Hesap h, int m)
			throws InterruptedException {
		if (!k.kilit.tryLock(50, MILLISECONDS))
			return false;
		try {
			if (!h.kilit.tryLock(50, MILLISECONDS))
				return false;  /* k'yi de birak */
			try { k.bakiye -= m; h.bakiye += m;
				return true; }
			finally { h.kilit.unlock(); }
		} finally { k.kilit.unlock(); }
	}

	/* cagiran: while (!dene(x, y, 1)) Thread.sleep(rastgele); */
	static Thread isci(String ad, Hesap k, Hesap h, long tohum, int[] geriAlinan) {
		return new Thread(() -> {
			Random rastgele = new Random(tohum);
			try {
				for (int i = 0; i < TUR; i++)
					while (!dene(k, h, 1)) {
						geriAlinan[0]++;
						Thread.sleep(rastgele.nextInt(5));
					}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}, ad);
	}

	public static void main(String[] args) throws InterruptedException {
		Hesap x = new Hesap(1, 1000), y = new Hesap(2, 1000);
		int[] g1 = new int[1], g2 = new int[1];
		Thread t1 = isci("T1", x, y, 42, g1);   /* x -> y */
		Thread t2 = isci("T2", y, x, 43, g2);   /* y -> x */
		long bas = System.nanoTime();
		t1.start(); t2.start();
		t1.join(); t2.join();
		long ms = (System.nanoTime() - bas) / 1_000_000;
		System.out.println("x=" + x.bakiye + " y=" + y.bakiye
			+ " toplam=" + (x.bakiye + y.bakiye));
		System.out.println("kilitlenme yok; geri alinan deneme: " + (g1[0] + g2[0])
			+ ", sure: " + ms + " ms");
	}
}

/* Not: bu programin ciktisi her calistirmada degisir (geri alinan deneme sayisi ve sure);
   asagidaki bir ornek calistirmadir. */
/* Beklenen cikti:
x=1000 y=1000 toplam=2000
kilitlenme yok; geri alinan deneme: 4, sure: 117 ms
*/
