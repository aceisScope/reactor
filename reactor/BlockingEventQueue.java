package reactor;

import reactorapi.BlockingQueue;

import java.util.LinkedList;
import java.util.List;

public class BlockingEventQueue<T> implements BlockingQueue<Event<? extends T>> {
	
	
	public class Semaphore {
	    private int counter;
	    public Semaphore() {
	        this(0);
	    }
	    public Semaphore(int i) {
	        if (i < 0) throw new IllegalArgumentException(i + " < 0");
	        counter = i;
	    }

	    public synchronized void release() {
	        counter++;
	        this.notify();
	    }

	    public synchronized void acquire() throws InterruptedException {
	        while (counter == 0) {
	            this.wait();
	        }
	        counter--;
	    }
	}
	
	public BlockingEventQueue(int capacity) {
		this.capacity = capacity;
		
		this.notEmpty = new Semaphore(0);
		this.notFull = new Semaphore(capacity);
	}

	public int getSize() {
		int size = 0;
		synchronized (this.queue) {
			size = queue.size();
		}
		return size;
	}

	public int getCapacity() {		
		return this.capacity;
	}

	public Event<? extends T> get() throws InterruptedException {
		
//		synchronized (this) {
//			while (this.queue.isEmpty()) {
//				wait();
//			}
//			
//			if(this.queue.size() == this.capacity){
//			      notifyAll();
//			    }
//
//			return queue.remove(0);
//		}
		
		notEmpty.acquire();
		
		Event<? extends T> tmp;
		synchronized (queue) {
			tmp = queue.remove(0);
		}
		notFull.release();
		return tmp;
	}

	public synchronized List<Event<? extends T>> getAll() {
		throw new UnsupportedOperationException(); // Replace this.
		// TODO: Implement BlockingEventQueue.getAll().
	}

	public void put(Event<? extends T> event) throws InterruptedException {		
//		synchronized (this) {
//			while (this.queue.size() == this.capacity) {
//				wait();
//			}
//			
//			if(this.queue.size() == 0) {
//			      notifyAll();
//			    }
//			queue.add(event);
//		}
		
		notFull.acquire();
		
		synchronized (queue) {
			queue.add(event);
		}
		notEmpty.release();
	}

	// Add other methods and variables here as needed.
	private LinkedList<Event<? extends T>> queue = new LinkedList<Event<? extends T>>();
	final private int capacity;
	
	private Semaphore notFull;
	private Semaphore notEmpty;
	
}