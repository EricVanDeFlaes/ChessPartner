package application.model;

import java.util.LinkedList;

public class Player {
	public enum Color { White, Black };

	private static final Player players[] = { new Player(Color.White), new Player(Color.Black) };
	
	public final Color color;
	public final LinkedList<Piece> pieces = new LinkedList<Piece>();
	
	public static Player getPlayer(Color color) {
		return (color == Color.White) ? players[0] : players[1];
	}
	
	private Player(Color color) {
		this.color = color;
	}
	
	public Player getOpponent() {
		return (color == Color.White) ? players[1] : players[0];
	}
}
