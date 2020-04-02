package Models;

import java.util.ArrayList;
import java.util.List;

//Diese Klasse verwaltet empfangene und gesendete Nachrichten
public class MessageDiary {
		
	private List<Message> messages;
	
	public MessageDiary() {
		messages = new ArrayList();
	}
	
	public void addMessage(Message newMessage) {
		messages.add(newMessage);
	}
	
	public String getDiaryString() {
		StringBuilder sb = new StringBuilder();
		if(messages.isEmpty()) {
			return "";
		}
		buildString(sb, messages.get(0));
		for(int i=1; i<messages.size(); i++) {
			sb.append("#");
			buildString(sb, messages.get(i));
		}
		
		return sb.toString();
	}

	private void buildString(StringBuilder sb, Message message) {
		sb.append("FROM "+ message.getFrom());
		sb.append(" TO "+ message.getTo());
		sb.append(" AT "+ message.getClock());
		sb.append(": "+ message.getMessage());
		
	}

}
