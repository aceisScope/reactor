package hangman;

import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;

import reactorapi.*;
import reactorexample.AcceptHandle;
import reactorexample.TCPTextHandle;
import hangmanrules.*;

public class TCPTextHandler implements EventHandler<String> {

	public TCPTextHandle tcpTextHandle = null;
	private HangmanRules<TCPTextHandle> hangmanrules;
	private AcceptHandler acceptHandler;
	
	public TCPTextHandler(Socket newSocket, HangmanRules<TCPTextHandle> rules, AcceptHandler acceptHandler) throws IOException {
		tcpTextHandle = new TCPTextHandle(newSocket);
		this.acceptHandler=acceptHandler;
		this.hangmanrules = rules;
	}
	
	
	@Override
	public Handle<String> getHandle() {
		// TODO Auto-generated method stub
		return this.tcpTextHandle;
	}

	@Override
	public void handleEvent(String s) {
		// TODO Auto-generated method stub
		if (s == null) {
			tcpTextHandle.close();
			hangmanrules.removePlayer(findPlayerbyHandle(tcpTextHandle));
			return;
		}
		
		else {
			if(s.length()!=1)
			{
				hangmanrules.addNewPlayer(tcpTextHandle, s);
				tcpTextHandle.write(hangmanrules.getStatus());
			}
			else
			{
			
				char guess = s.charAt(0);
				System.out.println("Received guess: "+guess);
				hangmanrules.makeGuess(guess);
				
				HangmanRules<TCPTextHandle>.Player player = findPlayerbyHandle(tcpTextHandle);
				String guessString = player.getGuessString(guess);
				BroadCast(guessString);
				
				if (hangmanrules.gameEnded()) {
					Iterator<HangmanRules<TCPTextHandle>.Player> itr = hangmanrules.getPlayers().iterator();
					while (itr.hasNext()) {
						// close all TCPTextHandle sockets
						((TCPTextHandle)(itr.next().playerData)).close();
					}
					
					//TODO: close server socket
					((AcceptHandle)acceptHandler.getHandle()).close();
				}
			}
		}
		
	}

	public HangmanRules<TCPTextHandle>.Player findPlayerbyHandle(TCPTextHandle handle) {
		Iterator<HangmanRules<TCPTextHandle>.Player> itr = hangmanrules.getPlayers().iterator();
		while (itr.hasNext()) {
			HangmanRules<TCPTextHandle>.Player player = itr.next();
			if (player.playerData.equals(handle)) 
				return player;
		}
		return null;
	}
		
	public void BroadCast(String guessString) {
		Iterator<HangmanRules<TCPTextHandle>.Player> itr = hangmanrules.getPlayers().iterator();
		while (itr.hasNext()) {
			HangmanRules<TCPTextHandle>.Player player = itr.next();
			player.playerData.write(guessString);
		}
	}
	
}
