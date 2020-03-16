import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread implements Runnable {
	
	private Socket socket;
	private String name;

	public ClientThread(Socket socket, String name) {
		this.socket = socket;
		this.name = name;
		
	}

	@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			String currentMessage;
			while((currentMessage = in.readLine()) != null){
				currentMessage = "Answer: " + currentMessage;					
				out.println(currentMessage);
				out.flush();
			}
			
		} catch (Exception e) {
			
		}		
	}

}
