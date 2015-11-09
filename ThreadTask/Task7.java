package ThreadTask;

import ThreadTask.MyList;

/**
 * Created by Регина on 28.10.2015.
 */
public class Task7 {
    public static void main(String[] args) {
        MyList myList = new MyList(4);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    myList.push(new MyList<Integer>(10));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    myList.pull();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (i < 10) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    myList.pull();
                    i++;
                }
            }
        }).start();

    }
}
