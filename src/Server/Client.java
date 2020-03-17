package Server;

import java.net.Socket;

public class Client {
	
	private int id;
	private String name;
	private String address;
	private Socket socket;

	public Client(int id, String name, Socket socket) {
		this.id = id;
		this.name = name;
		this.socket = socket;
		
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return socket.getInetAddress().getHostAddress();
	}

	public Socket getSocket() {
		return socket;
	}
}

	
