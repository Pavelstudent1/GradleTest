package multithread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockExample
{
	private static long LIMIT = 10;

	public static void main(String[] args) throws InterruptedException
	{
		final int[] counter = { 0 };
		final ReadWriteLock rwlock = new ReentrantReadWriteLock(false);

		RWTask rwTask1 = new RWTask(rwlock, counter);
		RWTask rwTask2 = new RWTask(rwlock, counter);
		RWTask rwTask3 = new RWTask(rwlock, counter);

		Thread t1 = new Thread(new WhileCycle(rwTask1));
		Thread t2 = new Thread(new WhileCycle(rwTask2));
		Thread t3 = new Thread(new WhileCycle(rwTask3));

		t1.start();
		t2.start();
		t3.start();

		t3.join();
		t2.join();
		t1.join();

		System.out.println("FINISH");
	}

	private static class RWTask
	{
		private int[] counter;
		private Lock w;
		private Lock r;

		public RWTask(ReadWriteLock rwlock, int[] counter)
		{
			this.r = rwlock.readLock();
			this.w = rwlock.writeLock();
			this.counter = counter;
		}

		public void write()
		{
			w.lock();
			try
			{
				System.out.println(Thread.currentThread().getName() + " writes value: " + ++counter[0]);
			}
			finally
			{
				w.unlock();
			}
		}

		public int read()
		{
			r.lock();
			try
			{
				System.out.println(Thread.currentThread().getName() + " reads value: " + counter[0]);
			}
			finally
			{
				r.unlock();
			}
			return counter[0];
		}
	}

	private static class WhileCycle implements Runnable
	{
		private RWTask runnable;
		
		public WhileCycle(RWTask runnable)
		{
			this.runnable = runnable;
		}

		@Override
		public void run()
		{
			try
			{
				while (runnable.read() < LIMIT)
				{
					runnable.write();
					Thread.sleep(1);
				}
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

	}
}
