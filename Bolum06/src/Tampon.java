import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/*
 * Java: ReentrantLock ve Condition. Iki yuvali dairesel tampon; bir kilide iki
 * kosul bagli (bosYer, dolu), signal dogru grubu uyandirir. unlock() finally
 * icinde. Uretici 1..5 koyar; main 5 kez al() yapar.
 */
class Tampon {
	private final int[] buf = new int[2];
	private int bas = 0, son = 0, adet = 0;
	private final ReentrantLock kilit = new ReentrantLock();
	private final Condition bosYer = kilit.newCondition();
	private final Condition dolu   = kilit.newCondition();

	void koy(int x) throws InterruptedException {
		kilit.lock();
		try {
			while (adet == buf.length) bosYer.await();
			buf[son] = x; son = (son + 1) % buf.length; adet++;
			dolu.signal();
		} finally { kilit.unlock(); }
	}

	int al() throws InterruptedException {
		kilit.lock();
		try {
			while (adet == 0) dolu.await();
			int x = buf[bas]; bas = (bas + 1) % buf.length; adet--;
			bosYer.signal();
			return x;
		} finally { kilit.unlock(); }
	}

	public static void main(String[] args) throws InterruptedException {
		Tampon t = new Tampon();
		Thread uretici = new Thread(() -> {
			try {
				for (int x = 1; x <= 5; x++)
					t.koy(x);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		});
		uretici.start();
		StringBuilder sb = new StringBuilder();
		int toplam = 0;
		for (int k = 0; k < 5; k++) {
			int x = t.al();
			sb.append(x).append(' ');
			toplam += x;
		}
		uretici.join();
		System.out.println(sb + "| toplam = " + toplam);
	}
}

/* Beklenen cikti:
1 2 3 4 5 | toplam = 15
*/
