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

public class PeerThread implements Runnable {
	
	private BufferedReader peer;
	private String name;
	
	public PeerThread(BufferedReader peerIn, String name) {
		this.peer = peerIn;
		this.name = name;
		
	}

	//Dieser Thread empfängt Nachrichten von einem bestimmten Peer
	@Override
	public void run() {

			String currentMessage;
			try {
				while((currentMessage = peer.readLine()) != null) {
					System.out.println("From "+ name + ": " +currentMessage);
			
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
