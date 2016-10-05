package application.main;

import application.model.Board;

public class Application {
	private static Application app;
	
	public final double version = 0.1;	
	public final Board board;
	public final MainWindow mainWindow;
	
	Application() {
		app = this;
		board = new Board();
		mainWindow = new MainWindow();
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
