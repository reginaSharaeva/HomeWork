package ThreadTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Регина on 28.10.2015.
 */
public class MyList<T> {
    public List<T> list = new ArrayList<>();

    int maxCount;

    public MyList(int maxCount) {
        this.maxCount = maxCount;
    }

    final Object lock = new Object();

    void push(T e) {
        synchronized (lock) {
            while (list.size() >= maxCount) {
                System.out.println("Waiting");
                try {
                    lock.wait();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
            list.add(e);
            lock.notifyAll();
            System.out.println("Object in List");
        }
    }

    T pull() {
        synchronized (lock) {
            while (list.size() == 0) {
                System.out.println("Wait object");
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T e = list.remove(list.size() - 1);
            System.out.println("Removed object");
            lock.notify();
            return e;
        }
    }
}
