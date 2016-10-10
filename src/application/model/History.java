package application.model;

import java.io.Serializable;
import java.util.LinkedList;

public class History implements Serializable {
	private static final long serialVersionUID = 1L;
	private LinkedList<HistoryMove> list = new LinkedList<HistoryMove>();
	private int current=-1;

	public void add(HistoryMove historyMove) {
		list.add(++current, historyMove);
		while (list.size()-1 > current) list.removeLast();
	}
	
	public boolean canUndo() {
		return (current >= 0);
	}
	
	public boolean isLast() {
		return (current == list.size()-1);
	}
	
	public void previous() {
		if (canUndo()) current--;
	}
	
	public void next() {
		if (!isLast()) current++;
	}
	
	/**
	 * renvoie l'history correspondant au current (dernier coup joué en général)
	 * @return
	 */
	public HistoryMove get() {
		if (!canUndo()) return null;
		return list.get(current);
	}
	
	/**
	 * renvoie l'history correspondant au current (dernier coup joué en général)
	 * et déplace le current sur le coup précédent
	 * @return
	 */
	public HistoryMove undo() {
		if (!canUndo()) return null;
		return list.get(current--);
	}
	
	/**
	 * renvoie l'history correspondant au coup suivant
	 * et déplace le current sur le coup suivant
	 * @return
	 */
	public HistoryMove redo() {
		if (isLast()) return null;
		return list.get(++current);
	}
}
