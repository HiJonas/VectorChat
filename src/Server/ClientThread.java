package Server;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ClientThread implements Runnable {
	
	private Client client;
	private VectorServer server;

	public ClientThread(Client client, VectorServer server) {
		this.client = client;
		this.server = server;
		
	}

	//Hört auf Nachrichten vom Clienten
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
					//Falls Nachricht mit /setname beginnt soll der Name des Clienten gesetzt werden
					String newName = currentMessage.substring(8);
					if(server.getClients().contains(newName)) {
						newMessage = "Name " + newName +" already exists";
					}else {
						client.setName(newName);	
						newMessage = "Your Name has been set to " + client.getName();
					}
					
				}
				else if(currentMessage.startsWith("/clients")) {
					//Falls Nachricht mit /clients beginnt sollen alle Clientennamen zurückgegeben werden
					StringBuilder messageBuilder = new StringBuilder();
					messageBuilder.append("Active Clients: ");
					List<String> clients  = server.getClients();			
					for(String currentClient: clients) {
						messageBuilder.append(" " + currentClient);
					}
					newMessage = messageBuilder.toString();
					
				}
				else if(currentMessage.startsWith("/add")) {
					//Falls Nachricht mit /add beginnt soll Adresse des angegebenen Clienten zurückgegeben werden
					String userToAdd = currentMessage.substring(4).replace(" ", "");
					Optional<Client> clientToAdd = server.getClientAddress(userToAdd);
					if(clientToAdd.isPresent()) {
						newMessage = clientToAdd.get().getAddress();
					}else {
						newMessage = "No user with name "+ userToAdd;
					}
					
					
				}
				
				//Schreibt Nachrit raus
				out.println(newMessage);
				out.flush();
			}
			
		} catch (Exception e) {
			
		}		
	}

}
