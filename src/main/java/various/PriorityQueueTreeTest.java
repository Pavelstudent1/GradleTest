package various;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

public class PriorityQueueTreeTest
{

	public static void main(String[] args)
	{
		Queue<Integer> pq = new PriorityQueue<Integer>();
		TreeSet<Integer> tree = new TreeSet<>();

		long start = 0, stop = 0, average = 0, rounds = 10;
		final long countToAdd = 100000;
		final TimeUnit fromNanos = TimeUnit.NANOSECONDS;

		for (int i = 0; i < rounds; i++)
		{
			start = System.nanoTime();
			for (int j = 0; j < countToAdd; j++)
			{
				pq.add(j);
			}
			stop = System.nanoTime();
			average += fromNanos.toMillis(stop - start);
		}
		System.out.println("PriorityQueue: " + ((double) average / rounds) + " ms.");

		average = 0;
		for (int i = 0; i < rounds; i++)
		{
			start = System.nanoTime();
			for (int j = (int) (i * countToAdd); j < countToAdd * (i + 1); j++)
			{
				tree.add(j);
			}
			stop = System.nanoTime();
			average += fromNanos.toMillis(stop - start);
		}
		System.out.println("TreeSet: " + ((double) average / rounds) + " ms.");

		System.out.println("PQ: " + pq.size() + "\tTree: " + tree.size());

		// while(!pq.isEmpty()){
		// System.out.println(pq.poll());
		// }

		/**
		 * Instant adding
		 */
		pq = new PriorityQueue<>();
		tree = new TreeSet<>();

		start = System.nanoTime();
		for (int j = 0; j < countToAdd; j++)
		{
			pq.add(j);
		}
		stop = System.nanoTime();
		System.out.println("PriorityQueue: " + fromNanos.toMillis(stop - start) + " ms.");

		start = System.nanoTime();
		for (int j = 0; j < countToAdd; j++)
		{
			tree.add(j);
		}
		stop = System.nanoTime();
		System.out.println("TreeSet: " + fromNanos.toMillis(stop - start) + " ms.");

		System.out.println("PQ: " + pq.size() + "\nTree: " + tree.size());

		Random random = new Random();
		final int pqSize = pq.size();
		start = System.nanoTime();
		for (int j = 0; j < pqSize; j++)
		{
			int nextInt = random.nextInt(pqSize);
			boolean b = pq.contains(nextInt);
			if (!b)
			{
				throw new RuntimeException();
			}
		}
		stop = System.nanoTime();
		System.out.println("Search PriorityQueue: " + fromNanos.toMillis(stop - start) + " ms.");

		final int treeSize = pq.size();
		start = System.nanoTime();
		for (int j = 0; j < treeSize; j++)
		{
			int nextInt = random.nextInt(treeSize);
			boolean b = tree.contains(nextInt);
			if (!b)
			{
				throw new RuntimeException();
			}
		}
		stop = System.nanoTime();
		System.out.println("Search Tree: " + fromNanos.toMillis(stop - start) + " ms.");

	}
}
