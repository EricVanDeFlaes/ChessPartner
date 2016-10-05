package application.view;

import java.awt.Color;

import javax.swing.JPanel;

import application.model.Piece;
import application.model.Player;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Case extends JPanel {
	private static final long serialVersionUID = 1L;
	private application.model.Case model=null;
	
	private JLabel lblPiece; 
	
	public Case(application.model.Case model) {
		super();
		setSize(new Dimension(30, 30));
		setPreferredSize(new Dimension(30, 30));
		setBackground((model.getColor()==application.model.Case.Color.White)?Color.white:Color.black);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{5, 20, 5, 0};
		gridBagLayout.rowHeights = new int[]{5, 20, 5, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 4.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 4.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JPanel panelContenu = new JPanel();
		GridBagConstraints gbc_panelContenu = new GridBagConstraints();
		gbc_panelContenu.fill = GridBagConstraints.BOTH;
		gbc_panelContenu.gridx = 1;
		gbc_panelContenu.gridy = 1;
		add(panelContenu, gbc_panelContenu);
		panelContenu.setOpaque(false);
		panelContenu.setLayout(new BorderLayout(0, 0));
		
		lblPiece = new JLabel("");
		lblPiece.setHorizontalAlignment(SwingConstants.CENTER);
		panelContenu.add(lblPiece, BorderLayout.CENTER);
		
		// on affecte le modèle à la fin pour éviter des soucis dans le repaint 
		this.model = model;
	}
	
	public application.model.Case getModel() {
		return model;
	}
	
	public void refreshContent() {
		Piece piece = model.getContent();
		if (piece == null) {
			lblPiece.setText("");
		} else {
			lblPiece.setText(piece.getType().toString());
			lblPiece.setForeground((piece.getPlayer()==Player.getPlayer(application.model.Player.Color.White))?Color.blue:Color.red);
		}
	}	
}
