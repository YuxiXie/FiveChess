package frontend;

import javax.swing.*;

import com.jtattoo.plaf.mint.MintLookAndFeel;

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(new MintLookAndFeel());
				} catch (Exception e) {
					e.printStackTrace();
				}
				new Home();
			}
		});
	}
}
