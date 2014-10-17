package hangman;

import java.io.IOException;
import java.net.Socket;

import reactorapi.*;
import reactorexample.TCPTextHandle;

public class TCPTextHanlder implements EventHandler<String> {

	public TCPTextHandle tcpTextHandle = null;
	
	public TCPTextHanlder(Socket newSocket) throws IOException {
		tcpTextHandle = new TCPTextHandle(newSocket);
	}
	
	@Override
	public Handle<String> getHandle() {
		// TODO Auto-generated method stub
		return this.tcpTextHandle;
	}

	@Override
	public void handleEvent(String s) {
		// TODO Auto-generated method stub
		
	}

}
