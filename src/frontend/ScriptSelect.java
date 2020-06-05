package frontend;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import com.jtattoo.plaf.mint.MintLookAndFeel;


public class ScriptSelect extends JFrame {
    private ScriptSelect scriptSelect = this;
    private Home home;
    private String mode; 
    
    public ScriptSelect(Home home, String mode) {
        this.home = home;
        this.mode = mode;
        try {
			UIManager.setLookAndFeel(new MintLookAndFeel());
		} catch (Exception e) {
			e.printStackTrace();
        }
        init();
    }

    public void init() {
        this.setIconImage(new ImageIcon("images/logo.png").getImage());

        this.setTitle("五子棋 - FIVE CHESS - 游戏规则");
        this.setSize(1215, 825);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        setVisible(true);	
        getContentPane().setLayout(null);

        JPanel panel = new JPanel() {
			protected void paintComponent(Graphics g) {
				Image image = new ImageIcon("images/script.png").getImage();
				g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
			}
        };
        panel.setBounds(0, 0, 1215, 825);
		getContentPane().add(panel);
		panel.setLayout(null);
		panel.setOpaque(false);
        
        JButton forbidButton = new JButton("有禁手模式");
        forbidButton.setBounds(490, 680, 190, 45);
        forbidButton.setFont(new java.awt.Font("宋体", 1, 22));
        forbidButton.addMouseListener(new MouseAdapter() {
            @Override
			public void mouseClicked(MouseEvent e) {
                home.setStatus(2);
                new Room(home, mode);
                scriptSelect.dispose();
            }
        });
        panel.add(forbidButton);
        forbidButton.setFocusPainted(false);

        JButton normalButton = new JButton("普通模式");
        normalButton.setBounds(710, 680, 190, 45);
        normalButton.setFont(new java.awt.Font("宋体", 1, 22));
        normalButton.addMouseListener(new MouseAdapter() {
            @Override
			public void mouseClicked(MouseEvent e) {
                home.setStatus(3);
                new Room(home, mode);
                scriptSelect.dispose();
            }
        });
        panel.add(normalButton);
    }
}	