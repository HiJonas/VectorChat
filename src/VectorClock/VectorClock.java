package VectorClock;

import java.util.ArrayList;

public class VectorClock {
    private ArrayList<Integer> vectorClock;

    public VectorClock() {
        vectorClock = new ArrayList<Integer>();
    }

    public void sendMessage(int id) {
        vectorClock.set(id, vectorClock.get(id)+1);
    }

    public void receivedMessage(int id, int zeitstempel) {
        vectorClock.set(id, vectorClock.get(id)+1);

        for (int i = 0; i < vectorClock.size(); i++) {
            vectorClock.set(i, Math.max(vectorClock.get(id), zeitstempel));
        }
    }
}