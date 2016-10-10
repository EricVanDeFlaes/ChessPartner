package application.model;

public class Board {
	public static class Delta {
		public final int column, line;
		
		public Delta(int column, int line) {
			this.column = column;
			this.line = line;
		}
	}
	
	public static enum Direction {
		TopLeft(-1, 1), Top(0, 1), TopRight(1, 1), Left(-1, 0), Right(1,0), BottomLeft(-1, -1), Bottom(0, -1), BottomRight(1, -1);
		
		public final Delta delta;
		
		Direction (int deltaColumn, int deltaLine) {
			delta = new Delta(deltaColumn, deltaLine);
		}
	}
	
	/**
	 * Le tableau est défini par [colonne][ligne] avec [0][0] représentant la case A1
	 */
	public final Case cases[][];
	
	public Board() {
		cases = new Case[8][8];
		Case.Color color = Case.Color.Black;
		for (int column=0 ; column < 8; column++) {
			for (int line=0; line < 8; line++) {
				cases[column][line] = new Case(column, line, color);
				color = color.next();
			}
			color = color.next();
		}
	}
	
	/**
	 * Prépare le plateau pour une nouvelle partie (replace toutes les pieces)
	 */
	public void setInitialPosition() {
		// On commence par virer les pieces de chaque joueur
		Player white = Player.getPlayer(Player.Color.White);
		while (white.pieces.size() > 0) {
			white.pieces.get(0).remove();
		}
		Player black = Player.getPlayer(Player.Color.Black);
		while (black.pieces.size() > 0) {
			black.pieces.get(0).remove();
		}
		
		// On charge les positions initiales
		Piece.TypePiece typePiece = Piece.TypePiece.Pawn;
		for (int column=0; column < 8; column++) {
			switch (column) {
				case 0:
				case 7:
					typePiece = Piece.TypePiece.Rook;
					break;
				case 1:
				case 6:
					typePiece = Piece.TypePiece.Knight;
					break;
				case 2:
				case 5:
					typePiece = Piece.TypePiece.Bishop;
					break;
				case 3:
					typePiece = Piece.TypePiece.Queen;
					break;
				case 4:
					typePiece = Piece.TypePiece.King;
					break;
			}
			new Piece(typePiece, white, cases[column][0]);
			new Piece(Piece.TypePiece.Pawn, white, cases[column][1]);
			new Piece(Piece.TypePiece.Pawn, black, cases[column][6]);
			new Piece(typePiece, black, cases[column][7]);
		}
	}
	
	/**
	 * renvoie la case situé à delta de la case origin
	 * @param origin
	 * @param delta
	 * @return null si le delta sort du plateau
	 */
	public Case getCaseDelta(Case origin, Delta delta) {
		int column = origin.coord.column + delta.column;
		int line = origin.coord.line + delta.line;
		if (column < 0 || column > 7 || line < 0 || line > 7) return null;
		return cases[column][line];
	}
	
	/**
	 * renvoie la case contigue à la case d'origine dans la direction donnée
	 * @param origin
	 * @param direction
	 * @return null s'il n'existe pas de case dans cette direction (bord du plateau)
	 */
	public Case getCaseContigue(Case origin, Direction direction) {
		return getCaseDelta(origin, direction.delta);
	}
}
