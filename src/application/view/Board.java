package application.view;

import javax.swing.JPanel;
import java.awt.GridLayout;

public class Board extends JPanel {
	private static final long serialVersionUID = 1L;
	private final Case cases[][];
	private final application.model.Board model;
	
	/**
	 * On construit le plateau par défaut avec les blancs jouant en bas
	 * @param model
	 */
	public Board(application.model.Board model) {
		super();
		setLayout(new GridLayout(8, 8, 0, 0));
		this.model = model;
		cases = new Case[8][8];
		for (int column=0; column < 8; column++) {
			for (int line=0; line < 8; line++) {
				cases[column][line] = new Case(model.cases[column][line]);
			}
		}
		model.setInitialPosition();
		setSide(application.model.Player.Color.White);
	}
	
	/**
	 * On réarrange les cases en fonction du joueur jouant en bas
	 * @param playerColor
	 */
	public void setSide(application.model.Player.Color playerColor) {
		removeAll();
		for (int line=0; line < 8; line++) {
			for (int column=0; column < 8; column++) {
				Case viewCase = cases[column][(playerColor==application.model.Player.Color.White)?7-line:line];
				viewCase.refreshContent();
				add(viewCase);
			}
		}
	}
	
	public application.model.Board getModel() {
		return model;
	}
}
