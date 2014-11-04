package reactor;

import reactorapi.*;

public class WorkerThread<T> extends Thread {
	private final EventHandler<T> handler;
	private final BlockingEventQueue<Object> queue;
	private boolean stop = false;

	// Additional fields are allowed.

	public WorkerThread(EventHandler<T> eh, BlockingEventQueue<Object> q) {
		handler = eh;
		queue = q;
	}

	public void run() {
		// TODO: Implement WorkerThread.run().
			
			while(!stop)
			{
				
//				if (Thread.currentThread().interrupted()) {
//					try {
//						throw (new InterruptedException());
//					} catch (InterruptedException e) {
//						// TODO: handle exception
//						break;
//					}
//				}
				
				try {
					T something = handler.getHandle().read();
//					System.out.println("I will try to put "+something);
					queue.put(new Event<T>(something, handler));
					if (something == null) stop = true;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					if (!stop){
						System.out.println("something very wrong happens");
						return;
					}
					e.printStackTrace();
					break;
				}
				
			}	
			
	}

	public void cancelThread() {
		// TODO: Implement WorkerThread.cancelThread().
		if (isAlive()) {
//			System.out.println("cancel worker thread ");
			stop = true;
//			this.interrupt();
		}
	}
}