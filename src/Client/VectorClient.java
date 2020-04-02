package Client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import Models.MessageDiary;
import Models.VectorClock;

public class VectorClient {
	private static final int SERVERPORT = 25565;
	static final int PEERPORT = 25566;
	private int id;
	PrintWriter serverOut;
	Map<String, Socket> peers;
	private String myName;
	private VectorClock clock;
	private MessageDiary diary;
	
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
			
			server = new Socket(serverAddress, SERVERPORT);
			BufferedReader reader = new BufferedReader(new InputStreamReader(server.getInputStream()));
			serverOut =  new PrintWriter(server.getOutputStream());
						
			String currentMessage;
			String idString = reader.readLine();
			id = Integer.parseInt(idString);
			
			System.out.println("Connecting to Server...");
			System.out.println("Your ID is "+ id);
			
			//Aufforderung Name einzugeben, solange bis valide
			initialNaming(consoleInput, reader);
			
			//Initialisierungen
			diary = new MessageDiary();
			clock = new VectorClock(id);
			
			//Thread starten um auf Server zu hören
			ServerThread serverThread = new ServerThread(reader, this);
			Thread t = new Thread(serverThread);
			t.start();
			
			//Thread starten um auf neue Peer Sockets zu hoeren
			NewPeerListener peerListener = new NewPeerListener(this);
			Thread thread = new Thread(peerListener);
			thread.start();

			//Benutzereingaben entgegennehmen
			while (true) {
				currentMessage = consoleInput.readLine();
				if(currentMessage.contains("#")){
					// Zeichen # reserviert fuer Metainformationen wie VectorClock
					System.out.println("Invalid Message");
					continue;
				}else if(currentMessage.startsWith("/")) {
					//Alle Eingaben mit / gehen an Server
					handleServerRequest(server, reader, currentMessage);	
				}else if(currentMessage.startsWith(".")) {
					//Alle Eingaben mit . geben eigene Informationen aus
					handleLocalRequests(currentMessage);
				}else if(currentMessage.contains(":")){
					//Nachricht an Peer mit name: Nachricht
					String user = currentMessage.split(":")[0];
					currentMessage = currentMessage.substring(user.length()+1);				
					if(peers.containsKey(user)) {
						clock.sendMessage();
						PrintWriter peerOut =  new PrintWriter(peers.get(user).getOutputStream());
						peerOut.println(clock.getClockString() + "#" + currentMessage);
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

	private void initialNaming(BufferedReader consoleInput, BufferedReader reader) throws IOException {
		String currentMessage;
		boolean nameSet = false;
		while(!nameSet) {
			System.out.println("Please enter a name:");
			currentMessage = consoleInput.readLine();
			serverOut.println("/setname" + currentMessage);
			serverOut.flush();
			String response = reader.readLine();
			String responseMeta = response.split("#")[0];
			response = response.split("#")[1];
			System.out.println(response);
			//Response faengt mit Your an sofern Name valide war
			if(!responseMeta.startsWith("ERROR")){
				nameSet = true;
				this.myName = responseMeta;
			}
		}
	}

	private void handleLocalRequests(String currentMessage) {
		if(currentMessage.startsWith(".clock")) {
			System.out.println("Current clock: " + clock.getClockString());
		}else if(currentMessage.startsWith(".diary")) {
			System.out.println("Current Message Diary: " + diary.getDiaryString());
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
			}			
		}else if(currentMessage.startsWith("/setname")){
			System.out.println("You can't change your name");
		}else {
			serverOut.println(currentMessage);
			serverOut.flush();		
		}
	}

	//Eroeffnet Socket zu Peer und schickt eigenen Namen
	public void addNewPeer(String name, String address)
			throws UnknownHostException, IOException {	
		Socket newSocket = new Socket(address, PEERPORT);
		PrintWriter peerOut =  new PrintWriter(newSocket.getOutputStream());
		peerOut.println(myName);
		peerOut.flush();
		peers.put(name, newSocket);
		System.out.println(name + " has been added to contacts");
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(newSocket.getInputStream()));
		PeerThread peerListener = new PeerThread(reader, name, this);
		Thread thread = new Thread(peerListener);
		thread.start();
	}

	//Fuegt einen neuen Peer zur Kontaktliste hinzu
	public void socketOpened(String name, Socket socket) {
		peers.put(name, socket);
		System.out.println(name + " opened a connection");
		System.out.println(name + " has been added to contacts");
			
	}

	public VectorClock getClock() {
		return clock;
	}

	public String getName() {
		return myName;
	}

	public MessageDiary getMessageDiary() {
		return diary;
	}

	public void sendLogToServer() {
		serverOut.println("LOG#"+diary.getDiaryString());
		serverOut.flush();
		System.out.println("Log has been sent to Server");
	}

}
