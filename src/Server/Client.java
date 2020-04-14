package Server;

import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	
	private int id;
	private String name;
	private String address;
	private Socket socket;
	private PrintWriter printWriter;

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

	public int getPeerPort() {
		return 25566 + id;		
	}
	
	public Socket getSocket() {
		return socket;
	}

	public PrintWriter getPrintWriter() {
		return printWriter;
	}

	public void setPrintWriter(PrintWriter out) {
		this.printWriter = out;
	}
}

	
