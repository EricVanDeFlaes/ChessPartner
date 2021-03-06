package application.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;

import javax.swing.JTextArea;
import application.main.Application;
import application.model.Board;
import application.model.Board.Delta;
import application.model.Board.Direction;
import application.model.Case;
import application.model.Case.Coord;
import application.model.Piece.TypePiece;
import application.model.History;
import application.model.HistoryMove;
import application.model.HistoryPiece;
import application.model.Piece;
import application.model.Player;

/**
 * Classe g�rant une partie d'�chec
 * @author Addstones
 */
public class ChessEngine {
	public final Board board;
	private Player player;
	private History history;
	
	public ChessEngine() {
		board = new Board();
	}
	
	/**
	 * reproduit la notation du coup dans le panel info
	 * @param move
	 */
	private void notateMove(HistoryMove move) {
		JTextArea txtArea = Application.getApp().mainWindow.panelInfo.textArea;
		String notation = move.toString();
		txtArea.setText(txtArea.getText() + notation + " ");
	}
	
	/**
	 * d�place une pi�ce sur le plateau sans v�rifier que c'est un coup valide
	 * @param piece
	 * @param destination
	 */
	private void doMove(Piece piece, Case destination) {
		if (piece.type==Piece.TypePiece.King && Math.abs(piece.getPosition().coord.column - destination.coord.column)>1) {
			// gestion du roque, on va chercher la tour pour la d�placer
			for (Piece rook: piece.player.pieces) {
				if ((rook.type==Piece.TypePiece.Rook) &&
						((destination.coord.column==2 && rook.getPosition().coord.column==0) ||
						(destination.coord.column==6 && rook.getPosition().coord.column==7))) {
					rook.move(board.cases[(destination.coord.column==2)?3:5][destination.coord.line]);
					break;
				}
			}
		} else if (piece.type==Piece.TypePiece.Pawn && piece.getPosition().coord.column!=destination.coord.column && getCase(destination.coord).getContent()==null) {
			// Gestion de la capture de la prise en passant
			board.cases[destination.coord.column][piece.getPosition().coord.line].getContent().remove();
		}
		piece.move(destination);
		player = player.getOpponent();
	}
	
	/**
	 * Lance une nouvelle partie
	 */
	public void newGame() {
		history = new History();
		player = Player.getPlayer(Player.Color.White);
		board.setInitialPosition();
		Application.getApp().mainWindow.panelInfo.textArea.setText("");
		Application.getApp().mainWindow.panelInfo.panelHistorique.refresh(history);
	}
	
	/**
	 * Annule un coup
	 */
	public void undo() {
		if (!history.canUndo()) return;
		HistoryMove move = history.undo();
		Piece piece = getCase(move.destination).getContent();
		// gestion du roque, on undo �galement la tour
		if (piece.type==TypePiece.King && Math.abs(move.originPiece.coord.column - move.destination.column)>1) {
			Case rookOrigin, rookDestination;
			switch (move.destination.column) {
				case 6: // petit roque
					rookDestination = board.cases[5][move.originPiece.coord.line];
					rookOrigin = board.cases[7][move.originPiece.coord.line];
					break;
				default: // grand roque
					rookDestination = board.cases[3][move.originPiece.coord.line];
					rookOrigin = board.cases[0][move.originPiece.coord.line];
					break;
			}
			HistoryPiece rook = new HistoryPiece(Piece.TypePiece.Rook, piece.player.color, false, rookOrigin.coord);
			HistoryMove rookMove = new HistoryMove(rook, rookDestination.coord);
			getCase(rookMove.destination).getContent().undo(rookMove);
		}
		piece.undo(move);
		player = player.getOpponent();

		// On supprime le coup de la notation de la partie
		JTextArea txtArea = Application.getApp().mainWindow.panelInfo.textArea;
		String notation = move.toString() + " ";
		txtArea.setText(txtArea.getText().substring(0, txtArea.getText().length()-notation.length()));
		Application.getApp().mainWindow.panelInfo.panelHistorique.refresh(history);
	}
	
	/**
	 * Rejoue un coup de la liste d'historique
	 */
	public void redo() {
		if (history.isLast()) return;
		HistoryMove move = history.redo();
		doMove(getCase(move.originPiece.coord).getContent(), Application.getApp().engine.getCase(move.destination));
		notateMove(move);
		Application.getApp().mainWindow.panelInfo.panelHistorique.refresh(history);
	}	
	
	/**
	 * revient au d�but de l'historique
	 */
	public void moveFirst() {
		while (history.canUndo()) undo();
	}
	
	/**
	 * revient � la fin de l'historique
	 */
	public void moveLast() {
		while (!history.isLast()) redo();
	}	
	
	/**
	 * V�rifie si un coup est valide
	 * @param piece
	 * @param destination
	 * @return
	 */
	public boolean checkMove(Piece piece, Case destination) {		
		return getValidMoves(piece).contains(destination);
	}
	
	/**
	 * d�place une pi�ce sur le plateau en v�rifiant que c'est un coup valide
	 * @param piece
	 * @param destination
	 * @throws ChessEngineException
	 */
	public void move(Piece piece, Case destination) throws ChessEngineException {
		if (piece.player!=player) throw new ChessEngineException("WrongPlayer");
		if (!getValidMoves(piece).contains(destination)) throw new ChessEngineException("WrongDestination");
		HistoryMove move = new HistoryMove(piece, destination);
		history.add(move);
		doMove(piece, destination);
		notateMove(move);
		Application.getApp().mainWindow.panelInfo.panelHistorique.refresh(history);
		switch (Application.getApp().serverMode) {
			case StandAlone:
				break;
			case Server:
				Application.getApp().server.move(move);
				break;
			case Client:
				Application.getApp().client.move(move);
				break;
		}
	}

	/**
	 * Renvoie la liste des coups valides
	 * @param piece
	 * @return
	 */
	public HashSet<Case> getValidMoves(Piece piece) {
		HashSet<Case> controlled = getControlled(piece);
		HashSet<Case> validMoves = new HashSet<Case>();
		Case checked;
		switch (piece.type) {
			// Gestion des 8 cases du roi
			case King:
				// r�cup�ration de la liste des cases controll�es par l'adversaire
				HashSet<Case> oppControlled = getControlled(piece.player.getOpponent());
				// On teste les cases controll�es par le Roi
				for (Case caseChecked: controlled) {
					if (caseChecked.getContent()==null || caseChecked.getContent().player!=piece.player) {
						if (!oppControlled.contains(caseChecked)) validMoves.add(caseChecked);
					}
				}
				// gestion du roque
				Piece pieceChecked;
				if (!piece.hasMoved() && !oppControlled.contains(piece.getPosition())) {
					Direction directions[] = { Direction.Left, Direction.Right };
					for (Direction direction: directions) {
						checked = board.getCaseContigue(piece.getPosition(), direction);
						// on boucle sur les cases vides dans cette direction
						while (checked != null) {
							pieceChecked = checked.getContent();
							if (pieceChecked != null) {
								if (pieceChecked.player==piece.player && pieceChecked.type==Piece.TypePiece.Rook && !pieceChecked.hasMoved()) {
									// on v�rifie que la case interm�diaire et la case de destination ne sont pas controll�es par l'adversaire
									checked = board.getCaseContigue(piece.getPosition(), direction);
									if (!oppControlled.contains(checked)) {
										checked = board.getCaseContigue(checked, direction);
										if (!oppControlled.contains(checked)) validMoves.add(checked);
									}
								}
								break;
							}
							checked = board.getCaseContigue(checked, direction);
						}
					}
				}
				return validMoves;
			
			// Gestion des 3 ou 4 cases du pion
			case Pawn:
				int deltaLine = (piece.player.color==application.model.Player.Color.White)?1:-1;
				// On teste les deux cases devant le pion
				checked = board.getCaseDelta(piece.getPosition(), new Delta(0, deltaLine));
				if (checked.getContent()==null) {
					validMoves.add(checked);
					if (!piece.hasMoved()) {
						checked = board.getCaseDelta(checked, new Delta(0, deltaLine));
						if (checked.getContent()==null) validMoves.add(checked);
					}
				}
				// on rajoute les cases de capture
				for (Case caseChecked: controlled) {
					if (caseChecked.getContent()==null) {
						// gestion de la prise en passant, le dernier coup jou� est il un pion � cot�?
						HistoryMove move = history.get();
						if (move!=null && move.originPiece.type==TypePiece.Pawn && move.originPiece.moved==false && move.destination.line==piece.getPosition().coord.line && move.destination.column==caseChecked.coord.column) {
							validMoves.add(caseChecked);
						}						
					} else if (caseChecked.getContent().player!=piece.player) {
						validMoves.add(caseChecked);
					}
				}
				return validMoves;
				
			// Les coups valides des autres pieces repr�sentent les cases qu'ils controllent 
			// qui ne contiennent pas de pi�ce de leur couleur
			default:
				for (Case caseChecked: controlled) {
					if (caseChecked.getContent()==null || caseChecked.getContent().player!=piece.player) {
						validMoves.add(caseChecked);
					}
				}
				return validMoves;
		}
	}
	
	/**
	 * Renvoie la liste des cases controll�es par une pi�ce
	 * @param piece
	 * @return
	 */
	public HashSet<Case> getControlled(Piece piece) {
		HashSet<Case> controlled = new HashSet<Case>(14);
		HashSet<Direction> directions = new HashSet<Direction>(8);
		Case caseChecked;
		switch (piece.type) {
			// Gestion des 8 cases du roi
			case King:
				for (Direction direction: Direction.values()) {
					caseChecked = board.getCaseContigue(piece.getPosition(), direction);
					if (caseChecked!=null) controlled.add(caseChecked);
				}
				return controlled;
				
			// Gestion des 8 cases du cavalier
			case Knight:
				Delta deltas[] = { new Delta(-1, 2), new Delta(1, 2), new Delta(-2, 1), new Delta(2, 1),
						 new Delta(-2, -1), new Delta(2, -1), new Delta(-1, -2), new Delta(1, -2) };
				for (Delta delta: deltas) {
					caseChecked = board.getCaseDelta(piece.getPosition(), delta);
					if (caseChecked!=null) controlled.add(caseChecked);
				}
				return controlled;
				
			// Gestion des 2 cases de capture du pion
			case Pawn:
				int deltaLine = (piece.player.color==application.model.Player.Color.White)?1:-1;
				caseChecked = board.getCaseDelta(piece.getPosition(), new Delta(-1, deltaLine));
				if (caseChecked!=null) controlled.add(caseChecked);
				caseChecked = board.getCaseDelta(piece.getPosition(), new Delta(1, deltaLine));
				if (caseChecked!=null) controlled.add(caseChecked);
				return controlled;
				
			// Pour ces 3 cas on � le m�me algo avec les directions en param�tre
			case Queen:
				for (Direction direction: Direction.values()) {
					directions.add(direction);
				}
				break;
			case Bishop:
				directions.add(Direction.TopLeft);
				directions.add(Direction.TopRight);
				directions.add(Direction.BottomLeft);
				directions.add(Direction.BottomRight);
				break;
			case Rook:
				directions.add(Direction.Top);
				directions.add(Direction.Left);
				directions.add(Direction.Right);
				directions.add(Direction.Bottom);
				break;
		}
		// constitution de la liste des cases possibles
		for (Direction direction: directions) {
			caseChecked = board.getCaseContigue(piece.getPosition(), direction);
			// on boucle sur les cases vides dans cette direction
			while (caseChecked != null) {
				controlled.add(caseChecked);
				if (caseChecked.getContent() != null) break;
				caseChecked = board.getCaseContigue(caseChecked, direction);
			}
		}
		return controlled;
	}
	
	/**
	 * renvoie la liste des cases controll�es par les pi�ces d'un joueur
	 * @param playerChecked
	 * @return
	 */
	public HashSet<Case> getControlled(Player playerChecked) {
		HashSet<Case> set = new HashSet<Case>();
		for (Piece piece: playerChecked.pieces) {
			for (Case controlled: getControlled(piece)) {
				set.add(controlled);
			}
		}
		return set;
	}
	
	/**
	 * renvoie le joueur actif
	 * @return
	 */
	public Player getActivePlayer() {
		return player;
	}
	
	/**
	 * renvoie la case du plateau correspondant � la coordonn�e pass�e en param�tre
	 * @param coord
	 * @return
	 */
	public Case getCase(Coord coord) {
		return board.cases[coord.column][coord.line];
	}
	
	/**
	 * Sauvegarde l'historique de la partie
	 * @param oos
	 * @throws IOException
	 */
	public void save(ObjectOutputStream oos) throws IOException {
		oos.writeObject(history);
	}
	
	/**
	 * Charge l'historique de la partie
	 * @param ois
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	public void load(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		newGame();
		history = (History) ois.readObject();
		while (history.canUndo()) history.previous();
		Application.getApp().mainWindow.panelInfo.textArea.setText("");
		moveLast();
	}
	
	/**
	 * joue un coup jou� sur l'ordinateur distant
	 * @param move
	 */
	public void playDistant(HistoryMove move) {
		history.setNextMove(move);
		redo();
	}
}
