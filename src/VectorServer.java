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

		int counter = 0;
		while(true){
			Socket socket = server.accept();
			String clientAdress;
			clientAdress = socket.getInetAddress().getHostAddress() + socket.getInetAddress().getHostName();
			
			System.out.println("New Client " + counter + " with address " + clientAdress);
			
			ClientThread clientThread = new ClientThread(socket, "Client Number: "+ counter);
			Thread thread = new Thread(clientThread);
			thread.start();
			
			System.out.println("Listening to Client " + counter + " ...");
			counter++;
		}
				
	}
}
