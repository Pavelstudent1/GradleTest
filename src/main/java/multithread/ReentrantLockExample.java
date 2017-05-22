package multithread;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockExample
{
	public static class TestClass
	{
		private ReentrantLock lock;
		private long counter;

		public TestClass(ReentrantLock lock)
		{
			this.lock = lock;
			this.counter = 0;
		}

		public void inc()
		{
			lock.lock();
			try
			{
				++counter;
			}
			finally
			{
				System.out.println(Thread.currentThread().getName() + ": " + counter);
				lock.unlock();
			}
		}

		public long get()
		{
			return counter;
		}
	}

	public static class RunnableLockingTest implements Runnable
	{
		private int LIMIT = 10;
		private TestClass tc;

		public RunnableLockingTest(TestClass tc)
		{
			this.tc = tc;
		}

		@Override
		public void run()
		{
			int counter = 0;
			while (counter < LIMIT)
			{
				try
				{
					tc.inc();
					// System.out.println(Thread.currentThread().getName() + ":
					// " + tc.get());
					Thread.sleep(1000);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				++counter;
			}
		}

	}

	public static void main(String[] args) throws InterruptedException
	{
		
		Thread.sleep(15000);
		
		final ReentrantLock lock = new ReentrantLock();
		final TestClass tc = new TestClass(lock);

		Thread t1 = new Thread(new RunnableLockingTest(tc));
		Thread t2 = new Thread(new RunnableLockingTest(tc));
		Thread t3 = new Thread(new RunnableLockingTest(tc));

		t1.start();
		t2.start();
		t3.start();

		t3.join();
		t2.join();
		t1.join();

		System.out.println("FINISH");
	}

}
