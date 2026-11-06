// Is parcacigina yerel depolama (TLS): ThreadLocal her is parcacigina ayri
// bir kopya verir; main baslangic degerini (0) gorur.
public class YerelDepolama {
	static ThreadLocal<Integer> no =
		ThreadLocal.withInitial(() -> 0);

	public static void main(String[] args) throws InterruptedException {
		String[] satir = new String[2];
		Thread a = new Thread(() -> { no.set(1); satir[0] = "A: " + no.get(); });
		Thread b = new Thread(() -> { no.set(2); satir[1] = "B: " + no.get(); });
		a.start(); b.start();
		a.join();  b.join();
		System.out.println(satir[0]);
		System.out.println(satir[1]);
		System.out.println("main: " + no.get());
	}
}

/* Beklenen cikti:
A: 1
B: 2
main: 0
*/
