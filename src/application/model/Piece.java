package application.model;

import application.controller.ChessEngine;
import application.main.Application;

public class Piece {
	public enum TypePiece {
		King, Queen, Bishop, Knight, Rook, Pawn;
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
	
	protected Piece(HistoryPiece piece, Case position) {
		this(piece.type, Player.getPlayer(piece.color), piece.moved, position); 
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
		position.setContent(null);
		position = engine.getCase(move.origin);
		position.setContent(this);
		this.moved = move.originPiece.moved;
		if (move.capturedPiece != null) new Piece(move.capturedPiece, engine.getCase(move.destination));
	}
	
	public Case getPosition() {
		return position;
	}
	
	public boolean hasMoved() {
		return moved;
	}
}
