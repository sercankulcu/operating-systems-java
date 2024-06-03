
public class DiningPhilosophers {
	
    public static void main(String[] args) {
    	
        Philosopher[] philosophers = new Philosopher[5];
        Object[] forks = new Object[philosophers.length];

        for (int i = 0; i < forks.length; i++) {
            forks[i] = new Object();
        }

        for (int i = 0; i < philosophers.length; i++) {
            Object leftFork = forks[i];
            Object rightFork = forks[(i + 1) % forks.length];

            if (i == philosophers.length - 1) {
                philosophers[i] = new Philosopher(rightFork, leftFork);
            } else {
                philosophers[i] = new Philosopher(leftFork, rightFork);
            }
            Thread thread = new Thread(philosophers[i], "Philosopher " + (i + 1));
            thread.start();
        }
    }
}

class Philosopher implements Runnable {
  private Object leftFork;
  private Object rightFork;

  public Philosopher(Object leftFork, Object rightFork) {
      this.leftFork = leftFork;
      this.rightFork = rightFork;
  }

  @Override
  public void run() {
      try {
          while (true) {
              doAction("Thinking");
              synchronized (leftFork) {
                  doAction("Picked up left fork");
                  synchronized (rightFork) {
                      doAction("Picked up right fork-eating");
                      doAction("Put down right fork");
                  }
                  doAction("Put down left fork");
              }
          }
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
  }

  private void doAction(String action) throws InterruptedException {
      System.out.println(
              Thread.currentThread().getName() + " " + action);
      Thread.sleep(((int) (Math.random() * 1000)));
  }
}