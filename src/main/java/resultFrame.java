import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.LayoutStyle.ComponentPlacement;

public class resultFrame extends JFrame {

	private JFrame frame;

	/**
	 * Create the application.
	 * 
	 * @param mainFrame
	 * @param players
	 * @param user
	 */
	public resultFrame(String result, Player user, ArrayList<Player> players, MainFrame mainFrame,
			PrintWriter logWriter, int theme, JLabel avatar, boolean limit, ImageIcon c1Icon, ImageIcon c2Icon, ImageIcon c3Icon,
			ImageIcon c4Icon, ImageIcon c5Icon) {
		initialize(result, user, players, mainFrame, logWriter, theme, avatar, limit, c1Icon, c2Icon, c3Icon, c4Icon, c5Icon);
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @param mainFrame
	 * @param players
	 * @param user
	 */
	private void initialize(String result, Player user, ArrayList<Player> players, MainFrame mainFrame,
			PrintWriter logWriter, int theme, JLabel avatar, boolean limit, ImageIcon c1Icon, ImageIcon c2Icon, ImageIcon c3Icon,
			ImageIcon c4Icon, ImageIcon c5Icon) {
		frame = new JFrame();
		JPanel btnPnl = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel cards = new JPanel(new FlowLayout(FlowLayout.CENTER));
		if (theme == 1) {
			frame.getContentPane().setBackground(new Color(31, 114, 205));
			cards.setBackground(new Color(31, 114, 205));
		}
		else {
			frame.getContentPane().setBackground(new Color(0, 128, 0));
			cards.setBackground(new Color(0, 128, 0));
		}

		JLabel label = new JLabel(result);
		label.setFont(new Font("Gill Sans MT Ext Condensed Bold", Font.BOLD, 17));
		label.setForeground(new Color(255, 255, 255));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel winningHand = new JLabel("Winning Hand	");
		winningHand.setFont(new Font("Gill Sans MT Ext Condensed Bold", Font.BOLD, 17));
		winningHand.setForeground(new Color(255, 255, 255));
		
		JLabel c1 = new JLabel();
		c1.setIcon(c1Icon);
		JLabel c2 = new JLabel();
		c2.setIcon(c2Icon);
		JLabel c3 = new JLabel();
		c3.setIcon(c3Icon);
		JLabel c4 = new JLabel();
		c4.setIcon(c4Icon);
		JLabel c5 = new JLabel();
		c5.setIcon(c5Icon);
		cards.add(winningHand);
		cards.add(c1);
		cards.add(c2);
		cards.add(c3);
		cards.add(c4);
		cards.add(c5);

		JLabel bustedOrWin = new JLabel();
		JButton quitButton = new JButton("Quit");
		JButton restart = new JButton("Restart");
		JButton nextHandButton = new JButton("Next Hand");

		btnPnl.add(restart);
		btnPnl.add(quitButton);
		btnPnl.add(nextHandButton);

		if (user.getStack() <= 0) {
			nextHandButton.setVisible(false);
			bustedOrWin.setText("You busted out!");
			btnPnl.add(bustedOrWin);
			btnPnl.revalidate();
		}

		btnPnl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		if (theme == 1)
			mainPanel.setBackground(new Color(31, 114, 205));
		else
			mainPanel.setBackground(new Color(0, 128, 0));
		mainPanel.add(cards, BorderLayout.NORTH);
		mainPanel.add(label, BorderLayout.CENTER);
		mainPanel.add(btnPnl, BorderLayout.SOUTH);

		quitButton.setBackground(new Color(50, 205, 50));
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		restart.setBackground(new Color(50, 205, 50));
		restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.setVisible(false);
				mainFrame.dispose();
				frame.setVisible(false);
				MainMenu game = new MainMenu();
				game.setVisible(true);
			}
		});

		this.addWindowListener(new java.awt.event.WindowAdapter() {
	    	@Override
	    	public void windowClosing(java.awt.event.WindowEvent windowEvent) {
	        	String endTime = new SimpleDateFormat("dd MMMM yyyy  -  HH : mm")
					.format(Calendar.getInstance().getTime());
					logWriter.println("\n- Game ends " + endTime);
					logWriter.close();
	            System.exit(0);
	    	}
		});
		
		frame.add(mainPanel);
		frame.setBounds(100, 100, 1100, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		nextHandButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int handNumber = mainFrame.getHandNumber();
				int dealerID = mainFrame.getDealerID();
				mainFrame.setDealerID(dealerID);
				mainFrame.setVisible(false);
				mainFrame.setHandNumber(handNumber + 1);
				// if no more AI players in the list,
				// close the logWriter
				if (mainFrame.isPlayersEmpty())
					logWriter.close();
				// System.out.println(mainFrame.getHandNumber());
				mainFrame.dispose();
				// System.out.println(mainFrame.getHandNumber());
				try {
					new MainFrame(user, players, frame, theme, avatar, limit);
				} catch (InterruptedException | IOException e1) {
				}
			}
		});

		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String endTime = new SimpleDateFormat("dd MMMM yyyy  -  HH : mm")
				.format(Calendar.getInstance().getTime());
				logWriter.println("\n- Game ends " + endTime);
				logWriter.close();
				System.exit(0);
			}
		});
	}

}
