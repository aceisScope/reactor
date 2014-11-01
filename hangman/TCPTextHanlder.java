package hangman;

import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;

import reactorapi.*;
import reactorexample.TCPTextHandle;
import hangmanrules.*;

public class TCPTextHanlder implements EventHandler<String> {

	public TCPTextHandle tcpTextHandle = null;
	private HangmanRules<TCPTextHandle> hangmanrules;
	
	public TCPTextHanlder(Socket newSocket) throws IOException {
		tcpTextHandle = new TCPTextHandle(newSocket);
	}
	
	public TCPTextHanlder(Socket newSocket, HangmanRules<TCPTextHandle> hr) throws IOException {
		tcpTextHandle = new TCPTextHandle(newSocket);
		this.hangmanrules = hr;
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
			//TODO:hangmanrules.removePlayer(this_player);
			return;
		}
		
		else {
			char guess = s.charAt(0);
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
				
				// close server socket
				//ssh.close();
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
