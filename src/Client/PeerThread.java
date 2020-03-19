package Client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import Models.Message;
import Models.VectorClock;
import Server.Client;
import Server.VectorServer;

public class PeerThread implements Runnable {
	
	private BufferedReader peerReader;
	private String peerName;
	private VectorClient client;
	
	public PeerThread(BufferedReader peerIn, String name, VectorClient client) {
		this.peerReader = peerIn;
		this.peerName = name;
		this.client = client;
	}

	//Dieser Thread empfaengt Nachrichten von einem bestimmten Peer
	@Override
	public void run() {

			String currentMessage;
			try {
				while((currentMessage = peerReader.readLine()) != null) {
					String receivedClock = currentMessage.split("#")[0];
					client.getClock().receivedMessage(receivedClock);
					String messageString = currentMessage.split("#")[1];
					
					Message newMessage = new Message(peerName, client.getName(), client.getClock().getClockString(), messageString);
					client.getMessageDiary().addMessage(newMessage);
					
					System.out.println("From "+ peerName + ": " +messageString);
			
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
