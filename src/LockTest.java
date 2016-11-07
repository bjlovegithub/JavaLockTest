import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockTest {
  private final int LOCK_TIMES = 10000000;

  public void testObjectLock(int threadNum) throws InterruptedException {
    Object object = new Object();

    Thread[] threads = new Thread[threadNum];

    for (int i = 0; i < threadNum; ++i) {
      Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
          for (int i = 0; i < LOCK_TIMES; ++i) {
            synchronized (object) {

            }
          }
        }
      });

      threads[i] = thread;
    }

    long st = System.currentTimeMillis();

    for (int i = 0 ; i < threadNum; ++i) {
      threads[i].start();
      threads[i].join();
    }

    long et = System.currentTimeMillis();

    System.out.println("testObjectLockWith["+threadNum+"]Thread: "+(et-st));

  }

  public void testReentrantLock(int threadNum) throws InterruptedException {
    Lock lock = new ReentrantLock(true);

    Thread[] threads = new Thread[threadNum];

    for (int i = 0; i < threadNum; ++i) {
      Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
          for (int i = 0; i < LOCK_TIMES; ++i) {
            lock.lock();
            lock.unlock();
          }
        }
      });

      threads[i] = thread;
    }

    long st = System.currentTimeMillis();

    for (int i = 0; i < threadNum; ++i) {
      threads[i].start();
      threads[i].join();
    }
    long et = System.currentTimeMillis();

    System.out.println("testReentrantLockWith["+threadNum+"]Thread: "+(et-st));
  }


  public void testReentrantReadWriteLock(int threadNum) throws InterruptedException {
    ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
    ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    Thread[] threads = new Thread[threadNum];

    for (int i = 0; i < threadNum-1; ++i) {
      Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
          for (int i = 0; i < LOCK_TIMES; ++i) {
            readLock.lock();
            readLock.unlock();
          }
        }
      });

      threads[i] = thread;
    }

    threads[threadNum-1] = new Thread(new Runnable() {
      @Override
      public void run() {
        for (int i = 0; i < LOCK_TIMES; ++i) {
          writeLock.lock();
          writeLock.unlock();
        }
      }
    });

    long st = System.currentTimeMillis();

    for (int i = 0; i < threadNum; ++i) {
      threads[i].start();
      threads[i].join();
    }

    long et = System.currentTimeMillis();

    System.out.println("testReentrantReadLockWith["+threadNum+"]Thread: "+(et-st));
  }

  public static void main(String[] args) throws InterruptedException {
    LockTest test = new LockTest();

    test.testObjectLock(1);

    test.testReentrantLock(1);

    test.testReentrantReadWriteLock(1);

    test.testObjectLock(3);

    test.testReentrantLock(3);

    test.testReentrantReadWriteLock(3);
  }
}
