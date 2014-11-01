package reactor;

import java.util.ArrayList;
import java.util.HashMap;

import reactorapi.*;
import reactorexample.AcceptHandle;
import reactorexample.TCPTextHandle;
import reactor.BlockingEventQueue;
import reactor.WorkerThread;

public class Dispatcher {
	public Dispatcher() {
		this(10);
	}

	public Dispatcher(int capacity) {
		// TODO: Implement Dispatcher(int).
		this.queue = new BlockingEventQueue<EventHandler<?>>(capacity);
	}

	public void handleEvents() throws InterruptedException {
		// TODO: Implement Dispatcher.handleEvents().
		
		// wait until there is an event. When an event is
		// received then first event is read and then removed
		
		while(!handlerMap.isEmpty())
		{
			Event<?> event = this.select();
			EventHandler eventHandler = event.getHandler();
			
			if (eventHandler != null && handlerMap.containsKey(eventHandler)) {
				if (event.getEvent() == null) {
					this.removeHandler(eventHandler);
				}
				else { 
					//eventHandler.handleEvent(event);
					event.handle(); // this does exactly the same as line above
				}
			}
		}
		
		
	
	}

	public Event<?> select() throws InterruptedException {
		//throw new UnsupportedOperationException();
		// TODO: Implement Dispatcher.select().
		
		// Wait until an event is received on any registered handle
		Event<?> event = queue.get();
		return event;
	}

	public void addHandler(EventHandler<?> h) {
		// TODO: Implement Dispatcher.addHandler(EventHandler).
		
		//Register an unregistered handler; i.e. start dispatching incoming events for h. 
		//All events received on the corresponding handle (h.getHandle()) must be (eventually) 
		//dispatched to h, until removeHandler(h) is called or a null message is received
		
		WorkerThread<?> workerThread = new WorkerThread(h, queue);
		workerThread.start();
		handlerMap.put(h, workerThread);
		numberOfActiveWorkerThreads ++;
	}

	public void removeHandler(EventHandler<?> h) {
		//eventHandlers.remove(h);
		if (handlerMap.containsKey(h)) {
			handlerMap.get(h).cancelThread();
			handlerMap.remove(h);
		}
	}
	
	public void listenToServer(Object m) {
		// TODO: receive message from hangman-server
	}
	
	public void clearConnections() {
		for (EventHandler handler: this.handlerMap.keySet()){
			try {
				((TCPTextHandle)handler.getHandle())
						.close();
			} catch (Exception ex) {
				((AcceptHandle)handler.getHandle())
						.close();
			}
		}
	}
	
	// Add methods and fields as needed.
	private BlockingEventQueue<EventHandler<?>> queue;
	//private ArrayList<EventHandler<?>> eventHandlers = new ArrayList<EventHandler<?>>();
	private HashMap<EventHandler<?>, WorkerThread<?>> handlerMap = new HashMap<EventHandler<?>, WorkerThread<?>>();
	public int numberOfActiveWorkerThreads = 0;
			
}
