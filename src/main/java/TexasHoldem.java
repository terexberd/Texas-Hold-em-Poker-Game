import javax.swing.*;

public class TexasHoldem{
	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new MainMenu();
			}
		});
	}
}