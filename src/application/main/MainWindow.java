package application.main;

import java.util.ResourceBundle;

import javax.swing.JFrame;
import java.awt.BorderLayout;

import application.clientServer.Client;
import application.clientServer.Server;
import application.main.Application.ServerMode;
import application.view.Board;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;

public class MainWindow extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static final ResourceBundle forms = ResourceBundle.getBundle("resources.forms"); //$NON-NLS-1$
	private final JMenuItem mntmNewGame = new JMenuItem(forms.getString("MainWindow.mntmNewGame.text")); //$NON-NLS-1$
	private final JMenuItem mntmLoad = new JMenuItem(forms.getString("MainWindow.mntmLoad.text")); //$NON-NLS-1$
	private final JMenuItem mntmSave = new JMenuItem(forms.getString("MainWindow.mntmSave.text")); //$NON-NLS-1$
	private final JMenuItem mntmSwitchSide = new JMenuItem(forms.getString("MainWindow.mntmSwitchSide.text")); //$NON-NLS-1$
	private final JMenuItem mntmQuit = new JMenuItem(forms.getString("MainWindow.mntmQuit.text")); //$NON-NLS-1$

	private final Board panelBoard = new Board(Application.getApp().engine.board);
	public final PanelInfo panelInfo = new PanelInfo();
	private final JMenu mnConnection = new JMenu(forms.getString("MainWindow.mnConnection.text")); //$NON-NLS-1$
	private final JMenuItem mntmServer = new JMenuItem(forms.getString("MainWindow.mntmServer.text")); //$NON-NLS-1$
	private final JMenuItem mntmClient = new JMenuItem(forms.getString("MainWindow.mntmClient.text")); //$NON-NLS-1$
		
	public MainWindow() {
		this.setTitle(forms.getString("MainWindow.title")); //$NON-NLS-1$
		this.setSize(600, 450);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));		
		getContentPane().add(panelBoard, BorderLayout.CENTER);
		getContentPane().add(panelInfo, BorderLayout.EAST);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnGame = new JMenu(forms.getString("MainWindow.mnGame.text"));
		menuBar.add(mnGame);
		
		mntmNewGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mntmNewGame.addActionListener(this);
		mnGame.add(mntmNewGame);
		mnGame.add(new JSeparator());
		
		mntmLoad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
		mntmLoad.addActionListener(this);	
		mnGame.add(mntmLoad);
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mntmSave.addActionListener(this);
		mnGame.add(mntmSave);
		mnGame.add(new JSeparator());
		
		mnGame.add(mntmSwitchSide);
		mntmSwitchSide.addActionListener(this);
		mnGame.add(new JSeparator());
		
		mntmQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		mntmQuit.addActionListener(this);		
		mnGame.add(mntmQuit);
		
		menuBar.add(mnConnection);
		mntmServer.addActionListener(this);		
		mnConnection.add(mntmServer);
		mntmClient.addActionListener(this);		
		mnConnection.add(mntmClient);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource()==mntmQuit) {
			System.exit(0);
		} else if (event.getSource()==mntmNewGame) {
			Application.getApp().engine.newGame();
			panelInfo.textArea.setText("");
			switch (Application.getApp().serverMode) {
				case StandAlone:
					break;
				case Server:
					Application.getApp().server.newGame();
					break;
				case Client:
					Application.getApp().client.newGame();
					break;
			}
		} else if (event.getSource()==mntmLoad) {
			// On charge l'historique sous forme s�rialis�e � partir du fichier "game.cpg"
			try (FileInputStream file = new FileInputStream("game.cpg")) {
				try (ObjectInputStream ois = new ObjectInputStream(file)) {
					Application.getApp().engine.load(ois);
					ois.close();
				} catch (ClassNotFoundException e) {}
			} catch (IOException e) {}
		} else if (event.getSource()==mntmSave) {
			// On sauvegarde l'historique sous forme s�rialis�e dans un fichier "game.cpg"
			try (FileOutputStream file = new FileOutputStream("game.cpg")) {
				try (ObjectOutputStream oos = new ObjectOutputStream(file)) {
					Application.getApp().engine.save(oos);
					oos.flush();
					oos.close();
				}
			} catch (IOException e) {}
		} else if (event.getSource()==mntmSwitchSide) {
			panelBoard.switchSide();
		} else if (event.getSource()==mntmServer) {
			Application.getApp().serverMode = ServerMode.Server;
			Application.getApp().server = new Server();
			Application.getApp().server.start();
		} else if (event.getSource()==mntmClient) {
			String address = (String) JOptionPane.showInputDialog(this, "Server IP address: ",
                    "Enter server IP address", JOptionPane.PLAIN_MESSAGE,
                    null, null, null);
			try {
				Application.getApp().serverMode = ServerMode.Client;
				Application.getApp().client = new Client(InetAddress.getByName(address));
				Application.getApp().client.start();
			} catch (UnknownHostException e) {}
		} 
	}	
}
