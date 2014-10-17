package hangman;

import java.io.IOException;
import java.net.Socket;

import reactor.Dispatcher;
import reactorapi.*;
import reactorexample.AcceptHandle;

public class AcceptHandler implements EventHandler<Socket>{
	
	private Dispatcher d = null;
	private AcceptHandle acceptHandle = null;
	
	public AcceptHandler(Dispatcher d) throws IOException {
		this.d = d;
		this.acceptHandle = new AcceptHandle();
		
	}

	@Override
	public Handle<Socket> getHandle() {
		// TODO Auto-generated method stub
		return this.acceptHandle;
	}

	@Override
	public void handleEvent(Socket s) {
		// TODO Auto-generated method stub

			try {
				d.addHandler(new TCPTextHanlder(s));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	
}
