package application.main;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextArea;

public class PanelInfo extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public final JTextArea textArea = new JTextArea();
	public final PanelHistorique panelHistorique = new PanelHistorique();
	
	public PanelInfo() {
		setLayout(new BorderLayout(0, 0));
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setTabSize(3);
		textArea.setEditable(false);
		add(textArea, BorderLayout.CENTER);
		
		add(panelHistorique, BorderLayout.SOUTH);
	}
}

