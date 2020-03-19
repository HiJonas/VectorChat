package Models;

import java.util.ArrayList;

public class VectorClock {
    private static final String SEPARATOR = ",";
	private ArrayList<Integer> vectorClock;
	private int id;

    public VectorClock(int id) {
        this.id = id;
		vectorClock = new ArrayList<Integer>();
		for(int i = 0; i <= id; i++) {
			vectorClock.add(0);
		}
    }

    public void sendMessage() {
        vectorClock.set(id, vectorClock.get(id)+1);
    }

    public void receivedMessage(String newClockString) {
        vectorClock.set(id, vectorClock.get(id)+1);
        String newClock[] = newClockString.split(SEPARATOR);
        if(newClock.length > vectorClock.size()) {
        	//Empfangene Clock ist laenger als eigene also werdn zusaetzliche uebernommen
        	for (int i = 0; i < vectorClock.size(); i++) {
                vectorClock.set(i, Math.max(vectorClock.get(id), Integer.parseInt(newClock[i])));
            }
        	for(int i = vectorClock.size(); i< newClock.length; i++) {
        		vectorClock.add(Integer.parseInt(newClock[i]));
        	}
        }else {
        	for (int i = 0; i < newClock.length; i++) {
                vectorClock.set(i, Math.max(vectorClock.get(id), Integer.parseInt(newClock[i])));
            }
        }     
                
    }
    
    public String getClockString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append(vectorClock.get(0));
        for (int i = 1; i < vectorClock.size(); i++) {
        	sb.append(SEPARATOR+vectorClock.get(i));
        }
    	
        return sb.toString();  	
    }
}