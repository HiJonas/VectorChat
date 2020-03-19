package Server;

import java.lang.Thread;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Map;

public class ServerThread extends Thread {

    private VectorServer vectorServer;

    public ServerThread(VectorServer vectorServer) {
        this.vectorServer = vectorServer;
    }

    public void run(){
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
		    if (in.readLine().startsWith("/log")) {
                Map<Integer, Client> clients = vectorServer.getClientList();
                for (int i = 0; i < clients.size(); i ++) {
                    System.out.println("client: " + clients.get(i).getName());
                }
            }
            sleep(2);
		} catch (InterruptedException | IOException e) {
            //
        }
    }
}