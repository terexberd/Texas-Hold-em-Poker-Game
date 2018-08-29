import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

public class TestTime extends JFrame{
	Timer t;
	static JLabel l;
	
	public static void main(String[] args) {
		new TestTime();
	}
	
	public TestTime() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 200);
		setLayout(new BorderLayout());
		
		JButton b;
		b = new JButton("Start Timer");
		b.addActionListener((new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (t != null) {
					t.cancel();
					t.purge();
				}
				t = new Timer();
				t.schedule(new tt(), 0, 1000);
			}
		}));
		add(b, BorderLayout.NORTH);
		
		l = new JLabel();
		add(l, BorderLayout.CENTER);
		setVisible(true);
	}

	class tt extends TimerTask {
		int timeLeft = 25;
		
		public void run() {
			l.setText(timeLeft + " s");
			l.revalidate();
			timeLeft--;
			if (timeLeft == -1) {
				l.setText("FOLD");
				cancel();
				//purge();
			}
		}
	}
}
