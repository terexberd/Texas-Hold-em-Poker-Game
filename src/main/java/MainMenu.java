import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JFormattedTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import java.awt.Choice;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

@SuppressWarnings({"rawtypes", "unchecked"})

public class MainMenu extends JFrame implements ActionListener {

	private JPanel contentPane;
	private String playerName = null;
	private int num;
	private JTextField nameField = new JTextField();
	private JTextField numberField = new JTextField();
	private JLabel startWarning = new JLabel("");// Give a base from which to write any error messages

	private JComboBox difficultyBox = new JComboBox();
	JLabel avatar;
	Player user;
	static int whichTheme = 0;
	boolean limit = false;

	/**
	 * Sets size of the frame
	 */
	public MainMenu() {
		super("Texas Holdem");
		initFrame();
		// set the size of the frame
		setSize(700, 450);
		// click X to exit
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Must be the last line of this constructor
		setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public void initFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 128, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JLabel MainTitle = new JLabel("Texas Hold'em");
		MainTitle.setForeground(new Color(255, 255, 255));
		MainTitle.setFont(new Font("Gill Sans MT Ext Condensed Bold", Font.PLAIN, 40));
		MainTitle.setHorizontalAlignment(SwingConstants.CENTER);

		nameField.setToolTipText("");
		nameField.setColumns(10);
		nameField.setText("");

		numberField.setToolTipText("");
		numberField.setColumns(10);
		numberField.setText("");

		JLabel nameLabel = new JLabel("Player Name:");
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		nameLabel.setForeground(new Color(255, 255, 255));
		nameLabel.setFont(new Font("Gill Sans MT Condensed", Font.PLAIN, 15));

		JLabel numLabel = new JLabel("# of Players:");
		numLabel.setHorizontalAlignment(SwingConstants.CENTER);
		numLabel.setForeground(new Color(255, 255, 255));
		numLabel.setFont(new Font("Gill Sans MT Condensed", Font.PLAIN, 15));

		JLabel theme = new JLabel("Themes:");
		theme.setHorizontalAlignment(SwingConstants.CENTER);
		theme.setForeground(new Color(255, 255, 255));
		theme.setFont(new Font("Gill Sans MT Condensed", Font.PLAIN, 15));

		JButton greenTheme = new JButton("Traditional (Default)");
		greenTheme.setForeground(new Color(43, 151, 0));
		greenTheme.setFont(new Font("Futura", Font.ITALIC, 14));
		greenTheme.addActionListener(this);
		
		JButton navyTheme = new JButton("Navy");
		navyTheme.setForeground(new Color(31, 114, 205));
		navyTheme.setFont(new Font("Futura", Font.ITALIC, 14));
		navyTheme.addActionListener(this);
		
		JLabel chosenTheme = new JLabel("Default");
		chosenTheme.setForeground(new Color(255, 255, 255));
		chosenTheme.setFont(new Font("Gill Sans MT Condensed", Font.PLAIN, 15));
		
		JCheckBox checkbox = new JCheckBox("Time Limit?");
		checkbox.setSelected(false);
		checkbox.setForeground(new Color(255, 255, 255));
		checkbox.setFont(new Font("Gill Sans MT Condensed", Font.PLAIN, 15));

		JButton btnStart = new JButton("Start");
		btnStart.setFont(new Font("Gill Sans MT Ext Condensed Bold", Font.PLAIN, 13));
		btnStart.addActionListener(this);

		JButton btnBrowse = new JButton("Load Avatar");
		btnBrowse.setFont(new Font("Gill Sans MT Ext Condensed Bold", Font.PLAIN, 13));
		btnBrowse.addActionListener(this);

		startWarning.setFont(new Font("Gill Sans MT Condensed", Font.PLAIN, 14));
		startWarning.setForeground(new Color(255, 255, 255));
		startWarning.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblUseTimer = new JLabel("Use Timer");
		lblUseTimer.setForeground(Color.WHITE);
		lblUseTimer.setFont(new Font("Gill Sans MT Ext Condensed", Font.PLAIN, 14));

		
		difficultyBox.setModel(new DefaultComboBoxModel(new String[] {"The Clones of James Buchanan (Easy)", "The Characters of Ulysses (Medium)", "Alcoholics of History (Hard)"}));
		//difficultyBox.setSelectedIndex(3);
		
		JLabel lblNewLabel = new JLabel("Opponents (Difficulty)");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Gill Sans MT Condensed", Font.PLAIN, 14));


		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(MainTitle, GroupLayout.PREFERRED_SIZE, 658, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(MainTitle, GroupLayout.PREFERRED_SIZE, 658, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(65)
					.addComponent(nameLabel, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(nameField, GroupLayout.PREFERRED_SIZE, 395, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(66, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(65)
					.addComponent(numLabel, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(numberField, GroupLayout.PREFERRED_SIZE, 395, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(66, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(245)
					.addComponent(startWarning, GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
					.addGap(245))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(181)
					.addComponent(btnStart, GroupLayout.PREFERRED_SIZE, 312, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(203, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(181)
							.addComponent(btnBrowse, GroupLayout.PREFERRED_SIZE, 312, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(65)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(theme, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel))
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(difficultyBox, GroupLayout.PREFERRED_SIZE, 321, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(greenTheme, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE)
									.addComponent(navyTheme, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE)))))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(chosenTheme, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(77, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(checkbox, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(lblUseTimer)
							.addGap(61))))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(MainTitle, GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
					.addGap(46)
					.addComponent(startWarning)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(nameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(nameLabel))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(numberField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(numLabel))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(greenTheme)
						.addComponent(navyTheme)
						.addComponent(chosenTheme)
						.addComponent(theme))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(difficultyBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(checkbox, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblUseTimer)
						.addComponent(lblNewLabel))
					.addGap(18)
					.addComponent(btnBrowse)
					.addGap(15)
					.addComponent(btnStart)
					.addGap(15))
		);

		avatar = new JLabel();
		avatar.setBounds(10, 10, 45, 45);
		getContentPane().add(avatar);

		contentPane.setLayout(gl_contentPane);

		btnBrowse.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				JFileChooser file = new JFileChooser();
				file.setCurrentDirectory(new File(System.getProperty("user.home")));
				// filter the files
				FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images", "jpg", "gif", "png");
				file.addChoosableFileFilter(filter);
				int result = file.showOpenDialog(null);
				// if the user click on save in JFileChooser
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = file.getSelectedFile();
					String path = selectedFile.getAbsolutePath();
					avatar.setIcon(ResizeImage(path));
				}
				// if the user click on save in JFileChooser
				else if (result == JFileChooser.CANCEL_OPTION) {
					System.out.println("No File Select");
				}
			}
		});

		greenTheme.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				contentPane.setBackground(new Color(0, 128, 0));

				whichTheme = 0;
				chosenTheme.setText("Default");
			}
		});

		navyTheme.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				contentPane.setBackground(new Color(31, 114, 205));
				
				whichTheme = 1;
				chosenTheme.setText("Navy");
			}
		});

		btnStart.addActionListener(new ActionListener() {
			@Override

			public void actionPerformed(ActionEvent e) {					
				playerName = nameField.getText();
				if (!numberField.getText().isEmpty()) {
					
					num = Integer.parseInt(numberField.getText());
				}

				if (checkbox.isSelected()) {
					limit = true;
				}
				//if player name is null a warning message will appear on screen
				if(playerName == null || nameField.getText().isEmpty())
					startWarning.setText("Player must enter name before starting");
								
				else if(num == 0 || num > 7 || numberField.getText().isEmpty())
					startWarning.setText("Not a valid number!");
								
				else {
					Random rand = new Random(System.currentTimeMillis());
					int diff = difficultyBox.getSelectedIndex();
					ArrayList<Player> players = new ArrayList<>();
					
					if(diff == 0) {
						
						String[] opponents = { "Doc James Buchanan", "Dopey James Buchanan", "Bashful James Buchanan", "Grumpy James Buchanan",
								"Sneezy James Buchanan", "Sleepy James Buchanan", "Happy James Buchanan" };
						for (int j = 0; j < num; j++) {
							try {
								players.add(new Player(true, opponents[j], 1000, j + 1));
								double aggro = rand.nextDouble();
								if(aggro >= .40) {
									
									aggro -= .15;
								}
								
								else if(aggro <= .10) {
									
									aggro +=.15;
								}
								
								players.get(j).setAggro(aggro);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
					else if(diff == 1) {
						
						String[] opponents = { "Leopold Bloom", "Stephen Dedalus", "Yelverton Barry", "Buck Mulligan",
								"Martin Cunningham", "Molly Bloom", "Josie Breen" };
						for (int j = 0; j < num; j++) {
							try {
								players.add(new Player(true, opponents[j], 1000, j + 11));
								double aggro = rand.nextDouble();
								if(aggro >= .75) {
									
									aggro -= .25;
								}
								
								else if(aggro <= .25) {
									
									aggro +=.25;
								}
								players.get(j).setAggro(aggro);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						
					}
					else {
						
						String[] opponents = { "Winston Churchill", "Ulysses S. Grant", "Alexander the Great", "Elizabeth Taylor",
								"Vincent van Gogh", "Ernest Hemingway", "Benjamin Franklin" };
						for (int j = 0; j < num; j++) {
							try {
								players.add(new Player(true, opponents[j], 1000, j + 21));
								double aggro = rand.nextDouble();
								
								if(aggro <= .50) {
									
									aggro +=.25;
								}
								players.get(j).setAggro(aggro);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						
					}
					
				try {
					user = new Player(false, playerName, 1000, 1);
				} catch (IOException e2) {}
				try {
					new MainFrame(user, players, whichTheme, avatar, limit);

				} catch (InterruptedException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} //starts the game passing the player name parameter

					setVisible(false);
					dispose();
				}
			}
		});
	}

	// Method to resize imageIcon with the same size of a JLabel
	public ImageIcon ResizeImage(String ImagePath) {
		ImageIcon MyImage = new ImageIcon(ImagePath);
		Image img = MyImage.getImage();
		Image newImg = img.getScaledInstance(avatar.getWidth(), avatar.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon image = new ImageIcon(newImg);
		return image;
	}

	@Override
	public void actionPerformed(ActionEvent e) {}
}
