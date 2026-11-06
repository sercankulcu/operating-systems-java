// Java: Thread, start ve join. Yeni is parcacigi 1..5 toplamini hesaplar;
// main join ile bekler. join, t'nin yazdiklarinin main'ce gorulmesini de garanti eder.
public class Topla {
	static int toplam = 0;

	public static void main(String[] a)
			throws InterruptedException {
		Thread t = new Thread(() -> {
			for (int i = 1; i <= 5; i++) toplam += i;
		});
		System.out.println("durum: " + t.getState());   // NEW
		t.start();
		System.out.println("main: bekliyor");
		t.join();
		System.out.println("durum: " + t.getState());   // TERMINATED
		System.out.println("toplam = " + toplam);
	}
}

/* Beklenen cikti:
durum: NEW
main: bekliyor
durum: TERMINATED
toplam = 15
*/
