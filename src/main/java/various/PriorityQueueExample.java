package various;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class PriorityQueueExample {
	
	public static void main(String[] args) {
		Random rand = new Random(); 
		Queue<Integer> pq = new PriorityQueue<Integer>();
		
		for (int i = 0; i < 10; i++) {
			pq.add(rand.nextInt(100));
		}
		
		while(!pq.isEmpty()){
			System.out.println(pq.poll());
		}
		
		for (int i = 10; i > 0; i--) {
			pq.add(i);
		}
		
		pq.remove(5);
		
		while(!pq.isEmpty()){
			System.out.println(pq.poll());
		}
		
	}
}
