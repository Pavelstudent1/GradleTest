package multithread;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

public class SyncQueueExample {
	
	public static void main(String[] args) {

		SynchronousQueue<String> syncQueue = new SynchronousQueue<>();
		final List<String> list = Arrays.asList("This is a messege to transmit".split(" "));
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					for (String s : list) {
						syncQueue.put(s);
						System.out.println(Thread.currentThread().getName() + " put: " + s);
						Thread.sleep((long) (Math.random() * 1000));
					}
					syncQueue.put("");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "Supplier").start();

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					while (true) {
						String s = syncQueue.take();
//						Thread.sleep((long) (Math.random() * 1000));
						if (s.length() == 0) {
							return;
						}
						System.out.println(Thread.currentThread().getName() + " take: " + s);
					}
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}
		}, "Consumer").start();

	}

}
