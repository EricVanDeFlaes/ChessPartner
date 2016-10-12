package application.model;

import java.io.Serializable;

import application.main.Application;
import application.model.Case.Coord;
import application.model.Piece.TypePiece;

/**
 * classe de gestion de l'historique des coups
 * @author Addstones
 *
 */
public class HistoryMove implements Serializable {
	private static final long serialVersionUID = 1L;
	public final HistoryPiece originPiece;
	public final Coord destination;
	public final HistoryPiece capturedPiece;
	
	/**
	 * Construction d'un HistoryMove standard (suite au déplacement d'une pièce)
	 * @param piece
	 * @param destination
	 */
	public HistoryMove(Piece piece, Case destination) {
		originPiece = new HistoryPiece(piece);
		this.destination = destination.coord;
		// gestion de la prise en passant
		if (piece.type==TypePiece.Pawn && this.destination.column!=originPiece.coord.column && destination.getContent()==null) {
			capturedPiece = new HistoryPiece(Application.getApp().engine.board.cases[this.destination.column][originPiece.coord.line].getContent());
		} else {
			capturedPiece = (destination.getContent() == null)? null : new HistoryPiece(destination.getContent());
		}
	}

	/** 
	 * Construction d'un HistoryMove manuel permettant de gérer les cas particuliers (mouvement de tour sur un roque) 
	 * @param piece
	 * @param destination
	 */
	public HistoryMove(HistoryPiece piece, Coord destination) {
		originPiece = piece;
		this.destination = destination;
		capturedPiece = null; 
	}

	/**
	 * Notation d'un coup
	 */
	@Override
	public String toString() {
		String notation = originPiece.toString();
		notation += ((capturedPiece==null)?"-":"x"+capturedPiece.type.toString());
		notation += destination.toString();
		return notation;
	}
}
