package Server;

import java.lang.Thread;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class ServerThread extends Thread {

    public void run(){
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("DU da?");
        try {
		    if (in.readLine().startsWith("/log")) {
			    System.out.println("naaa");
            }
            sleep(2);
		} catch (InterruptedException | IOException e) {
            //
        }
        System.out.println("ciao");
    }
}