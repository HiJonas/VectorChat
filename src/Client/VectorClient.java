package Client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import VectorClock.VectorClock;

public class VectorClient {
	private static final int PORT = 5555;
	private int id;
	PrintWriter serverOut;
	Map<String, Socket> peers;
	private String myName;
	private VectorClock clock;
	
	public VectorClient() {
		System.out.println("Client starting...");
		startClient();
	}

	private void startClient() {
		Socket server = null;
		peers = new HashMap<String, Socket>();

		try {
			BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
			
			//Eingabe der Serveradresse
			System.out.println("Enter Server Address: ");
			String serverAddress = consoleInput.readLine();
			
			server = new Socket(serverAddress, PORT);
			BufferedReader reader = new BufferedReader(new InputStreamReader(server.getInputStream()));
			serverOut =  new PrintWriter(server.getOutputStream());
						
			String currentMessage;
			String idString = reader.readLine();
			id = Integer.parseInt(idString);
			
			System.out.println("Connecting to Server...");
			System.out.println("Your ID is "+ id);
			
			//Aufforderung Name einzugeben, solange bis valide
			boolean nameSet = false;
			while(!nameSet) {
				System.out.println("Please enter a name:");
				currentMessage = consoleInput.readLine();
				serverOut.println("/setname" + currentMessage);
				serverOut.flush();
				String response = reader.readLine();
				System.out.println(response);
				//Response fängt mit Your an sofern Name valide war
				if(response.startsWith("Your")){
					nameSet = true;
					this.myName = currentMessage;
				}
			}
			
			clock = new VectorClock(id);
			
			//Thread starten um auf neue Peer Sockets zu hören
			NewPeerListener peerListener = new NewPeerListener(this);
			Thread thread = new Thread(peerListener);
			thread.start();

			//Benutzereingaben entgegennehmen
			while (true) {
				currentMessage = consoleInput.readLine();
				if(currentMessage.contains("#")){
					// Zeichen # reserviert für Metainformationen wie VectorClock
					System.out.println("Invalid Message");
					continue;
				}else if(currentMessage.startsWith("/")) {
					//Alle Eingaben mit / gehen an Server
					handleServerRequest(server, reader, currentMessage);	
				}else if(currentMessage.startsWith(".")) {
					//Alle Eingaben mit . geben eigene Informationen aus
					if(currentMessage.startsWith(".clock")) {
						System.out.println("Current clock: " + clock.getString());
					} 
				}else if(currentMessage.contains(":")){
					//Nachricht an Peer mit name: Nachricht
					String user = currentMessage.split(":")[0];
					currentMessage = currentMessage.substring(user.length()+1);				
					if(peers.containsKey(user)) {
						clock.sendMessage();
						PrintWriter peerOut =  new PrintWriter(peers.get(user).getOutputStream());
						peerOut.println(clock.getString() + "#" + currentMessage);
						peerOut.flush();
					}else {
						System.out.println(user + " is not in contacts");
					}
				}				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void handleServerRequest(Socket server, BufferedReader reader, String currentMessage)
			throws IOException, UnknownHostException {
		if(currentMessage.startsWith("/add")) {
			//Falls request an Server mit /add angefangen hat
			
			if(currentMessage.substring(5).equals(myName)) {
				System.out.println("You can't add yourself");
			}else if(peers.containsKey(currentMessage.substring(5))) {				
				System.out.println("User already in your contacts");
			}else {
				serverOut.println(currentMessage);
				serverOut.flush();
				String response = reader.readLine();	
				
				//Falls response mit No anfängt gab es einen Fehler, ansonsten ist response Adresse
				if(response.startsWith("No")) {
					//Falls response mit No anfängt invalider Name
					System.out.println(response);							
				}else {
					addNewPeer(currentMessage, response);
				}
				
			}			
		}else {
			serverOut.println(currentMessage);
			serverOut.flush();
			String response = reader.readLine();
			System.out.println(response);
		}
	}

	//Eröffnet Socket zu Peer und schickt eigenen Namen
	private void addNewPeer(String currentMessage, String address)
			throws UnknownHostException, IOException {
		String name = currentMessage.substring(5);	
		
		Socket newSocket = new Socket(address, 55554);
		PrintWriter peerOut =  new PrintWriter(newSocket.getOutputStream());
		peerOut.println(myName);
		peerOut.flush();
		peers.put(name, newSocket);
		
		System.out.println(name + " has been added to contacts");							
	}

	//Fügt einen neuen Peer zur Kontaktliste hinzu
	public void socketOpened(String name, Socket socket) {
		peers.put(name, socket);
		System.out.println(name + " opened a connection");
		System.out.println(name + " has been added to contacts");
			
	}

	public VectorClock getClock() {
		return clock;
	}

}
