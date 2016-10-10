package application.main;

import java.util.ResourceBundle;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import application.view.Board;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;

public class MainWindow extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static final ResourceBundle forms = ResourceBundle.getBundle("resources.forms"); //$NON-NLS-1$
	private final JMenuItem mntmLoad = new JMenuItem(forms.getString("MainWindow.mntmLoad.text")); //$NON-NLS-1$
	private final JMenuItem mntmSave = new JMenuItem(forms.getString("MainWindow.mntmSave.text")); //$NON-NLS-1$
	private final JMenuItem mntmQuit = new JMenuItem(forms.getString("MainWindow.mntmQuit.text")); //$NON-NLS-1$
	private final JMenuItem mntmNewGame = new JMenuItem(forms.getString("MainWindow.mntmNewGame.text")); //$NON-NLS-1$

	public final PanelInfo panelInfo = new PanelInfo();
		
	public MainWindow() {
		this.setTitle(forms.getString("MainWindow.title")); //$NON-NLS-1$
		this.setSize(600, 450);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));		
		Board panelBoard = new Board(Application.getApp().engine.board);
		getContentPane().add(panelBoard, BorderLayout.CENTER);
		getContentPane().add(panelInfo, BorderLayout.EAST);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu(forms.getString("MainWindow.mnFichier.text"));
		menuBar.add(mnFile);
		
		mntmNewGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mntmNewGame.addActionListener(this);
		mnFile.add(mntmNewGame);
		mnFile.add(new JSeparator());
		
		mntmLoad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
		mntmLoad.addActionListener(this);	
		mnFile.add(mntmLoad);
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mntmSave.addActionListener(this);
		mnFile.add(mntmSave);
		mnFile.add(new JSeparator());
		
		mntmQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		mntmQuit.addActionListener(this);
		mnFile.add(mntmQuit);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource()==mntmQuit) {
			System.exit(0);
		} else if (event.getSource()==mntmNewGame) {
			Application.getApp().engine.newGame();
			panelInfo.textArea.setText("");
		} else if (event.getSource()==mntmLoad) {
			// On charge l'historique sous forme sérialisée à partir du fichier "game.cpg"
			try (FileInputStream file = new FileInputStream("game.cpg")) {
				try (ObjectInputStream ois = new ObjectInputStream(file)) {
					Application.getApp().engine.load(ois);
					ois.close();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (event.getSource()==mntmSave) {
			// On sauvegarde l'historique sous forme sérialisée dans un fichier "game.cpg"
			try (FileOutputStream file = new FileOutputStream("game.cpg")) {
				try (ObjectOutputStream oos = new ObjectOutputStream(file)) {
					Application.getApp().engine.save(oos);
					oos.flush();
					oos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
}
