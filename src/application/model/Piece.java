package application.model;

import application.controller.ChessEngine;
import application.main.Application;

public class Piece {
	public enum TypePiece {
		King, Queen, Bishop, Knight, Rook, Pawn;
		
		@Override
		public String toString() {
			switch (this) {
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
	
	public final TypePiece type;
	public final Player player;
	private Case position;
	private boolean moved;
	
	protected Piece(TypePiece type, Player player, boolean moved, Case position) {
		this.type = type;
		this.player = player;
		this.position = position;
		this.moved = moved;
		position.setContent(this);
		player.pieces.add(this);
	}
	
	protected Piece(HistoryPiece piece) {
		this(piece.type, Player.getPlayer(piece.color), piece.moved, Application.getApp().engine.getCase(piece.coord)); 
	}
	
	public Piece(TypePiece type, Player player, Case position) {
		this(type, player, false, position);
	}
	
	public void remove() {
		position.setContent(null);
		player.pieces.remove(this);
	}
	
	public void move(Case destination) {
		position.setContent(null);
		if (destination.getContent() != null) destination.getContent().remove();
		position = destination;
		position.setContent(this);
		moved = true;
	}
	
	public void undo(HistoryMove move) {
		ChessEngine engine = Application.getApp().engine;
		move(engine.getCase(move.originPiece.coord));
		this.moved = move.originPiece.moved;
		if (move.capturedPiece!=null) new Piece(move.capturedPiece);
	}
	
	public Case getPosition() {
		return position;
	}
	
	public boolean hasMoved() {
		return moved;
	}
}
