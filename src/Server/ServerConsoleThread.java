package Server;

import java.lang.Thread;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Map;

public class ServerConsoleThread implements Runnable {

    private VectorServer vectorServer;

    public ServerConsoleThread(VectorServer vectorServer) {
        this.vectorServer = vectorServer;
    }

    //Hoert auch Konsoleneingabe uns sendet an alle Clienten
    public void run(){
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            try {
    		    if (in.readLine().startsWith("/log")) {
                	vectorServer.resetLogs();
                    Map<Integer, Client> clients = vectorServer.getClientList();
                    for (int i = 0; i < clients.size(); i ++) {
                    	PrintWriter out = clients.get(i).getPrintWriter();
                    	out.println("LOG#"+clients.get(i).getName());
                    	out.flush();                   	
                    }
                    
                    System.out.println("Waiting for client replies...");
                    try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    vectorServer.evaluateLogs();
                }
    		} catch (IOException e) {
                //
            }
        }   
    }
}