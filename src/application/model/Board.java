package application.model;

public class Board {
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
	
	public void setInitialPosition() {
		// On commence par virer les pieces de chaque joueur
		Player white = Player.getPlayer(Player.Color.White);
		while (white.getPieces().size() > 0) {
			white.getPieces().get(0).remove();
		}
		Player black = Player.getPlayer(Player.Color.Black);
		while (black.getPieces().size() > 0) {
			black.getPieces().get(0).remove();
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
}
