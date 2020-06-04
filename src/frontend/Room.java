package frontend;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.jtattoo.plaf.mint.MintLookAndFeel;


public class Room extends JFrame {
	public boolean backGame = false;
	private Home home;
	private String mode;
	private ChessTable chessPanel;
	JPanel recordPanel = new JPanel();
	private int player1Win = 0;
	private int player1Lose = 0;
	private int roundCnt = 0;

	public Room(Home home, String mode) {
		this.home = home;
		this.mode = mode;
		init(mode);
	}

	public void updateRecord() {
		recordPanel.removeAll();
		recordPanel.repaint();
		recordPanel.setBounds(50, 25, 800, 200);
		recordPanel.setLayout(null);
		recordPanel.setOpaque(false);

		setLookAndFeel(30, 1);
		JLabel name1 = new JLabel(this.home.name1);
		JLabel name2 = new JLabel(this.home.name2);
		name1.setBounds(125, 22, 100, 30);
		recordPanel.add(name1);
		name2.setBounds(500, 22, 100, 30);
		recordPanel.add(name2);
		JLabel win1 = new JLabel(String.valueOf(this.player1Win));
		JLabel lose1 = new JLabel(String.valueOf(this.player1Lose));
		JLabel win2 = new JLabel(String.valueOf(this.player1Lose));
		JLabel lose2 = new JLabel(String.valueOf(this.player1Win));
		JLabel pj1 = new JLabel(String.valueOf(this.roundCnt - this.player1Lose - this.player1Win));
		JLabel pj2 = new JLabel(String.valueOf(this.roundCnt - this.player1Lose - this.player1Win));
		win1.setBounds(130, 72, 25, 30);
		lose1.setBounds(190, 72, 25, 30);
		pj1.setBounds(250, 72, 25, 30);
		win2.setBounds(505, 72, 25, 30);
		lose2.setBounds(565, 72, 25, 30);		
		pj2.setBounds(625, 72, 25, 30);
		recordPanel.add(win1);
		recordPanel.add(win2);
		recordPanel.add(lose1);
		recordPanel.add(lose2);
		recordPanel.add(pj1);
		recordPanel.add(pj2);
		recordPanel.revalidate();
	}
	
	public void init(String mode) {
		this.setIconImage(new ImageIcon("images/logo.png").getImage());

		this.setTitle("五子棋 - FIVE CHESS - 对战房间");
		this.setSize(1400, 825);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		setVisible(true);
		getContentPane().setLayout(null);

		chessPanel = new ChessTable(this, mode);

		JPanel panel = new JPanel() {
			protected void paintComponent(Graphics g) {
				Image image = new ImageIcon("images/room.png").getImage();
				g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
			}
		};
		panel.setBounds(0, 0, 1400, 825);
		getContentPane().add(panel);
		panel.setLayout(null);
		panel.setOpaque(false);

		chessPanel.setBounds(100, 165, 565, 565);
		panel.add(chessPanel);

		updateRecord();
		panel.add(recordPanel);

		setLookAndFeel(15, 0);
		JPanel bttnPanel = new JPanel();
		bttnPanel.setBounds(100, 725, 515, 50);
		panel.add(bttnPanel);
		bttnPanel.setLayout(null);
		bttnPanel.setOpaque(false);

		JButton refresh = new JButton("重新开始");
		refresh.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				chessPanel.getCore().newGame();
				repaint();
				backGame = false;
			}
		});
		refresh.setBounds(115, 10, 100, 26);
		bttnPanel.add(refresh);
		refresh.setFocusPainted(false);
	
		JButton regret = new JButton("悔棋");
		regret.setBounds(235, 10, 90, 26);
		regret.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setLookAndFeel(15, 0);
				if (backGame == true) {
					String[] options = {"确认", "取消"};
					int res = JOptionPane.showOptionDialog(null, "确认要悔棋吗？", "三思而后行",
														   JOptionPane.DEFAULT_OPTION, JOptionPane.YES_NO_OPTION,
														   new ImageIcon("images/back.png"), 
														   options, options[0]);
					if (res == 0) {
						chessPanel.unpaintItem();
					}
				}
			}
		});
		bttnPanel.add(regret);
		
		JButton theExit = new JButton("退出");
		theExit.setBounds(345, 10, 90, 26);
		theExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				chessPanel.getCore().newGame();
				home.setName(0, "");
				home.setName(1, "");
				toRoomList();
			}
		});		
		bttnPanel.add(theExit);
	}

	public void toRoomList() {
		home.setVisible(true);
		this.dispose();
	}

	private void resetGame() {
		chessPanel.getCore().newGame();
		updateRecord();
		repaint();
		chessPanel.steps = 0;
		backGame = false;		
	}

	public void win(int steps) {
		if (steps % 2 == 1)
			this.player1Win += 1;
		else
			this.player1Lose += 1;
		this.roundCnt += 1;
		setLookAndFeel(15, 0);
		System.out.println(mode);
		if (mode == "人机") {
			JOptionPane.showMessageDialog(this, "恭喜 :)", "你赢了！", 
				JOptionPane.ERROR_MESSAGE, new ImageIcon("images/result.png"));
		} else {
			if (steps % 2 == 0) {
				JOptionPane.showMessageDialog(this, "胜败乃兵家常事", "白方赢了，黑方输了！", 
					JOptionPane.ERROR_MESSAGE, new ImageIcon("images/result.png"));
			} else {
				JOptionPane.showMessageDialog(this, "胜败乃兵家常事", "黑方赢了，白方输了！", 
					JOptionPane.ERROR_MESSAGE, new ImageIcon("images/result.png"));
			}
		}		
		resetGame();
	}

	public void deafeat(int steps) {
		this.player1Lose += 1;
		this.roundCnt += 1;
		setLookAndFeel(15, 0);
		JOptionPane.showMessageDialog(this, "还需努力 :(", "你输了！", 
			JOptionPane.ERROR_MESSAGE, new ImageIcon("images/result.png"));
		resetGame();
	}

	public void pingju(int steps) {
		this.roundCnt += 1;
		setLookAndFeel(15, 0);
		JOptionPane.showMessageDialog(this, "势均力敌 :|", "平局！", 
			JOptionPane.ERROR_MESSAGE, new ImageIcon("images/result.png"));
		resetGame();
	}

	private static void setLookAndFeel(int fontSize, int isBold) {
		try {
			UIManager.setLookAndFeel(new MintLookAndFeel());
			UIManager.put("Button.font", new java.awt.Font("宋体", 0, 14));
			UIManager.put("Label.font", new java.awt.Font("宋体", isBold, fontSize));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}