package reactor;

import reactorapi.BlockingQueue;

import java.util.LinkedList;
import java.util.List;

public class BlockingEventQueue<T> implements BlockingQueue<Event<? extends T>> {
	public BlockingEventQueue(int capacity) {
		this.capacity = capacity;
	}

	public int getSize() {
		synchronized (this) {
			return this.queue.size();
		}
	}

	public int getCapacity() {		
		return this.capacity;
	}

	public Event<? extends T> get() throws InterruptedException {
		
		synchronized (this) {
			while (this.queue.isEmpty()) {
				wait();
			}
			
			if(this.queue.size() == this.capacity){
			      notifyAll();
			    }
			
			//System.out.println("dequeue");
			//notifyAll();
			return queue.remove(0);
		}

	}

	public synchronized List<Event<? extends T>> getAll() {
		throw new UnsupportedOperationException(); // Replace this.
		// TODO: Implement BlockingEventQueue.getAll().
	}

	public void put(Event<? extends T> event) throws InterruptedException {		
		synchronized (this) {
			while (this.queue.size() == this.capacity) {
				wait();
			}
			
			if(this.queue.size() == 0) {
			      notifyAll();
			    }
			queue.add(event);
			//System.out.println("enqueue");
			//notifyAll();
		}
	}

	// Add other methods and variables here as needed.
	private LinkedList<Event<? extends T>> queue = new LinkedList<Event<? extends T>>();
	final private int capacity;
}