package application.main;

import javax.swing.JPanel;

import application.model.History;

import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.FlowLayout;

public class PanelHistorique extends JPanel {
	private static final long serialVersionUID = 1L;
	private JButton btnBegin = new JButton("<<");
	private JButton btnUndo = new JButton("<");
	private JButton btnRedo = new JButton(">");
	private JButton btnEnd = new JButton(">>");

	public PanelHistorique() {		
		btnBegin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Application.getApp().engine.moveFirst();
			}
		});
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		add(btnBegin);
		
		btnUndo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Application.getApp().engine.undo();
			}
		});
		add(btnUndo);
		
		btnRedo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Application.getApp().engine.redo();
			}
		});
		add(btnRedo);
		
		btnEnd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Application.getApp().engine.moveLast();
			}
		});
		add(btnEnd);
	}
	
	/**
	 * Rafraichit l'état des boutons de navigation d'historique
	 */
	public void refresh(History history) {
		btnBegin.setEnabled(history.canUndo());
		btnUndo.setEnabled(history.canUndo());
		btnRedo.setEnabled(!history.isLast());
		btnEnd.setEnabled(!history.isLast());
	}	
}
