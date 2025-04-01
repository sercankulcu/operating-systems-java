import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/*
 * 
 * Here is an example of how to use the fork method in Java:
 * 
 * In this example, the ForkExample class extends the RecursiveAction class, which is part of the 
 * Java ForkJoin framework. The compute method is overridden to define the work that should be 
 * performed by the task. If the size of the task is below a certain threshold, the work is 
 * performed directly. If the size of the task is above the threshold, the task is split into 
 * two smaller tasks using the fork method. The left task is then forked and the right task is 
 * computed directly. The left task is then joined to the main task once it is finished.
 * 
 * */

public class ForkExample extends RecursiveAction {

	private static final long serialVersionUID = 1L;
	private static final int THRESHOLD = 1024;
	private final int[] array;
	private final int start;
	private final int end;

	public ForkExample(int[] array, int start, int end) {
		this.array = array;
		this.start = start;
		this.end = end;
	}

	protected void compute() {
		if (end - start <= THRESHOLD) {
			// perform some work here
			System.out.println("Thread: " + Thread.currentThread().getId() + ", computing start: " + start + ", end: " + end);
		} else {
			int middle = (start + end) / 2;
			ForkExample leftTask = new ForkExample(array, start, middle);
			ForkExample rightTask = new ForkExample(array, middle, end);
			leftTask.fork();
			rightTask.compute();
			leftTask.join();
		}
	}

	public static void main(String[] args) {
		int[] array = new int[16*THRESHOLD];
		try (ForkJoinPool pool = new ForkJoinPool()) {
			ForkExample task = new ForkExample(array, 0, array.length);
			pool.invoke(task);
		}
	}
}
