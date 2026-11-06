// Yarisi gozlemlemek: iki is parcacigi ayni sayaci kilitsiz 1 000 000'ar kez
// artirir. sayac++ okuma-artirma-yazma uclusudur; guncellemeler kaybolur.
public class Yaris {
	static int sayac = 0;

	public static void main(String[] a) throws Exception {
		Runnable is = () -> {
			for (int i = 0; i < 1_000_000; i++) sayac++;
		};
		Thread t1 = new Thread(is), t2 = new Thread(is);
		t1.start(); t2.start();
		t1.join();  t2.join();
		System.out.println("sayac = " + sayac);
	}
}

/* Beklenen cikti: 2 000 000'dan kucuk ve her calistirmada farkli, ornegin
sayac = 1456465
(JIT dongu govdesini tek toplamaya cevirirse bazen 2000000 de cikabilir.)
*/
