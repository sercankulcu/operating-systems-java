import java.time.Duration;
import java.util.concurrent.*;
import java.util.stream.IntStream;

// Java 21 sanal is parcaciklari: 10 000 gorevin her biri kendi sanal is
// parcaciginda 1 sn uyur; birkac tasiyici is parcacigi yeter, toplam ~2 sn.
public class Sanal {
	public static void main(String[] args) {
		long bas = System.nanoTime();
		try (var ex = Executors.newVirtualThreadPerTaskExecutor()) {
			IntStream.range(0, 10_000).forEach(i ->
				ex.submit(() -> {
					Thread.sleep(Duration.ofSeconds(1));
					return i;
				}));
		}  // close(): tum gorevleri bekler
		long ms = (System.nanoTime() - bas) / 1_000_000;
		System.out.println("bitti");
		System.out.println("sure: " + ms + " ms");
	}
}

/* Beklenen cikti (sure makineye gore degisir):
bitti
sure: 2197 ms
*/
