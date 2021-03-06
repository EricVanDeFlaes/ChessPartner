package application.view;

import javax.swing.JPanel;
import java.awt.GridLayout;

public class Board extends JPanel {
	private static final long serialVersionUID = 1L;
	private final Case cases[][];
	private application.model.Player.Color side;
	
	/**
	 * On construit le plateau par d�faut avec les blancs jouant en bas
	 * @param model
	 */
	public Board(application.model.Board board) {
		super();
		setLayout(new GridLayout(8, 8, 0, 0));
		cases = new Case[8][8];
		for (int column=0; column < 8; column++) {
			for (int line=0; line < 8; line++) {
				cases[column][line] = new Case(board.cases[column][line]);
			}
		}
		board.setInitialPosition();
		setSide(application.model.Player.Color.White);
	}
	
	/**
	 * On r�arrange les cases en fonction du joueur jouant en bas
	 * @param playerColor
	 */
	public void setSide(application.model.Player.Color playerColor) {
		side = playerColor;
		removeAll();
		for (int line=0; line < 8; line++) {
			for (int column=0; column < 8; column++) {
				Case viewCase = cases[column][(playerColor==application.model.Player.Color.White)?7-line:line];
				add(viewCase);
			}
		}
		validate();
	}
	
	/**
	 * Inverse le plateau
	 */
	public void switchSide() {
		setSide((side==application.model.Player.Color.White)?application.model.Player.Color.Black:application.model.Player.Color.White);
	}
}
