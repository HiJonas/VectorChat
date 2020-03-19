package Server;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class VectorServer {
	
	private Map<Integer, Client> clients;

	public VectorServer() {
		System.out.println("Server starting...");
		startServerSocket();
	}

	public void  startServerSocket(){
		try {
			ServerSocket server = new ServerSocket(5555);
			InetAddress adresse = InetAddress.getLocalHost();
			System.out.println("Server started");
			System.out.println("Running at:  " +  adresse.getHostAddress());

			ServerThread thread = new ServerThread();
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

	//Gibt Optional die Adresse eines Clients zurueck
	public Optional<Client> getClientAddress(String userToAdd) {
		return clients.values().stream()
				.filter(c -> c.getName().equals(userToAdd))
				.findFirst();
		
	}

}
