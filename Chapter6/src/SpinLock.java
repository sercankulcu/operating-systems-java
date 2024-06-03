
import java.util.concurrent.atomic.AtomicReference;

/*
 * Here's a basic Java code that demonstrates spin lock in an OS context using AtomicReference:
 * 
 * In this code, we have used AtomicReference to store the owner of the lock. The lock is 
 * acquired by a task when compareAndSet sets the owner to the current task. The lock is released 
 * by setting the owner to null. This approach ensures that the lock is acquired and released in 
 * a thread-safe manner, and the lock is acquired by the task that first calls compareAndSet.
 * 
 * */

public class SpinLock {
	
    static class Lock {
    	
        private AtomicReference<Thread> owner = new AtomicReference<>();
        public void lock() {
            Thread currentThread = Thread.currentThread();
            while (!owner.compareAndSet(null, currentThread)) { }
        }
        public void unlock() {
            owner.set(null);
        }
    }
    static class Task implements Runnable {
        private Lock lock;
        public Task(Lock lock) {
            this.lock = lock;
        }
        public void run() {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + " acquired lock");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                System.out.println(Thread.currentThread().getName() + " released lock");
            }
        }
    }
    public static void main(String[] args) {
    	
        Lock lock = new Lock();
        
        Task task1 = new Task(lock);
        Task task2 = new Task(lock);
        Task task3 = new Task(lock);
        Task task4 = new Task(lock);
        
        new Thread(task1, "Task 1").start();
        new Thread(task2, "Task 2").start();
        new Thread(task3, "Task 3").start();
        new Thread(task4, "Task 4").start();
    }
}
