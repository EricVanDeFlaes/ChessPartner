package application.model;

import java.io.Serializable;

import application.model.Piece.TypePiece;
import application.model.Player.Color;

public class HistoryPiece implements Serializable {
	private static final long serialVersionUID = 1L;
	public final Color color;
	public final TypePiece type;
	public final boolean moved;
	
	protected HistoryPiece(TypePiece type, Color color, boolean moved) {
		this.type = type;
		this.color = color;
		this.moved = moved;
	}
	
	public HistoryPiece(Piece piece) {
		this(piece.type, piece.player.color, piece.hasMoved());
	}
	
	@Override
	public String toString() {
		switch (type) {
			case King:
				return "K";
			case Queen:
				return "Q";
			case Bishop:
				return "B";
			case Knight:
				return "N";
			case Rook:
				return "R";
			default:
				return "";
		}
	}
}
