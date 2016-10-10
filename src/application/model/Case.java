package application.model;

import java.io.Serializable;
import java.util.HashSet;

public class Case {
	public enum Color {
		White, Black;
		
		public Color next() {
			return values()[(ordinal()+1)%2];
		}
	}
	
	public static class Coord implements Serializable {
		private static final long serialVersionUID = 1L;
		public final int column, line;
		
		public Coord(int column, int line) {
			this.column = column;
			this.line = line;
		}
		
		/**
		 * renvoie le nom de la colonne [A-H]
		 * @return
		 */
		public String getColumnName() {
			return String.valueOf((char)('a' + column));
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
	
	public final Color color;
	public final Coord coord;
	private Piece content;
	private HashSet<ICaseListener> listeners;
	
	/**
	 * Constructeur de Case
	 * @param column
	 * @param ligne
	 * @param color
	 */
	public Case(int column, int line, Color color) {
		coord = new Coord(column, line);
		this.color = color;
		content = null;
		listeners = new  HashSet<ICaseListener>();
	}
	
	public void addListener(ICaseListener listener) {
		listeners.add(listener);
	}
		
	public void removeListener(ICaseListener listener) {
		listeners.remove(listener);
	}
		
	public Piece getContent() {
		return content;
	}
	
	public void setContent(Piece piece) {
		content = piece;
		for (ICaseListener listener: listeners) {
			listener.contentChanged(piece);
		}
	}
	
	@Override
	public String toString() {
		return coord.toString();
	}

}
