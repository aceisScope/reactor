package reactor;

import reactorapi.*;

public class WorkerThread<T> extends Thread {
	private final EventHandler<T> handler;
	private final BlockingEventQueue<Object> queue;

	// Additional fields are allowed.

	public WorkerThread(EventHandler<T> eh, BlockingEventQueue<Object> q) {
		handler = eh;
		queue = q;
	}

	public void run() {
		// TODO: Implement WorkerThread.run().
		while(true)
		{
			Event<? extends Object> event = (Event<? extends Object>) handler.getHandle().read();
			if (event == null) {
				this.cancelThread(); 
			}
			try {
				queue.put(event);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void cancelThread() {
		// TODO: Implement WorkerThread.cancelThread().
		this.interrupt();
	}
}