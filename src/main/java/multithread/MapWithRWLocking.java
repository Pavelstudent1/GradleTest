package multithread;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class MapWithRWLocking
{
	private static final int ATTEMPT_NUMBER = 1000;
	private static final String VOCABULARY = "abcdifghijklmnopqrstuvwxyz";
	private static final int WORDS_LENGTH_GENERATION = 100;
	private static final int PERC_READ = 2;
	private static final int PERC_WRITE = 10;
	private static final int PERC_REM = 20;

	private static final int DELAY_CONST_MILLIS = 10;

	/**
	 * 1 == syncronized 2 == rw lock 3 == reentrant lock
	 */

	private static int choose = 1;

	private static BlockingQueue<String> bqueue = new LinkedBlockingQueue<>();

	private static class MapRW
	{
		private Map<Integer, String> map = new HashMap<>();
		private ReentrantReadWriteLock rw = new ReentrantReadWriteLock();
		private ReadLock r;
		private WriteLock w;
		private Lock rrlock = new ReentrantLock();

		public MapRW()
		{
			r = rw.readLock();
			w = rw.writeLock();
		}

		public String getRWLock(Integer key) throws InterruptedException
		{
			r.lock();
			try
			{
				Thread.sleep(DELAY_CONST_MILLIS);
				return map.get(key);
			}
			finally
			{
				r.unlock();
			}
		}

		public void putRWLock(Integer key, String value) throws InterruptedException
		{
			w.lock();
			try
			{
				Thread.sleep(DELAY_CONST_MILLIS);
				map.put(key, value);
			}
			finally
			{
				w.unlock();
			}
		}

		public String remRWLock(Integer key) throws InterruptedException
		{
			w.lock();
			try
			{
				Thread.sleep(DELAY_CONST_MILLIS);
				return map.remove(key);
			}
			finally
			{
				w.unlock();
			}
		}

		public int sizeRWLock()
		{
			r.lock();
			try
			{
				return map.size();
			}
			finally
			{
				r.unlock();
			}
		}

		public String getRLock(Integer key) throws InterruptedException
		{
			rrlock.lock();
			try
			{
				Thread.sleep(DELAY_CONST_MILLIS);
				return map.get(key);
			}
			finally
			{
				rrlock.unlock();
			}
		}

		public void putRLock(Integer key, String value) throws InterruptedException
		{
			rrlock.lock();
			try
			{
				Thread.sleep(DELAY_CONST_MILLIS);
				map.put(key, value);
			}
			finally
			{
				rrlock.unlock();
			}
		}

		public String remRLock(Integer key) throws InterruptedException
		{
			rrlock.lock();
			try
			{
				Thread.sleep(DELAY_CONST_MILLIS);
				return map.remove(key);
			}
			finally
			{
				rrlock.unlock();
			}
		}

		public int sizeRLock()
		{
			rrlock.lock();
			try
			{
				return map.size();
			}
			finally
			{
				rrlock.unlock();
			}
		}

		public synchronized String getSync(Integer key) throws InterruptedException
		{
			Thread.sleep(DELAY_CONST_MILLIS);
			return map.get(key);
		}

		public synchronized String remSync(Integer key) throws InterruptedException
		{
			Thread.sleep(DELAY_CONST_MILLIS);
			return map.remove(key);
		}

		public synchronized void putSync(Integer key, String value) throws InterruptedException
		{
			Thread.sleep(DELAY_CONST_MILLIS);
			map.put(key, value);
		}

		public synchronized int sizeSync()
		{
			return map.size();
		}

	}

	public static void main(String[] args) throws InterruptedException
	{
		long start = System.nanoTime();

		final MapRW mapRW = new MapRW();

		Runnable rlock1 = null;
		Runnable rlock2 = null;
		Runnable rlock3 = null;
		Runnable rlock4 = null;
		Runnable rlock5 = null;
		Runnable rlock6 = null;

		switch (choose)
		{
		case 1:
			rlock1 = new RunnerSync(mapRW);
			rlock2 = new RunnerSync(mapRW);
			rlock3 = new RunnerSync(mapRW);
			rlock4 = new RunnerSync(mapRW);
			rlock5 = new RunnerSync(mapRW);
			rlock6 = new RunnerSync(mapRW);
			break;
		case 2:
			rlock1 = new RunnerRWLock(mapRW);
			rlock2 = new RunnerRWLock(mapRW);
			rlock3 = new RunnerRWLock(mapRW);
			rlock4 = new RunnerRWLock(mapRW);
			rlock5 = new RunnerRWLock(mapRW);
			rlock6 = new RunnerRWLock(mapRW);
			break;
		case 3:
			rlock1 = new RLRunnerLock(mapRW);
			rlock2 = new RLRunnerLock(mapRW);
			rlock3 = new RLRunnerLock(mapRW);
			rlock4 = new RLRunnerLock(mapRW);
			rlock5 = new RLRunnerLock(mapRW);
			rlock6 = new RLRunnerLock(mapRW);
			break;
		}

		Thread t1 = new Thread(rlock1);
		Thread t2 = new Thread(rlock2);
		Thread t3 = new Thread(rlock3);
		Thread t4 = new Thread(rlock4);
		Thread t5 = new Thread(rlock5);
		Thread t6 = new Thread(rlock6);

		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		t6.start();

		t6.join();
		t5.join();
		t4.join();
		t3.join();
		t2.join();
		t1.join();

		for (String d : bqueue)
		{
			System.out.println(d);
		}

		long end = System.nanoTime();
		System.out.println("FINISH: " + TimeUnit.NANOSECONDS.toMillis(end - start) + " ms. " + "CHOOSED: "
				+ (choose == 1 ? "Sync" : (choose == 2 ? "RW lock" : "RR lock")));

	}

	private static class RunnerSync implements Runnable
	{
		private MapRW map;
		private int attempts = 0;
		private ThreadLocalRandom random;

		public RunnerSync(MapRW map)
		{
			random = ThreadLocalRandom.current();
			this.map = map;
		}

		@Override
		public void run()
		{
			final int vocabLength = VOCABULARY.length();
			final String t_name = Thread.currentThread().getName();

			try
			{
				while (attempts < ATTEMPT_NUMBER)
				{
					if (attempts % PERC_READ == 0)
					{
						String sync = map.getSync(random.nextInt());
						System.err.println(t_name + " reads: " + sync);
					}

					if (attempts % PERC_REM == 0)
					{
						String remSync = map.remSync(random.nextInt());
						System.err.println(t_name + " removes: " + remSync);
					}

					if (attempts % PERC_WRITE == 0)
					{
//						final int choosedLength = random.nextInt(vocabLength);
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < WORDS_LENGTH_GENERATION; i++)
						{
							char ch = VOCABULARY.charAt(random.nextInt(vocabLength));
							sb.append(ch);
						}

						sb.trimToSize();
						final String str = sb.toString();
						System.err.println(t_name + " puts: " + str);

						map.putSync(random.nextInt(), str);
					}

					++attempts;
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}

			// System.out.println(t_name + " final size: " + map.sizeSync());
			try
			{
				bqueue.put(t_name + " final size: " + map.sizeSync());
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	private static class RunnerRWLock implements Runnable
	{
		private MapRW map;
		private int attempts = 0;
		private ThreadLocalRandom random;

		public RunnerRWLock(MapRW map)
		{
			this.random = ThreadLocalRandom.current();
			this.map = map;
		}

		@Override
		public void run()
		{
			final int vocabLength = VOCABULARY.length();
			final String t_name = Thread.currentThread().getName();

			try
			{
				while (attempts < ATTEMPT_NUMBER)
				{
					if (attempts % PERC_READ == 0)
					{
						String lock = map.getRWLock(random.nextInt());
						System.err.println(t_name + " reads: " + lock);
					}

					if (attempts % PERC_REM == 0)
					{
						String remLock = map.remRWLock(random.nextInt());
						System.err.println(t_name + " removes: " + remLock);
					}

					if (attempts % PERC_WRITE == 0)
					{
//						final int choosedLength = random.nextInt(vocabLength);
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < WORDS_LENGTH_GENERATION; i++)
						{
							char ch = VOCABULARY.charAt(random.nextInt(vocabLength));
							sb.append(ch);
						}

						sb.trimToSize();
						final String str = sb.toString();
						System.err.println(t_name + "puts: " + str);

						map.putRWLock(random.nextInt(), str);
					}

					++attempts;
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}

			try
			{
				bqueue.put(t_name + " final size: " + map.sizeRWLock());
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			// System.out.println(t_name + " final size: " + map.sizeRWLock());
		}
	}

	private static class RLRunnerLock implements Runnable
	{
		private MapRW map;
		private int attempts = 0;
		private ThreadLocalRandom random;

		public RLRunnerLock(MapRW map)
		{
			this.random = ThreadLocalRandom.current();
			this.map = map;
		}

		@Override
		public void run()
		{
			final int vocabLength = VOCABULARY.length();
			final String t_name = Thread.currentThread().getName();

			try
			{
				while (attempts < ATTEMPT_NUMBER)
				{
					if (attempts % PERC_READ == 0)
					{
						String lock = map.getRLock(random.nextInt());
						System.err.println(t_name + " reads: " + lock);
					}

					if (attempts % PERC_REM == 0)
					{
						String remLock = map.remRLock(random.nextInt());
						System.err.println(t_name + " removes: " + remLock);
					}

					if (attempts % PERC_WRITE == 0)
					{
//						final int choosedLength = random.nextInt(vocabLength);
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < WORDS_LENGTH_GENERATION; i++)
						{
							char ch = VOCABULARY.charAt(random.nextInt(vocabLength));
							sb.append(ch);
						}

						sb.trimToSize();
						final String str = sb.toString();
						System.err.println(t_name + "puts: " + str);

						map.putRLock(random.nextInt(), str);
					}

					++attempts;
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}

			try
			{
				bqueue.put(t_name + " final size: " + map.sizeRLock());
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			// System.out.println(t_name + " final size: " + map.sizeRLock());
		}
	}
}
