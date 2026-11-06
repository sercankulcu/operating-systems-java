/*
 * Java: synchronized, wait ve notify. Her nesne bir monitordur: tek yuvali
 * tampon koy/al ile korunur, Mesa anlami yuzunden kosul while ile sinanir.
 * Uretici 10, 20, 30, 40 koyar; main dort kez al() ile okur.
 */
public class TekYuva {
	private int yuva = 0;            /* 0 = bos */

	synchronized void koy(int x) throws InterruptedException {
		while (yuva != 0) wait();      /* kilidi birakir */
		yuva = x;
		notifyAll();
	}

	synchronized int al() throws InterruptedException {
		while (yuva == 0) wait();
		int x = yuva;
		yuva = 0;
		notifyAll();
		return x;
	}

	public static void main(String[] args) throws InterruptedException {
		TekYuva t = new TekYuva();
		Thread uretici = new Thread(() -> {
			try {
				for (int x = 10; x <= 40; x += 10)
					t.koy(x);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		});
		uretici.start();
		for (int k = 0; k < 4; k++)
			System.out.println("alindi " + t.al());
		uretici.join();
	}
}

/*
 * Beklenen cikti (her calistirmada ayni; slayttaki "alindi" Turkce karakterlidir):
 * alindi 10
 * alindi 20
 * alindi 30
 * alindi 40
 */
