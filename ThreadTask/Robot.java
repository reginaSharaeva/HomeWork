package ThreadTask;

/**
 * Created by Регина on 28.10.2015.
 */
public class Robot {

    public static class RobotEx extends Thread {
        static Object lock = new Object();

        private String name;

        public RobotEx(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            for (;;) {
                synchronized (lock) {
                    step();
                    lock.notify();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        private synchronized void step() {
            System.out.println("Step: " + name);
        }
    }

    public static void main(String... args) {
        new Thread(new RobotEx("left")).start();
        new Thread(new RobotEx("right")).start();
    }

}
