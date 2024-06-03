import java.util.concurrent.Semaphore;

/*
 * Here is an example of a Java program that demonstrates the "readers-writers" problem, 
 * a classic concurrency problem in computer science:
 * 
 * This program defines two classes: Reader and Writer, which represent the threads that will 
 * be reading and writing to a shared resource. The main method creates four threads (two readers 
 * and two writers) and starts them. 
 * 
 * The Reader class uses a semaphore mutex to control access to the shared readCount variable, 
 * which keeps track of the number of readers currently accessing the shared resource. The Writer 
 * class uses a semaphore writeLock to block writers while any readers are accessing the resource.
 * 
 * */

class ReadersWritersExample {

  static int readCount = 0;
  static Semaphore mutex = new Semaphore(1);
  static Semaphore writeLock = new Semaphore(1);

  static class Reader implements Runnable {
    @Override
    public void run() {
      try {
        // acquire lock
        mutex.acquire();
        readCount++;
        if (readCount == 1) {
          // if this is the first reader, block the writers
          writeLock.acquire();
        }
        // release lock
        mutex.release();

        // reading is happening here
        System.out.println("Reader is reading");
        Thread.sleep(1000);

        // acquire lock
        mutex.acquire();
        readCount--;
        if (readCount == 0) {
          // if this is the last reader, release the lock for the writers
          writeLock.release();
        }
        // release lock
        mutex.release();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  static class Writer implements Runnable {
    @Override
    public void run() {
      try {
        // acquire lock
        writeLock.acquire();

        // writing is happening here
        System.out.println("Writer is writing");
        Thread.sleep(1000);

        // release lock
        writeLock.release();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args) {
  	
    Thread reader1 = new Thread(new Reader());
    Thread reader2 = new Thread(new Reader());
    Thread writer1 = new Thread(new Writer());
    Thread writer2 = new Thread(new Writer());

    reader1.start();
    reader2.start();
    writer1.start();
    writer2.start();
  }
}
