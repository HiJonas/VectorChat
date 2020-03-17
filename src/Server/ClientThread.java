package Server;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Set;

public class ClientThread implements Runnable {
	
	private Client client;
	private VectorServer server;

	public ClientThread(Client client, VectorServer server) {
		this.client = client;
		this.server = server;
		
	}

	@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getSocket().getInputStream()));			
			PrintWriter out = new PrintWriter(client.getSocket().getOutputStream());
			String currentMessage;
			String newMessage;
			
			out.println(""+client.getId());
			out.flush();
			
			
			while((currentMessage = in.readLine()) != null){
				newMessage = currentMessage;
				if(currentMessage.startsWith("/setname")) {
					String newName = currentMessage.substring(8);
					if(server.getClients().contains(newName)) {
						newMessage = "Name " + client.getName() +" already exists";
					}else {
						client.setName(newName);	
						newMessage = "Your Name has been set to " + client.getName();
					}
					
				}
				else if(currentMessage.startsWith("/clients")) {
					StringBuilder messageBuilder = new StringBuilder();
					messageBuilder.append("Active Clients: ");
					List<String> clients  = server.getClients();			
					for(String currentClient: clients) {
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
