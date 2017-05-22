package multithread;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Exchanger;

public class ExchangerExample {
	
	public static void main(String[] args) {
		
		Exchanger<String> exchanger = new Exchanger<>();
		final List<String> list = Arrays.asList("This is a messege to transmit from".split(" "));
		
		new Thread(new Runnable() {
			
			StringBuilder sb = new StringBuilder();
			
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName() + " start work");
				for(String s : list){
					sb.append(s).append(" ");
					try {
						Thread.sleep((long) (Math.random() * 1000));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				sb.append(Thread.currentThread().getName());
				String fromOther = null;
				try {
					fromOther = exchanger.exchange(sb.toString());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName() + " take: " + fromOther);
			}
		}).start();
		
		new Thread(new Runnable() {
			
			StringBuilder sb = new StringBuilder();
			
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName() + " start work");
				for(String s : list){
					sb.append(s).append(" ");
					try {
						Thread.sleep((long) (Math.random() * 1000));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				sb.append(Thread.currentThread().getName());
				String fromOther = null;
				try {
					fromOther = exchanger.exchange(sb.toString());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName() + " take: " + fromOther);
			}
		}).start();
		
	}

}
