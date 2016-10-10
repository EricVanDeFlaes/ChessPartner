package application.view;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;

import application.main.Application;
import application.model.ICaseListener;
import application.model.Piece;

public class PieceHolder extends JLabel implements ICaseListener {
	/**
	 * Classe permettant de filtrer les drag & drop possibles à partir/vers un pieceHolder
	 */
	public static class PieceTransferable implements Transferable {
		private static DataFlavor[] dataFlavors;
		static {
			try {
				dataFlavors = new DataFlavor[] { new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=" + PieceHolder.class.getName()) };
			} catch (ClassNotFoundException e) {
				dataFlavors = new DataFlavor[] {};
			}
		}
		
		protected PieceHolder source;
		public PieceTransferable(PieceHolder source) {
			this.source = source;
		}
		
		@Override
		public Object getTransferData(DataFlavor df) throws UnsupportedFlavorException, IOException {
			return source;
		}
		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return dataFlavors;
		}
		@Override
		public boolean isDataFlavorSupported(DataFlavor df) {
			return df == dataFlavors[0];
		}
	}
	
	public static class PieceTransferHandler extends TransferHandler {
		private static final long serialVersionUID = 1L;

		// Drag part
		public int getSourceActions(JComponent c) {
			return MOVE;
		}
		public Transferable createTransferable(JComponent source) {
			return new PieceTransferable((PieceHolder) source);
		}		
		public void exportDone(JComponent source, Transferable data, int action) {
		}

		// Drop part
		public boolean canImport(TransferHandler.TransferSupport info) {
			return info.isDataFlavorSupported(PieceTransferable.dataFlavors[0]);
		}		
		public boolean importData(TransferHandler.TransferSupport support) {
			if(!canImport(support)) return false;
			try {
				Transferable t = support.getTransferable();
				PieceHolder src = (PieceHolder) t.getTransferData(PieceTransferable.dataFlavors[0]);
				PieceHolder dst = (PieceHolder)support.getComponent();
				Application.getApp().engine.move(src.model.getContent(), dst.model);
			} catch (Exception e) {
				Toolkit.getDefaultToolkit().beep();			
			}
			return true;
		}	
	}
	
	private static final long serialVersionUID = 1L;
	private static final PieceTransferHandler pieceTransferHandler = new PieceTransferHandler();
	
	private final application.model.Case model;
	
	public PieceHolder(application.model.Case model) {
		super("");
		this.model = model;
		setHorizontalAlignment(SwingConstants.CENTER);
		
		// Gestion du drag & drop
	    setTransferHandler(pieceTransferHandler);
	    addMouseListener(new MouseAdapter() {
	    	public void mousePressed(MouseEvent evt) {
	    		getTransferHandler().exportAsDrag(PieceHolder.this, evt, TransferHandler.MOVE);
	    	}
	    });
	    
	    // Gestion des listeners
	    model.addListener(this);
	}
	
	public application.model.Case getCase() {
		return model;
	}
	
	@Override
	public void contentChanged(Piece piece) {
		if (piece == null) {
			setIcon(null);
		} else {
			String name = piece.type.toString()+piece.player.color.toString();
			ImageIcon image = new ImageIcon("C:/Developpement/workspace/ChessPartner/src/resources/icones/"+name+".png");
			setIcon(image);
		}
	}
}
