import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
                    myList.push(10);
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
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    myList.pull();
                }
            }
        }).start();
    }
}
