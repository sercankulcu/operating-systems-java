import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*
 * Kapsulleme kilit sirasini gizler: a.addAll(b) iceride once a'nin kilidini alir,
 * sonra b.toArray() ile b'nin kilidini ister. b.addAll(a) ayni anda ters sirada
 * ister -> kilitlenme. Duzeltme: kopyayi kilit disinda almak.
 * Is parcaciklari daemon; kilitlenme ThreadMXBean ile bulunup rapor edilir.
 */
public class KapsullemeKilitSirasi {

	static final int TUR = 200_000;

	/* Iki is parcacigini calistirir; kilitlenirse kimin kimi bekledigini dondurur */
	static String yaris(Runnable r1, Runnable r2) throws InterruptedException {
		Thread t1 = new Thread(r1, "T1");
		Thread t2 = new Thread(r2, "T2");
		t1.setDaemon(true);
		t2.setDaemon(true);
		t1.start();
		t2.start();
		ThreadMXBean mx = ManagementFactory.getThreadMXBean();
		long son = System.currentTimeMillis() + 4000;
		while (System.currentTimeMillis() < son) {
			long[] ids = mx.findDeadlockedThreads();
			if (ids != null)    /* onceki denemenin kilitli kalan is parcaciklarini ele */
				ids = Arrays.stream(ids)
					.filter(id -> id == t1.threadId() || id == t2.threadId())
					.toArray();
			if (ids != null && ids.length > 0) {
				Arrays.sort(ids);
				StringBuilder sb = new StringBuilder("KILITLENDI:");
				for (ThreadInfo ti : mx.getThreadInfo(ids))
					sb.append(' ').append(ti.getThreadName())
						.append(" -> ").append(ti.getLockOwnerName()).append(';');
				return sb.toString();
			}
			if (!t1.isAlive() && !t2.isAlive())
				return "kilitlenme yok, iki is parcacigi da bitti";
			Thread.sleep(10);
		}
		return "4 sn icinde bitmedi";
	}

	public static void main(String[] args) throws InterruptedException {
		var a = Collections.synchronizedList(
				new ArrayList<Integer>());
		var b = Collections.synchronizedList(
				new ArrayList<Integer>());
		/* a.addAll(b) iceride:             */
		/* synchronized (a) { b.toArray() }  */
		/*   -> synchronized (b) { ... }      */
		/* Listeler bos kalir: addAll yine de iki kilidi de alir. */
		System.out.println("Capraz addAll: " + yaris(
			() -> { for (int i = 0; i < TUR; i++) a.addAll(b); },
			() -> { for (int i = 0; i < TUR; i++) b.addAll(a); }));

		/* Duzeltme: kopyayi kilit disinda al (yeni listelerle) */
		List<Integer> c = Collections.synchronizedList(new ArrayList<Integer>());
		List<Integer> d = Collections.synchronizedList(new ArrayList<Integer>());
		System.out.println("Duzeltilmis surum: " + yaris(
			() -> {
				for (int i = 0; i < TUR; i++) {
					Integer[] k = d.toArray(new Integer[0]);
					c.addAll(Arrays.asList(k));
				}
			},
			() -> {
				for (int i = 0; i < TUR; i++) {
					Integer[] k = c.toArray(new Integer[0]);
					d.addAll(Arrays.asList(k));
				}
			}));
	}
}

/*
 * Beklenen cikti (ilk satir calistirmaya gore degisebilir; JDK 21'de neredeyse
 * her zaman kilitlenir):
 * Capraz addAll: KILITLENDI: T1 -> T2; T2 -> T1;
 * Duzeltilmis surum: kilitlenme yok, iki is parcacigi da bitti
 */
