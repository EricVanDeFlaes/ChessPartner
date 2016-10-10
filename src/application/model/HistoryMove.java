package application.model;

import java.io.Serializable;

import application.model.Case.Coord;

/**
 * classe de gestion de l'historique des coups
 * @author Addstones
 *
 */
public class HistoryMove implements Serializable {
	private static final long serialVersionUID = 1L;
	public final HistoryPiece originPiece;
	public final Coord origin;
	public final Coord destination;
	public final HistoryPiece capturedPiece;
	
	public HistoryMove(Piece piece, Case destination) {
		originPiece = new HistoryPiece(piece);
		origin = piece.getPosition().coord;
		this.destination = destination.coord;
		capturedPiece = (destination.getContent() == null)? null : new HistoryPiece(destination.getContent());
	}
	
	public HistoryMove(HistoryPiece piece, Coord origin, Coord destination) {
		originPiece = piece;
		this.origin = origin;
		this.destination = destination;
		capturedPiece = null; 
	}
	
	/**
	 * Notation d'un coup
	 */
	@Override
	public String toString() {
		String notation = originPiece.toString() + origin.toString();
		notation += ((capturedPiece==null)?"-":"x"+capturedPiece.toString());
		notation += destination.toString();
		return notation;
	}
}
