/*
 * Peterson algoritmasi Java'da: ayri volatile alanlar (flag0, flag1, turn)
 * sirali tutarli okunup yazildigi icin algoritma dogru calisir.
 * Iki is parcacigi 100_000'er kez kilitle(i); sayac++; birak(i) yapar.
 */
public class Peterson {
	static volatile boolean flag0, flag1;
	static volatile int turn;
	static int sayac = 0;

	static void kilitle(int i) {
		if (i == 0) { flag0 = true; turn = 1;
			while (flag1 && turn == 1) Thread.onSpinWait(); }
		else        { flag1 = true; turn = 0;
			while (flag0 && turn == 0) Thread.onSpinWait(); }
	}

	static void birak(int i) {
		if (i == 0) flag0 = false; else flag1 = false;
	}

	public static void main(String[] args) throws InterruptedException {
		Thread[] t = new Thread[2];
		for (int i = 0; i < 2; i++) {
			final int id = i;
			t[i] = new Thread(() -> {
				for (int k = 0; k < 100_000; k++) {
					kilitle(id);
					sayac++;
					birak(id);
				}
			});
			t[i].start();
		}
		for (Thread th : t)
			th.join();
		System.out.println("sayac = " + sayac);
	}
}

/* Beklenen cikti:
sayac = 200000
*/
