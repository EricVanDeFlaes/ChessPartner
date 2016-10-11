package application.clientServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import application.main.Application;
import application.model.HistoryMove;

public class DialogManager extends Thread {
	public enum Message { NewGame((byte)100), Move((byte)101), Disconnect((byte)102);
		public final byte value;		
		Message(byte value) {
			this.value = value;
		}
		public static Message getMessage(byte value) {
			for (Message t: values()) {
				if (t.value == value) return t;
			}
			return null;
		}
	}

	protected Socket socket = null;
	
	protected void dialogue() {
		// boucle de communication
		boolean dialog = true;
		try {
			while (dialog) {
				if (socket.getInputStream().available()==0) {
					sleep(300);
				} else {
					Message msg = Message.getMessage((byte)socket.getInputStream().read());
					switch (msg) {
						case NewGame:
							Application.getApp().engine.newGame();
							break;
						case Move:
							// on désérialise le move et on le joue
							ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
							HistoryMove move = (HistoryMove) ois.readObject();
							Application.getApp().engine.playDistant(move);
							break;
						case Disconnect:
							dialog=false;
							break;
					}
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void newGame() {
		try {
			socket.getOutputStream().write(Message.NewGame.value);
			socket.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void move(HistoryMove move) {
		try {
			socket.getOutputStream().write(Message.Move.value);
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(move);
			socket.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect() {
		try {
			socket.getOutputStream().write(Message.Disconnect.value);
			socket.getOutputStream().flush();
			socket.close();
			socket = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
