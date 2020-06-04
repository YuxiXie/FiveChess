package frontend;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class InputName extends JDialog {
	private Home home;
	private InputName inputName = this;
	private JTextField nameTextField1 = new JTextField();
	private JTextField nameTextField2 = new JTextField();
	private JLabel nameTip = new JLabel("请输入玩家姓名");
	private JLabel blackTip = new JLabel("黑方：");
	private JLabel whiteTip = new JLabel("白方：");
	private JButton ok = new JButton("确定");
	private JButton cancel = new JButton("取消");

	public InputName(Home home, String mode, int tag) {
		this.setModal(true);
		this.home = home;
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		if (tag == 1) {
			this.nameTip = new JLabel("请正确输入玩家姓名（不可为空）");
		}

		if (mode == "人机") {
			this.setBounds((int)(home.getWidth() * 0.4), (int)(home.getHeight() * 0.4), 280, 150);
		} else {
			this.setBounds((int)(home.getWidth() * 0.4), (int)(home.getHeight() * 0.4), 320, 180);
		}    
		
		this.setLayout(null);

		nameTip.setBounds(30, 20, 300, 15);
		nameTip.setFont(new java.awt.Font("宋体", 0, 14));

		if (mode == "人机") {
			nameTextField1.setBounds(30, 40, 200, 30);
			nameTextField1.setFont(new java.awt.Font("宋体", 0, 14));
			ok.setBounds(30, 80, 90, 30);
			ok.setFont(new java.awt.Font("宋体", 0, 14));
			cancel.setBounds(140, 80, 90, 30);
			cancel.setFont(new java.awt.Font("宋体", 0, 14));
		} else {
			blackTip.setBounds(30, 40, 50, 30);
			blackTip.setFont(new java.awt.Font("宋体", 0, 14));
			whiteTip.setBounds(30, 80, 50, 30);
			whiteTip.setFont(new java.awt.Font("宋体", 0, 14));
			nameTextField1.setBounds(80, 40, 200, 30);
			nameTextField1.setFont(new java.awt.Font("宋体", 0, 14));
			nameTextField2.setBounds(80, 80, 200, 30);
			nameTextField2.setFont(new java.awt.Font("宋体", 0, 14));
			ok.setBounds(70, 120, 90, 30);
			ok.setFont(new java.awt.Font("宋体", 0, 14));
			cancel.setBounds(180, 120, 90, 30);
			cancel.setFont(new java.awt.Font("宋体", 0, 14));
			this.add(blackTip);
			this.add(whiteTip);
		}  

		addAction(mode);

		this.add(nameTip);
		this.add(ok);
		this.add(nameTextField1);
		if (mode == "双人") {
			this.add(nameTextField2);
		}
		this.add(cancel);
		this.setVisible(true);
	}
	
	private void addAction(String mode) {
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				home.setStatus(1);
				String name1 = nameTextField1.getText();
				home.setName(0, name1);
				if (mode == "双人") {
					String name2 = nameTextField2.getText();
					home.setName(1, name2);
				}
				inputName.dispose();
			}
		});
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				home.setStatus(0);
				home.setVisible(true);
				inputName.dispose();
			}
		});
	}
}