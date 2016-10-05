package application.model;

public class Case {
	public enum Color {
		White, Black;
		
		public Color next() {
			return values()[(ordinal()+1)%2];
		}
	}
	
	public static class Coord {
		private final int column, line;
		
		public Coord(int column, int line) {
			this.column = column;
			this.line = line;
		}
		
		/**
		 * renvoie l'index de la colonne [0-7]
		 * @return
		 */
		public int getColumn() {
			return column;
		}
		
		/**
		 * renvoie le nom de la colonne [A-H]
		 * @return
		 */
		public String getColumnName() {
			return String.valueOf((char)('A' + column));
		}
		
		/**
		 * renvoie l'index de la ligne [0-7]
		 * @return
		 */
		public int getLine() {
			return line;
		}
		
		/**
		 * renvoie le nom de la ligne [1-8]
		 * @return
		 */
		public String getLineName() {
			return String.valueOf(line+1);
		}
		
		@Override
		public String toString() {
			return getColumnName() + getLineName();
		}
	}
	
	private final Color color;
	private final Coord coord;
	private Piece content;
	
	/**
	 * Constructeur de Case
	 * @param column
	 * @param ligne
	 */
	public Case(int column, int line, Color color) {
		coord = new Coord(column, line);
		this.color = color;
		content = null;
	}
	
	/**
	 * renvoie les coordonnées de la case
	 * @return les coordonnées
	 */
	public Coord getCoord() {
		return coord;
	}
	
	/**
	 * Renvoie la couleur de la case
	 * @return
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * renvoie le contenu de la case
	 * @return null si la case ne contient pas de pièce
	 */
	public Piece getContent() {
		return content;
	}
	
	/**
	 * définit le contenu d'une case
	 * @param piece null pour indiquer que la case ne contient plus de pièce
	 */
	public void setContent(Piece piece) {
		content = piece;
	}
	
	@Override
	public String toString() {
		return coord.toString();
	}

}
