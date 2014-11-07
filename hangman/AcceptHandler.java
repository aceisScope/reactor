package hangman;

import hangmanrules.HangmanRules;

import java.io.IOException;
import java.net.Socket;

import reactor.Dispatcher;
import reactorapi.*;
import reactorexample.AcceptHandle;
import reactorexample.TCPTextHandle;

public class AcceptHandler implements EventHandler<Socket>{
	
	private Dispatcher d = null;
	private AcceptHandle acceptHandle = null;
	private HangmanRules<TCPTextHandle> rules;
	
	public AcceptHandler(Dispatcher d, HangmanRules<TCPTextHandle> rules) throws IOException {
		this.d = d;
		this.acceptHandle = new AcceptHandle();
		this.rules = rules;
	}

	@Override
	public Handle<Socket> getHandle() {
		// TODO Auto-generated method stub
		return this.acceptHandle;
	}

	@Override
	public void handleEvent(Socket s) {
		// TODO Auto-generated method stub
		
		if (s == null) {
			acceptHandle.close();
			return ;
		}

		try {
			d.addHandler(new TCPTextHandler(s, rules, this, d));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			try {
				s.close();
			} catch (IOException _e) {
				_e.printStackTrace();
				return;
			}
		}
	}
	
	
}
