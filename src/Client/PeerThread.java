package Client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import Server.Client;
import Server.VectorServer;
import VectorClock.VectorClock;

public class PeerThread implements Runnable {
	
	private BufferedReader peer;
	private String name;
	private VectorClock clock;
	
	public PeerThread(BufferedReader peerIn, String name, VectorClock clock) {
		this.peer = peerIn;
		this.name = name;
		this.clock = clock;
		
	}

	//Dieser Thread empfängt Nachrichten von einem bestimmten Peer
	@Override
	public void run() {

			String currentMessage;
			try {
				while((currentMessage = peer.readLine()) != null) {
					String receivedClock = currentMessage.split("#")[0];
					clock.receivedMessage(receivedClock);
					String message = currentMessage.split("#")[1];
					System.out.println("From "+ name + ": " +message);
			
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
