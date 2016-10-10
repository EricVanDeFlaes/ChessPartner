package application.main;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.FlowLayout;

public class PanelHistorique extends JPanel {
	private static final long serialVersionUID = 1L;

	public PanelHistorique() {
		
		JButton btnBegin = new JButton("<<");
		btnBegin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Application.getApp().engine.moveFirst();
			}
		});
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		add(btnBegin);
		
		JButton btnUndo = new JButton("<");
		btnUndo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Application.getApp().engine.undo();
			}
		});
		add(btnUndo);
		
		JButton btnRedo = new JButton(">");
		btnRedo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Application.getApp().engine.redo();
			}
		});
		add(btnRedo);
		
		JButton btnEnd = new JButton(">>");
		btnEnd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Application.getApp().engine.moveLast();
			}
		});
		add(btnEnd);
	}
	
}
