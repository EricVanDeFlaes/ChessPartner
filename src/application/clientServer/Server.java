package application.clientServer;

import java.io.IOException;
import java.net.ServerSocket;

public class Server extends DialogManager {
	private ServerSocket serverSocket = null;
	
	public Server() {
		try {
			this.serverSocket = new ServerSocket(54321, 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
	    try {
	    	// on se remet en attente d'une connexion quand la connexion se termine
	    	while(true) {
				socket = serverSocket.accept();
				dialogue();
				socket.close();
				socket = null;
			}
	    } catch (IOException e) {
			e.printStackTrace();
	    }
	}	
}	