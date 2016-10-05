package application.main;

import java.util.ResourceBundle;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import application.view.Board;

public class MainWindow extends JFrame {
	private static final ResourceBundle forms = ResourceBundle.getBundle("resources.forms"); //$NON-NLS-1$
	private static final long serialVersionUID = 1L;
	
	public MainWindow() {
		this.setTitle(forms.getString("MainWindow.title")); //$NON-NLS-1$
		this.setSize(600, 450);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		Board panelBoard = new Board(Application.getApp().board);
		getContentPane().add(panelBoard, BorderLayout.CENTER);
		
		this.setVisible(true);
	}	
}
