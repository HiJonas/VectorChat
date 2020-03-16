import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class VectorClient {

	public VectorClient() {
		System.out.println("Client starting...");
		startClient();
	}

	private void startClient() {
		Socket server = null;

		try {
			server = new Socket("127.0.0.1", 5555);
			BufferedReader reader = new BufferedReader(new InputStreamReader(server.getInputStream()));
			PrintWriter out = new PrintWriter(server.getOutputStream());
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

			String currentMessage;
			while (true) {
				System.out.println(": ");
				currentMessage = input.readLine();
				out.println(currentMessage);
				out.flush();
				System.out.println(reader.readLine());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
