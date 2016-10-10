package application.view;

import java.awt.Color;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Dimension;

public class Case extends JPanel {
	private static final long serialVersionUID = 1L;
	
	protected PieceHolder pieceHolder; 
	
	public Case(application.model.Case model) {
		super();
		setSize(new Dimension(30, 30));
		setPreferredSize(new Dimension(30, 30));
		setBackground((model.color==application.model.Case.Color.White)?Color.white:Color.lightGray);
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
		
		pieceHolder = new PieceHolder(model);
		panelContenu.add(pieceHolder, BorderLayout.CENTER);		
	}
}
