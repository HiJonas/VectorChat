package Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class VectorServer {
	
	private Map<Integer, String> clients;
	private Map<Integer, String> clientAlias;
	
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
			
			serverListen(server);	
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	public void serverListen(ServerSocket server) throws IOException{

		int idCounter = 0;
		clients = new HashMap<>();
		clientAlias = new HashMap<>();
		while(true){
			Socket socket = server.accept();
			String clientAdress;
			clientAdress = socket.getInetAddress().getHostAddress() + socket.getInetAddress().getHostName();

			System.out.println("New Client " + idCounter + " with address " + clientAdress);
			
			ClientThread clientThread = new ClientThread(socket, idCounter, this);
			Thread thread = new Thread(clientThread);
			thread.start();
			
			clients.put(idCounter, clientAdress);
			System.out.println("Listening to Client " + idCounter + " ...");
			idCounter++;
		}
				
	}

	public Set<Integer> getClients() {
		return clients.keySet();
	}

	public void setAlias(Integer id, String alias) {
		clientAlias.put(id, alias);
		
	}

	public String getAlias(Integer id) {
		if(clientAlias.get(id) == null) {
			return ""+id;
		}
		return clientAlias.get(id);
	}

}
