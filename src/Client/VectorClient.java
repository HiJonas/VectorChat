package Client;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class VectorClient {
	private int id;

	public VectorClient() {
		System.out.println("Client starting...");
		startClient();
	}

	private void startClient() {
		Socket server = null;

		try {
			BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter Server Address: ");
			String serverAddress = consoleInput.readLine();
			
			server = new Socket(serverAddress, 5555);
			BufferedReader reader = new BufferedReader(new InputStreamReader(server.getInputStream()));
			PrintWriter out = new PrintWriter(server.getOutputStream());
						
			String currentMessage;
			String idString = reader.readLine();
			id = Integer.parseInt(idString);
			
			System.out.println("Connecting to Server...");
			System.out.println("Your ID is "+ id);
			
			boolean nameSet = false;
			while(!nameSet) {
				System.out.println("Please enter a name:");
				currentMessage = consoleInput.readLine();
				out.println("/setname" + currentMessage);
				out.flush();
				String response = reader.readLine();
				System.out.println(response);
				if(response.startsWith("Your")){
					nameSet = true;
				}
			}
			
			
			while (true) {
				currentMessage = consoleInput.readLine();
				out.println(currentMessage);
				out.flush();
				System.out.println(reader.readLine());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
