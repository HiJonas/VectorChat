package Models;

public class Message {
	
	private String sender;
	private String recipient;
	private String clock;
	private String message;

	public Message(String from, String to, String clock, String message) {
		this.sender = from;
		this.recipient = to;
		this.clock = clock;
		this.message = message;
		
	}

	public String getFrom() {
		return sender;
	}

	public String getTo() {
		return recipient;
	}

	public String getClock() {
		return clock;
	}

	public String getMessage() {
		return message;
	}
	
	

}
