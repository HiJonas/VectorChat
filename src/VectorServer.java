import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class VectorServer {
	
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

		while(true){
			Socket client = server.accept();
			String clientAdresse;
			clientAdresse = client.getInetAddress().getHostAddress() + client.getInetAddress().getHostName();
			System.out.println(clientAdresse);
			
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				
				PrintWriter out = new PrintWriter(client.getOutputStream());
				String currentMessage;
				while((currentMessage = in.readLine()) != null){
					currentMessage = "Answer: " + currentMessage;					
					out.println(currentMessage);
					out.flush();
					System.out.println("Sendt: " + currentMessage);
				}
				
			} catch (Exception e) {
				
			}
		}
				
	}
}
