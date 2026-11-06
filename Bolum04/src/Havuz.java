import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// Java ExecutorService: dort gorev (1^2, 2^2, 3^2, 4^2) iki is parcacikli
// havuza verilir; Future.get() her sonucu bekler.
public class Havuz {
	public static void main(String[] args)
			throws InterruptedException, ExecutionException {
		ExecutorService havuz = Executors.newFixedThreadPool(2);
		List<Future<Integer>> sonuclar = new ArrayList<>();
		for (int g = 1; g <= 4; g++) {
			final int n = g;
			sonuclar.add(havuz.submit(() -> n * n));
		}
		int toplam = 0;
		for (Future<Integer> f : sonuclar)
			toplam += f.get();      // sonucu bekler
		System.out.println("toplam = " + toplam);
		havuz.shutdown();
	}
}

/* Beklenen cikti:
toplam = 30
*/
