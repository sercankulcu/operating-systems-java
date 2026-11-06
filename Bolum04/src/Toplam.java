import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

// Java ForkJoinPool ve is calma: dizi ikiye bolunur, sol yari fork ile
// kuyruga konur, sag yari ayni is parcaciginda hesaplanir, sonra join edilir.
public class Toplam extends RecursiveTask<Long> {
	private static final long serialVersionUID = 1L;
	static final int ESIK = 2;
	final int[] a;
	final int bas, son;

	Toplam(int[] a, int bas, int son) {
		this.a = a;
		this.bas = bas;
		this.son = son;
	}

	@Override
	protected Long compute() {
		if (son - bas <= ESIK) {      // kucukse dogrudan
			long s = 0;
			for (int i = bas; i < son; i++) s += a[i];
			return s;
		}
		int orta = (bas + son) / 2;
		Toplam sol = new Toplam(a, bas, orta);
		Toplam sag = new Toplam(a, orta, son);
		sol.fork();                   // es zamansiz baslat
		long sagSonuc = sag.compute();
		return sol.join() + sagSonuc;
	}

	public static void main(String[] args) {
		// Kod izleme ornegi: tek iscili havuz, ESIK = 2
		int[] a = {2, 7, 1, 8, 2, 8, 1, 8};
		try (ForkJoinPool havuz = new ForkJoinPool(1)) {
			System.out.println("toplam = " + havuz.invoke(new Toplam(a, 0, 8)));
		}

		// 1..100 000 dizisi, ortak havuz
		int[] b = new int[100_000];
		for (int i = 0; i < b.length; i++) b[i] = i + 1;
		long s = ForkJoinPool.commonPool()
				.invoke(new Toplam(b, 0, b.length));
		System.out.println("toplam = " + s);
	}
}

/* Beklenen cikti:
toplam = 37
toplam = 5000050000
*/
