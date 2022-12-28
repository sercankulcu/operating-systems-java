public class ReadersWritersExample {
    private static final int NUM_READERS = 5;
    private static final int NUM_WRITERS = 2;
    private static final Object readerMutex = new Object();
    private static final Object writerMutex = new Object();
    private static int numReaders = 0;
    private static boolean writeInProgress = false;

    public static void main(String[] args) {
        // Create the readers
        for (int i = 0; i < NUM_READERS; i++) {
            new Thread(() -> {
                while (true) {
                    synchronized (readerMutex) {
                        numReaders++;
                        if (numReaders == 1) {
                            synchronized (writerMutex) {
                                writeInProgress = true;
                            }
                        }
                    }
                    // Read
                    System.out.println("Reader is reading");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (readerMutex) {
                        numReaders--;
                        if (numReaders == 0) {
                            synchronized (writerMutex) {
                                writeInProgress = false;
                            }
                        }
                    }
                }
            }).start();
        }
        // Create the writers
        for (int i = 0; i < NUM_WRITERS; i++) {
            new Thread(() -> {
                while (true) {
                    synchronized (writerMutex) {
                        while (writeInProgress) {
                            try {
                                writerMutex.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        // Write
                        System.out.println("Writer is writing");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }
}
