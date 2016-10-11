package application.clientServer;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerPort {
	public ServerSocket connectSocket=null;
	
	public ServerPort() {
		try {
			connectSocket = new ServerSocket(54321, 1);
		} catch (IOException e) {}
	}
	
	public void close() {
		if (connectSocket != null) try { 
			connectSocket.close(); 
		} catch (IOException e) {}
	}
}
