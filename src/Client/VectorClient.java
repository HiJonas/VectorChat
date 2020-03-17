package Client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class VectorClient {
	private static final int PORT = 5555;
	private int id;
	PrintWriter serverOut;
	Map<String, Socket> peers;
	private String myName;
	
	public VectorClient() {
		System.out.println("Client starting...");
		startClient();
	}

	private void startClient() {
		Socket server = null;
		peers = new HashMap<String, Socket>();

		try {
			BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
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
			
			boolean nameSet = false;
			while(!nameSet) {
				System.out.println("Please enter a name:");
				currentMessage = consoleInput.readLine();
				serverOut.println("/setname" + currentMessage);
				serverOut.flush();
				String response = reader.readLine();
				System.out.println(response);
				if(response.startsWith("Your")){
					nameSet = true;
					this.myName = currentMessage;
				}
			}
			
			NewPeerListener peerListener = new NewPeerListener(this);
			Thread thread = new Thread(peerListener);
			thread.run();
			
			while (true) {
				currentMessage = consoleInput.readLine();
				if(currentMessage.contains("#")){
					System.out.println("Invalid Message");
					continue;
				}else if(currentMessage.startsWith("/")) {
					handleServerRequest(server, reader, currentMessage);	
				}else if(currentMessage.contains(":")){
					String user = currentMessage.split(":")[0];
					currentMessage = currentMessage.substring(user.length()+1);				
					if(peers.containsKey(user)) {
						PrintWriter peerOut =  new PrintWriter(peers.get(user).getOutputStream());
						peerOut.println(currentMessage);
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
			if(currentMessage.substring(5).equals(myName)) {
				System.out.println("You can't add yourself");
			}else if(peers.containsKey(currentMessage.substring(5))) {
				System.out.println("User already in your contacts");
			}else {
				serverOut.println(currentMessage);
				serverOut.flush();
				String response = reader.readLine();			
				if(response.startsWith("No")) {
					System.out.println(response);							
				}else {
					addNewPeer(server, currentMessage, response);
				}
				
			}			
		}else {
			serverOut.println(currentMessage);
			serverOut.flush();
			String response = reader.readLine();
			System.out.println(response);
		}
	}

	private void addNewPeer(Socket server, String currentMessage, String response)
			throws UnknownHostException, IOException {
		String name = currentMessage.substring(5);	
		
		Socket newSocket = new Socket(response, 55554);
		PrintWriter peerOut =  new PrintWriter(newSocket.getOutputStream());
		peerOut.println(myName);
		peerOut.flush();
		peers.put(name, newSocket);
		
		System.out.println(name + " has been added to your contacts");							
	}

	public void socketOpened(String name, Socket socket) {
		peers.put(name, socket);
		System.out.println(name + " opened a connection");
			
	}

}
