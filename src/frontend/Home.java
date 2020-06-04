package frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.jtattoo.plaf.mint.MintLookAndFeel;


public class Home extends JFrame {
	private Home home = this;
	public String name1 = "";
	public String name2 = "";
	private int status = 0;
	private JButton pairButton = new JButton("双人对战");
	private JButton robotButton = new JButton("人机对战");

	private JPanel contentPane = new JPanel() {
		protected void paintComponent(Graphics g) {
			Image image = new ImageIcon("images/home.png").getImage();
			g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		}
	};

	public Home() {
		try {
			UIManager.setLookAndFeel(new MintLookAndFeel());
		} catch (Exception e) {
			e.printStackTrace();
		}
		init();
	}

	public void setName(int type, String name) {
		name = name.trim();
		if (type == 0) {
			this.name1 = name;
		} else {
			this.name2 = name;
		}
	}

	public void setStatus(int stts) {
		this.status = stts;
	}

	private void init() {
		this.setTitle("五子棋 - FIVE CHESS");
		this.setSize(1400, 825);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setIconImage(new ImageIcon("images/logo.png").getImage());
		contentPane.setLayout(null);

		pairButton.setBounds((int)(this.getWidth() * 0.2), (int)(this.getHeight() * 0.5), this.getWidth() / 6, this.getHeight() / 13);
		pairButton.setFocusPainted(false);
		robotButton.setBounds((int)(this.getWidth() * 0.2), (int)(this.getHeight() * 0.6), this.getWidth() / 6, this.getHeight() / 13);
		
		addAction();
		pairButton.setFont(new java.awt.Font("宋体", 1, 30));
		robotButton.setFont(new java.awt.Font("宋体", 1, 30));
		contentPane.add(pairButton);
		contentPane.add(robotButton);

		this.add(contentPane);
		this.setVisible(true);
	}

	private void addAction() {
		pairButton.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			new InputName(home, "双人", 0);
			while (name1.length() * name2.length() == 0 && status == 1) {
				new InputName(home, "双人", 1);
			}
			if (name1.length() * name2.length() != 0) {
				toRoom("双人");
			} else {
				setVisible(true);
			}    
		}
		});

		robotButton.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			new InputName(home, "人机", 0);
			while (name1.length() == 0 && status == 1) {
				new InputName(home, "人机", 1);
			}
			if (name1.length() != 0) {
				setName(1, "机器人");
				toRoom("人机");
			} else {
				setVisible(true);
			}
		}
		});

		addWindowListener(new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent e) {
			System.out.println("EXIT");
		}
		});
	}

	public void toRoom(String mode) {
		new Room(this, mode);
		this.setVisible(false);
	}
}
