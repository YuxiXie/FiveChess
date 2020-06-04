package frontend;

import backend.Core;

public class HumanThread implements Runnable {
	private Core chess;
	private ChessTable chessTable;

	public HumanThread(ChessTable chessTable, Core chess) {
		this.chessTable = chessTable;
		this.chess = chess;
	}

	public void run() {
		chessTable.addMouseListener(chessTable.new MouseHandler());
	}
}
