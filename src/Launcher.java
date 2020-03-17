import java.util.Scanner;

import Client.VectorClient;
import Server.VectorServer;

public class Launcher {

	public static void main(String[] args) {
		System.out.println("VectorChat");
		System.out.println("1. Start Client");
		System.out.println("2. Start Server");
		
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		while(!(input.equals("1") || input.equals("2"))) {
			System.out.println("Invalid input");
			input = scanner.nextLine();
		}
		if(input.equals("1")) {
			new VectorClient();
		}else {
			new VectorServer();
		}
	}

}
