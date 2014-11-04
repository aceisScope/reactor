package hangman;

import hangmanrules.HangmanRules;

import java.io.IOException;
import java.util.Random;

import reactor.Dispatcher;
import reactorapi.EventHandler;
import reactorapi.Handle;
import reactorexample.ReactorExample;
import reactorexample.ReactorExample.StringHandle;
import reactorexample.ReactorExample.StringHandler;
import reactorexample.TCPTextHandle;

public class HangmanServer 
{
	Dispatcher d = new Dispatcher();
	AcceptHandler ah;
	Random r = new Random();
	String expected = "Reactor test completed OK.";
	
	HangmanRules<TCPTextHandle> rules;

	public static void main(String[] args) {
		HangmanServer server = new HangmanServer(args[0], Integer.parseInt(args[1]));
		server.execute();
	}

	public HangmanServer(String word, int max_attempts) {
		
		rules = new HangmanRules<TCPTextHandle>(word, max_attempts);
		
		try {
			ah = new AcceptHandler(d, rules);
			d.addHandler(ah);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	public void execute() {
		/* Main loop. */
		try {
			d.handleEvents();
		} catch (InterruptedException ie) {
			return;
		}

	}

	/**
	 * Handle that returns characters one at a time slowly from a
	 * creator-supplied String.
	 */
	public class StringHandle implements Handle<Character> {
		int position = 0;
		String data;

		public StringHandle(String s) {
			data = s;
		}

		public Character read() {
			try {
				Thread.sleep(r.nextInt(50));
			} catch (InterruptedException ie) {
			}

			if (position >= data.length())
				return null;
			// System.err.println("Returning: "+data.charAt(position));
			return new Character(data.charAt(position++));
		}
	}

	}
