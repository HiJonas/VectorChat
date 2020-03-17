package Server;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Set;

public class ClientThread implements Runnable {
	
	private Socket socket;
	private int id;
	private VectorServer server;

	public ClientThread(Socket socket, Integer id, VectorServer server) {
		this.socket = socket;
		this.id = id;
		this.server = server;
		
	}

	@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			String currentMessage;
			String newMessage;
			while((currentMessage = in.readLine()) != null){
				newMessage = currentMessage;
				if(currentMessage.startsWith("/setname")) {
					server.setAlias(id, currentMessage.substring(8));	
					newMessage = "Your Name has been set to " + server.getAlias(id);
				}
				else if(currentMessage.startsWith("/clients")) {
					StringBuilder messageBuilder = new StringBuilder();
					messageBuilder.append("Active Clients: ");
					Set<Integer> clients  = server.getClients();			
					for(Integer currentClient: clients) {
						messageBuilder.append(" " + currentClient);
					}
					newMessage = messageBuilder.toString();
					
				}
				out.println(newMessage);
				out.flush();
			}
			
		} catch (Exception e) {
			
		}		
	}

}
