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

public class ServerThread implements Runnable {
	
	private BufferedReader serverIn;
	private VectorClient vectorClient;
	
	public ServerThread(BufferedReader serverIn, VectorClient vectorClient) {
		this.serverIn = serverIn;
		this.vectorClient = vectorClient;
	}

	//Dieser Thread empfaengt Nachrichten von einem bestimmten Peer
	@Override
	public void run() {

			String currentMessage;
			try {
				while((currentMessage = serverIn.readLine()) != null) {
					String currentMessageHeader = currentMessage.split("#")[0];
					currentMessage = currentMessage.split("#")[1];
					 if(currentMessageHeader.contains("ADD")) {
						String address=currentMessage.split(" ")[0];
						String name=currentMessage.substring(address.length()+1);
						vectorClient.addNewPeer(name, address);					
					}else if(currentMessageHeader.contains("ERROR")) {
						System.out.println("From Server: "+ currentMessage);
					}else if(currentMessageHeader.contains("LOG")) {
						System.out.println("Server is requesting Log...");
						vectorClient.sendLogToServer();
					}else {
						System.out.println("From Server: "+ currentMessage);
					}						
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
