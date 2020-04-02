package Server;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class VectorServer {
	
	private static final int SERVERPORT = 25565;
	private Map<Integer, Client> clients;
	private List<String> logs;

	public VectorServer() {	
		System.out.println("Server starting...");
		startServerSocket();
	}

	public void  startServerSocket(){
		try {
			ServerSocket server = new ServerSocket(SERVERPORT);
			InetAddress adresse = InetAddress.getLocalHost();
			System.out.println("Server started");
			System.out.println("Running at:  " +  adresse.getHostAddress());

			ServerConsoleThread serverConsoleThread = new ServerConsoleThread(this);
			Thread thread = new Thread(serverConsoleThread);
			thread.start();
			
			serverListen(server);	
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	public void serverListen(ServerSocket server) throws IOException{

		//Laufende ID fuer CLienten
		int idCounter = 0;
		
		clients = new HashMap<>();
		
		//Hoert auf neue Clienten
		while(true){
			Socket socket = server.accept();
			String clientAdress;
			clientAdress = socket.getInetAddress().getHostAddress();

			System.out.println("New Client " + idCounter + " with address " + clientAdress);

			//Erzeugt Clienten Objekt und ClientThread um auf eingehende Nachrichten zu lauschen
			Client newClient = new Client(idCounter, ""+ idCounter, socket);
			ClientThread clientThread = new ClientThread(newClient, this);
			Thread thread = new Thread(clientThread);
			thread.start();
			
			clients.put(newClient.getId(), newClient);
			System.out.println("Listening to Client " + idCounter + " ...");
			idCounter++;			
		}
				
	}

	//Gibt Liste an registrierten Clienten Namen zurueck
	public List<String> getClients() {
		return clients.values().stream().map(c -> c.getName()).collect(Collectors.toList());
	}

	public Map<Integer, Client> getClientList() {
		return clients;
	}

	//Gibt Optional die Adresse eines Clients zurueck
	public Optional<Client> getClientAddress(String userToAdd) {
		return clients.values().stream()
				.filter(c -> c.getName().equals(userToAdd))
				.findFirst();
		
	}
	
	public void resetLogs() {
		logs = Collections.synchronizedList(new ArrayList<String>());
	}

	
	public void addLogs(String string) {
		logs.add(string);	
	}

	public void evaluateLogs() {
		System.out.println("Unsorted Logs:");
		for(String log:logs) {
			System.out.println(log);
		}
		Collections.sort(logs, new VectorComparator());	
		System.out.println("Sorted Logs:");
		for(String log:logs) {
			System.out.println(log);
		}
	}

}
