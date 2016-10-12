package application.model;

import java.io.Serializable;

import application.model.Case.Coord;
import application.model.Piece.TypePiece;
import application.model.Player.Color;

public class HistoryPiece implements Serializable {
	private static final long serialVersionUID = 1L;
	public final Color color;
	public final TypePiece type;
	public final boolean moved;
	public final Coord coord;
	
	public HistoryPiece(TypePiece type, Color color, boolean moved, Coord coord) {
		this.type = type;
		this.color = color;
		this.moved = moved;
		this.coord = coord;
	}
	
	public HistoryPiece(Piece piece) {
		this(piece.type, piece.player.color, piece.hasMoved(), piece.getPosition().coord);
	}
	
	@Override
	public String toString() {
		return type.toString() + coord.toString();
	}
}
