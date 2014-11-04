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
			
			if (Thread.currentThread().interrupted()) {
				try {
					throw (new InterruptedException());
				} catch (InterruptedException e) {
					// TODO: handle exception
					break;
				}
			}
			
			T something = handler.getHandle().read();
			try {
				queue.put(new Event<T>(something, handler));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
		}
	}

	public void cancelThread() {
		// TODO: Implement WorkerThread.cancelThread().
		if (isAlive()) {
			this.interrupt();
		}
	}
}