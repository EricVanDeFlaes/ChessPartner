package application.model;

public class Piece {
	public enum TypePiece {
		King, Queen, Bishop, Knight, Rook, Pawn;
	}
	
	private final TypePiece type;
	private final Player player;
	private Case position;
	private boolean bMoved;
	
	public Piece(TypePiece type, Player player, Case position) {
		this.type = type;
		this.player = player;
		this.position = position;
		this.bMoved = false;
		position.setContent(this);
		player.getPieces().add(this);
	}
	
	public TypePiece getType() {
		return type;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Case getPosition() {
		return position;
	}
	
	public void remove() {
		position.setContent(null);
		player.getPieces().remove(this);
	}
	
	public void move(Case destination) {
		position.setContent(null);
		if (destination.getContent() != null) destination.getContent().remove();
		position = destination;
		destination.setContent(this);
		bMoved = true;
	}
	
	public boolean hasMoved() {
		return bMoved;
	}
}
