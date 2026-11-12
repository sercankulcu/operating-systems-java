import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/*
 * Senkronizasyon bariyeri: 3 is parcacigi iki faz hesaplar; her fazin sonunda
 * b.await() ile bulusur. Ucuncu gelen bariyeri acar ve bariyer eylemi
 * "faz N tamam" yazar. Bariyer yeniden kullanilabilir (cyclic).
 */
public class Bariyer {
	static final int[] faz = { 0 };
	static final int[] hesaplanan = new int[3];

	static void hesapla(int id, int f) {
		hesaplanan[id] += f + 1;          /* fazin isi */
	}

	public static void main(String[] args) throws InterruptedException {
		var b = new CyclicBarrier(3,
			() -> System.out.println(
					"faz " + faz[0]++ + " tamam"));
		Thread[] t = new Thread[3];
		for (int i = 0; i < 3; i++) {
			final int id = i;
			t[i] = new Thread(() -> {
				try {
					for (int f = 0; f < 2; f++) {
						hesapla(id, f); b.await();
					}
				} catch (InterruptedException | BrokenBarrierException e) {
					Thread.currentThread().interrupt();
				}
			});
			t[i].start();
		}
		for (Thread th : t)
			th.join();
	}
}

/* Beklenen cikti:
faz 0 tamam
faz 1 tamam
*/
