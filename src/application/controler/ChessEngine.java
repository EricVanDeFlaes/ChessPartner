package application.controler;

import java.util.ArrayList;

import application.model.Board;
import application.model.Case;
import application.model.Piece;
import application.model.Player;

/**
 * Classe gérant une partie d'échec
 * @author Addstones
 *
 */
public class ChessEngine {
	private Board board;
	private Player player;
	
	public ChessEngine() {
		board = new Board();
		newGame();
	}
	
	public void newGame() {
		// On initialise la partie
		player = Player.getPlayer(Player.Color.White);
		board.setInitialPosition();
	}
	
	/**
	 * Vérifie si un coup est valide
	 * @param piece
	 * @param destination
	 * @return
	 */
	public boolean checkMove(Piece piece, Case destination) {		
		return getValidMoves(piece).contains(destination);
	}

	/**
	 * Renvoie la liste des coups valides
	 * @param piece
	 * @return
	 */
	public ArrayList<Case> getValidMoves(Piece piece) {
		ArrayList<Case> validMoves = new ArrayList<Case>();
		switch (piece.getType()) {
			case King:
				break;
			case Queen:
				break;
			case Bishop:
				break;
			case Knight:
				break;
			case Rook:
				break;
			case Pawn:
				break;
		}
		return validMoves;
	}
}
