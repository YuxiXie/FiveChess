package frontend;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import backend.Core;


public class ChessTable extends JPanel {

	private Executor pool = Executors.newFixedThreadPool(2);
	private RobotThread robotThread = new RobotThread(this, core);
	private HumanThread humanThread = new HumanThread(this, core);
	private HumanThread humanThread2 = new HumanThread(this, core);
	private ChessTable chessTable = this;
	private boolean lock = false;
	private int humanX;
	private int humanY;
	public String mode;
	private Room room;
	int gridWidth = 36;
	int adjust = -6;

	public static final int chess_BLACK = 2;
	public static final int chess_WHITE = 1;
	public static final int chess_EMPTY = 0;
	public static boolean isReady = false;
	public int steps = 0;
	private int[][] mark = new int[15][15];
	public static Core core = new Core();

	public static Core getCore() {
		return core;
	}

	public void setMark(int x,int y) {
		mark[x][y]=1;
	}

	public static final int BOARD_WIDTH = 515;
	private int[] draw = new int[15];

	public static int[][] map = new int[15][15];

	public ChessTable(Room room, String mode) {
		super(null);
		this.room = room;
		this.mode = mode;
		core.newGame();

		this.setBounds(0, 0, BOARD_WIDTH, BOARD_WIDTH);
		if (mode == "人机") {
			pool.execute(humanThread);
			pool.execute(robotThread);
		} else {
			pool.execute(humanThread2);
		}
	}

	public synchronized void robotChess() {
		synchronized (chessTable) {
			while (true) {
				if (!lock) {
					try {
						wait();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					try {
						Thread.sleep(700);
					} catch (Exception e) {
						e.printStackTrace();
				}
				int[] XY = core.computer(2);
				core.add(XY[0], XY[1], 2);
				mark[XY[0]][XY[1]] = 2;
				repaint();
				steps += 1;
				lock = false;
				if(steps == 225)
					room.pingju(steps);
				else if(core.check(XY[0], XY[1], 2 - steps % 2)){
					room.deafeat(steps);
				} else
					chessTable.notifyAll();
				}
			}
		}
	}

	public class MouseHandler extends MouseAdapter {
		public void mousePressed(MouseEvent event) {

			synchronized (chessTable) {
				int x = event.getX();
				int y = event.getY();

				if (x > 20 && x < 535 && y > 20 && y < 535) {
					humanX = (x - 21) / gridWidth;
					humanY = (y - 21) / gridWidth;
					if (mode == "人机") {
						if (paintItem(humanX, humanY, steps % 2 + 1)) {
							room.backGame = true;
							steps += 1;
							mark[humanX][humanY] = 1;
							lock = true;
							repaint();
							if(steps == 225) {
								room.pingju(steps);
							}
							else if(core.check(humanX, humanY, 2 - steps % 2)){
								room.win(steps);
							} else {
								chessTable.notifyAll();
							}	
						}
					} else {
						if (paintItem(humanX, humanY, steps % 2 + 1)) {
							room.backGame = true;
							steps += 1;
							mark[humanX][humanY] = 1;
							lock = true;
							repaint();
							if (room.getRoomStatus() == 2 || steps != 3 || !room.checkRestart()) {
								if (steps == 225) {
									room.pingju(steps);
								} else if(core.check(humanX, humanY, 2 - steps % 2)){
									room.win(steps);
								} else {
									chessTable.notifyAll();
								}
							}
						}
					}
				}
			}
		}
	}

	boolean paintItem(int i, int j, int type) {
		if (i < 15 && j < 15) {
			if (mode == "人机") {
				if (!core.add(i, j, type)) {
					return false;
				}
				return true;
			} else {
				if (!core.add(i, j, type)) {
					return false;
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(new ImageIcon("images/pan.png").getImage(), 0, 0, 565, 565, this);
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.black);
		g.setFont(new Font("宋体", Font.BOLD, 12));

		for (int i = 0, WIDTH = gridWidth + adjust; i < draw.length; i++, WIDTH += gridWidth) {
			draw[i] = WIDTH;
		}

		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				if (core.chessboard[i][j] != 0) {
					int m, n;
					m = i * gridWidth + (gridWidth + adjust);
					n = j * gridWidth + (gridWidth + adjust);
					Ellipse2D ellipse = new Ellipse2D.Double();
					Ellipse2D ellipse2D = new Ellipse2D.Double();
					ellipse.setFrameFromCenter(m, n, m + 14, n + 14);
					ellipse2D.setFrameFromCenter(m + 1, n - 1, m + 15, n + 13);
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 0.5f));
					
					GradientPaint gp1 = new GradientPaint(
						(float) ellipse.getMinX(),
						(float) ellipse.getMinY(), Color.white,
						(float) ellipse.getMaxX(),
						(float) ellipse.getMaxY(), Color.gray);
					
					GradientPaint gp2 = new GradientPaint(
						(float) ellipse.getMinX() - 1,
						(float) ellipse.getMinY() - 1, Color.white,
						(float) ellipse.getCenterX() - 1,
						(float) ellipse.getCenterY() - 1, Color.black);
					
					if (core.chessboard[i][j] == 1) {
						g2.setPaint(gp2);
					} else if (core.chessboard[i][j] == 2) {
						g2.setPaint(gp1);
					}
					
					g2.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 1));
					g2.fill(ellipse2D);
					if (mark[i][j] != 0) {
						g2.setColor(Color.red);
						g2.drawLine(m - 3, n, m + 3, n);
						g2.drawLine(m, n - 3, m, n + 3);
						mark[i][j] = 0;
					}
				}
			}
		}
	}

	public void unpaintItem() {
		core.undo(2 - steps % 2, mode == "人机");
		if (mode == "人机")
			steps -= 2;
		else
			steps -= 1;
	}
}