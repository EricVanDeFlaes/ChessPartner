package application.clientServer;

import java.net.InetAddress;
import java.net.Socket;

public class Client extends DialogManager {
	private InetAddress address;
	
	public Client(InetAddress address) {
		this.address = address;
	}
	
	public void run() {
		try {
			socket = new Socket(address, 54321);
			dialogue();
			socket.close();
		} catch(Exception e) {}
	}
}
