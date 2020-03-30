package Client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import Server.Client;
import Server.ClientThread;
import Server.VectorServer;

public class NewPeerListener implements Runnable {

	
	private VectorClient vectorClient;

	public NewPeerListener(VectorClient vectorClient) {
		this.vectorClient = vectorClient;

	}
	
	//Dieser Thread hoert auf neue Socket Verbindungen und eroeffnet einen nueen PeerThread
	@Override
	public void run() {
		try {
			ServerSocket server = new ServerSocket(vectorClient.PEERPORT);		
			InetAddress adresse = InetAddress.getLocalHost();
			while(true){
				Socket socket = server.accept();
				String clientAdress;
				clientAdress = socket.getInetAddress().getHostAddress();
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String name = reader.readLine();
				//Hinzufuegen des neuen Peers zur Kontaktliste
				vectorClient.socketOpened(name, socket);
				
				PeerThread peerThread = new PeerThread(reader, name, vectorClient);
				Thread thread = new Thread(peerThread);
				thread.start();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
