package application.main;

import application.controller.ChessEngine;

public class Application {
	private static Application app;
	
	public final double version = 0.1;
	public final ChessEngine engine;
	public final MainWindow mainWindow;
	
	Application() {
		app = this;
		engine = new ChessEngine();
		mainWindow = new MainWindow();
		engine.newGame();
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
