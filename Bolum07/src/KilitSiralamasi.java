import java.util.concurrent.locks.ReentrantLock;

/*
 * Dongusel beklemeye saldiri: kilit siralamasi. Iki kilit her zaman kucuk id'den
 * buyuge dogru alinir; T1 x->y, T2 y->x aktarsa da dongu olusamaz.
 * Her biri 100 000 ters yonlu aktarim.
 */
public class KilitSiralamasi {

	static class Hesap {
		final int id;
		int bakiye;
		final ReentrantLock kilit = new ReentrantLock();

		Hesap(int id, int bakiye) {
			this.id = id;
			this.bakiye = bakiye;
		}
	}

	static final int TUR = 100_000;

	static void transfer(Hesap k, Hesap h, int m) {
		Hesap ilk = k.id < h.id ? k : h;
		Hesap ikinci = k.id < h.id ? h : k;
		ilk.kilit.lock();
		try {
			ikinci.kilit.lock();
			try { k.bakiye -= m; h.bakiye += m; }
			finally { ikinci.kilit.unlock(); }
		} finally { ilk.kilit.unlock(); }
	}

	public static void main(String[] args) throws InterruptedException {
		Hesap x = new Hesap(1, 1000), y = new Hesap(2, 1000);
		/* T1: transfer(x, y, 1)   x.id = 1
		   T2: transfer(y, x, 1)   y.id = 2 */
		Thread t1 = new Thread(() -> { for (int i = 0; i < TUR; i++) transfer(x, y, 1); }, "T1");
		Thread t2 = new Thread(() -> { for (int i = 0; i < TUR; i++) transfer(y, x, 1); }, "T2");
		t1.start(); t2.start();
		t1.join(); t2.join();
		System.out.println("x=" + x.bakiye + " y=" + y.bakiye + ", kilitlenme yok");
	}
}

/* Beklenen cikti:
x=1000 y=1000, kilitlenme yok
*/
