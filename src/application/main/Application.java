package application.main;

import application.clientServer.Client;
import application.clientServer.Server;
import application.controller.ChessEngine;

public class Application {
	public enum ServerMode { StandAlone, Server, Client; }
	private static Application app;
	
	public final double version = 0.1;
	public final ChessEngine engine;
	public final MainWindow mainWindow;
	public ServerMode serverMode;
	public Server server = null;
	public Client client = null;
	
	Application() {
		app = this;
		engine = new ChessEngine();
		mainWindow = new MainWindow();
		engine.newGame();
		serverMode = ServerMode.StandAlone;
	}
	
	/**
	 * fonction principale du programme
	 * @param args
	 */
	public static void main(String[] args) {
		new Application();
	}
	
	public static Application getApp() {
		return app;
	}
}
