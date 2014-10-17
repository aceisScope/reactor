package reactor;

import java.util.ArrayList;

import reactorapi.*;
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
		
		Event<?> event = this.select();
		EventHandler<?> eventHandler = event.getHandler();
		if (eventHandler != null && eventHandlers.indexOf(event) != -1) {
			eventHandler.handleEvent(event);
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
		
		eventHandlers.add(h);
		WorkerThread<?> workerThread = new WorkerThread(h, queue);
		workerThread.start();
	}

	public void removeHandler(EventHandler<?> h) {
		// TODO: Implement Dispatcher.removeHandler(EventHandler).
		eventHandlers.remove(h);
		h = null;
	}
	
	public void listenToServer(Object m) {
		// TODO: receive message from hangman-server
	}
	
	// Add methods and fields as needed.
	private BlockingEventQueue<EventHandler<?>> queue;
	public ArrayList<EventHandler<?>> eventHandlers = new ArrayList<EventHandler<?>>();
			
}
