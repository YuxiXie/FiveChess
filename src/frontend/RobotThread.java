package frontend;

import backend.Core;


public class RobotThread implements Runnable {
	private Core chess;
	private ChessTable chessTable;
	public RobotThread(ChessTable chessTable, Core chess) {
		this.chessTable = chessTable;
		this.chess = chess;
	}

	public void run() {
		chessTable.robotChess();
	}

}
