package exercise1;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

public class Games extends JFrame {
	private PreparedStatement pst;
	private Connection conn;
	private Statement stmt = null;
	private ResultSet rs = null;

	public static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	public static final String connectionUrl = "jdbc:sqlserver://localhost:1433;"
			+ "databaseName=GAMES;user=lchiang;password=abc123";

	private JTabbedPane tabs;

	/* DECLARATIONS FOR INSERT PLAYER/GAME */
	private JPanel pInsertGamePlayer, pInsertPlayerGameNorth, pInsertPlayerGameCenter, pInsertPlayerGameCenter1,
			pInsertPlayerGameCenter2;
	private JTextField txt1, txt2, txt3, txt4, txt5, txt6, txt7;
	private JLabel lblInsertNew, lbl1, lbl2, lbl3, lbl4, lbl5, lbl6, lbl7;
	private final JComboBox<String> objectsJComboBox;
	private JButton confirmChoice, confirmData, resetData, cancelOperation;
	private JTextArea txtAreaSummary;
	private static final String[] newObjects = { "Player", "Game" };

	/* DECLARATIONS FOR INSERT ACTIVITY */
	private JPanel pInsertActivity, pInsertActivityNorth1, pInsertActivityNorth2, pInsertActivityCenter,
			pInsertActivityCenter1, pInsertActivityCenter2;
	private JTextField txtActivity1, txtActivity2;
	private JLabel lblSelectPlayer, lblSelectGame, lblActivity1, lblActivity2;
	private JComboBox<String> playerJComboBox, gameJComboBox;
	private JButton confirmChoice2, confirmData2, resetData2, cancelOperation2;
	private JTextArea txtAreaSummaryActivity;

	/* DECLARATIONS FOR REPORT */
	public static final String DATABASE_URL = "jdbc:sqlserver://localhost:1433;databaseName=GAMES";
	public static final String USERNAME = "user=lchiang";
	public static final String PASSWORD = "password=abc123";

	// query retrieves all data about players and their respective played games
	static final String DEFAULT_QUERY = "SELECT [dbo].[PLAYER].PLAYER_ID, FIRST_NAME, LAST_NAME, GAME_TITLE, PLAYING_DATE, SCORE "
			+ "FROM [dbo].[PLAYER], [dbo].[GAME], [dbo].[PLAYER_GAME] "
			+ "WHERE [dbo].[PLAYER].PLAYER_ID = [dbo].[PLAYER_GAME].PLAYER_ID AND "
			+ "[dbo].[GAME].GAME_ID = [dbo].[PLAYER_GAME].GAME_ID";

	private JPanel pTable;
	private ResultSetTableModel tableModel;

	public Games() {
		super("Game GUI");

		////////// create GUI for Insert Players and Games
		// Panels
		pInsertGamePlayer = new JPanel();
		pInsertPlayerGameNorth = new JPanel();
		pInsertPlayerGameCenter = new JPanel();
		pInsertPlayerGameCenter1 = new JPanel();
		pInsertPlayerGameCenter2 = new JPanel();

		/* PANEL NORTH */
		pInsertPlayerGameNorth.setBorder(new EmptyBorder(10, 10, 30, 50));
		pInsertPlayerGameNorth.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

		lblInsertNew = new JLabel("Insert New:");

		objectsJComboBox = new JComboBox<String>(newObjects);

		confirmChoice = new JButton("OK");
		confirmChoice.addActionListener( // add ActionListener to show the form
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// According to what the user selected, show the
						// corresponding fields and labels
						if (objectsJComboBox.getSelectedIndex() == 0) {
							lbl1.setText("First Name");
							lbl1.setVisible(true);
							lbl2.setVisible(true);
							lbl3.setVisible(true);
							lbl4.setVisible(true);
							lbl5.setVisible(true);
							lbl6.setVisible(true);
							lbl7.setVisible(true);
							txt1.setVisible(true);
							txt2.setVisible(true);
							txt3.setVisible(true);
							txt4.setVisible(true);
							txt5.setVisible(true);
							txt6.setVisible(true);
							txt7.setVisible(true);

						} else {
							lbl1.setText("Game Title");
							lbl1.setVisible(true);
							lbl2.setVisible(false);
							lbl3.setVisible(false);
							lbl4.setVisible(false);
							lbl5.setVisible(false);
							lbl6.setVisible(false);
							lbl7.setVisible(false);
							txt1.setVisible(true);
							txt2.setVisible(false);
							txt3.setVisible(false);
							txt4.setVisible(false);
							txt5.setVisible(false);
							txt6.setVisible(false);
							txt7.setVisible(false);
						}

						// Reset the text in the text fields
						txt1.setText("");
						txt2.setText("");
						txt3.setText("");
						txt4.setText("");
						txt5.setText("");
						txt6.setText("");
						txt7.setText("");

						// Show form buttons
						resetData.setVisible(true);
						confirmData.setVisible(true);
						cancelOperation.setVisible(true);

						// Make form buttons enabled
						resetData.setEnabled(true);
						confirmData.setEnabled(true);
						cancelOperation.setEnabled(true);

						// make combobox and button disabled
						objectsJComboBox.setEnabled(false);
						confirmChoice.setEnabled(false);

						// enable text fields
						txt1.setEnabled(true);
						txt2.setEnabled(true);
						txt3.setEnabled(true);
						txt4.setEnabled(true);
						txt5.setEnabled(true);
						txt6.setEnabled(true);
						txt7.setEnabled(true);

						// Clear text in the text area
						txtAreaSummary.setText("");
					}
				});

		pInsertPlayerGameNorth.add(lblInsertNew);
		pInsertPlayerGameNorth.add(objectsJComboBox);
		pInsertPlayerGameNorth.add(confirmChoice);

		/* PANEL CENTER */
		pInsertPlayerGameCenter.setLayout(new GridLayout(1, 1, 6, 6));
		pInsertPlayerGameCenter1.setLayout(new GridLayout(7, 2, 6, 6));
		// pInsertPlayerCenter2.setLayout(new GridLayout(6,1,6,6));
		pInsertPlayerGameCenter2.setLayout(new BoxLayout(pInsertPlayerGameCenter2, BoxLayout.X_AXIS));

		// sub panel center 1
		lbl1 = new JLabel();
		lbl2 = new JLabel("Last Name");
		lbl3 = new JLabel("Address");
		lbl4 = new JLabel("Postal Code");
		lbl5 = new JLabel("Province");
		lbl6 = new JLabel("Phone Number");
		lbl7 = new JLabel("Username");

		lbl1.setVisible(false);
		lbl2.setVisible(false);
		lbl3.setVisible(false);
		lbl4.setVisible(false);
		lbl5.setVisible(false);
		lbl6.setVisible(false);
		lbl7.setVisible(false);

		txt1 = new JTextField(20);
		txt2 = new JTextField(20);
		txt3 = new JTextField(20);
		txt4 = new JTextField(20);
		txt5 = new JTextField(20);
		txt6 = new JTextField(20);
		txt7 = new JTextField(20);

		txt1.setVisible(false);
		txt2.setVisible(false);
		txt3.setVisible(false);
		txt4.setVisible(false);
		txt5.setVisible(false);
		txt6.setVisible(false);
		txt7.setVisible(false);

		pInsertPlayerGameCenter1.add(lbl1);
		pInsertPlayerGameCenter1.add(txt1);
		pInsertPlayerGameCenter1.add(lbl2);
		pInsertPlayerGameCenter1.add(txt2);
		pInsertPlayerGameCenter1.add(lbl3);
		pInsertPlayerGameCenter1.add(txt3);
		pInsertPlayerGameCenter1.add(lbl4);
		pInsertPlayerGameCenter1.add(txt4);
		pInsertPlayerGameCenter1.add(lbl5);
		pInsertPlayerGameCenter1.add(txt5);
		pInsertPlayerGameCenter1.add(lbl6);
		pInsertPlayerGameCenter1.add(txt6);
		pInsertPlayerGameCenter1.add(lbl7);
		pInsertPlayerGameCenter1.add(txt7);

		// sub panel center 2
		resetData = new JButton("Reset Fields");
		resetData.setVisible(false);
		resetData.addActionListener( // add ActionListener to reset form
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						txt1.setText("");
						txt2.setText("");
						txt3.setText("");
						txt4.setText("");
						txt5.setText("");
						txt6.setText("");
						txt7.setText("");
					}
				});
		confirmData = new JButton("Confirm");
		confirmData.setVisible(false);
		confirmData.addActionListener( // add ActionListener to insert new
										// player/game
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {

						boolean isAdded = false; // boolean to keep trace of the
													// success/failure of the
													// insert operation
						String newInformation = ""; // string to print the info
						if (objectsJComboBox.getSelectedIndex() == 0) {
							isAdded = prepareStatementInsertPlayer(txt1.getText(), txt2.getText(), txt3.getText(),
									txt4.getText(), txt5.getText(), txt6.getText(), txt7.getText());
							if (isAdded) {

								// Notify the user that the player has been
								// successfully added
								JOptionPane.showMessageDialog(null, "New Player Added!\n"
										+ "See summary of the information at the bottom of the window.");

								newInformation += "NEW PLAYER ADDED.\n\n";
								newInformation += "First Name: " + txt1.getText() + "\n";
								newInformation += "Last Name: " + txt2.getText() + "\n";
								newInformation += "Address: " + txt3.getText() + "\n";
								newInformation += "Postal Code: " + txt4.getText() + "\n";
								newInformation += "Province: " + txt5.getText() + "\n";
								newInformation += "Phone Number: " + txt6.getText() + "\n";
								newInformation += "Username: " + txt7.getText() + "\n";
							}
						} else {
							isAdded = prepareStatementInsertGame(txt1.getText());
							if (isAdded) {

								// Notify the user that the game has been
								// successfully added
								JOptionPane.showMessageDialog(null, "New Game Added!\n"
										+ "See summary of the information at the bottom of the window.");

								newInformation += "NEW GAME ADDED.\n\n";
								newInformation += "Game Title: " + txt1.getText() + "\n";
							}
						}

						txtAreaSummary.setText(newInformation);

						// make combo box and button enabled
						objectsJComboBox.setEnabled(true);
						confirmChoice.setEnabled(true);

						// Disable form buttons
						resetData.setEnabled(false);
						confirmData.setEnabled(false);

						// Disable text fields
						txt1.setEnabled(false);
						txt2.setEnabled(false);
						txt3.setEnabled(false);
						txt4.setEnabled(false);
						txt5.setEnabled(false);
						txt6.setEnabled(false);
						txt7.setEnabled(false);
					}
				});
		cancelOperation = new JButton("Cancel");
		cancelOperation.setVisible(false);
		cancelOperation.addActionListener( // add ActionListener to annul insert
											// operation
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {

						// make combo box and button enabled
						objectsJComboBox.setEnabled(true);
						confirmChoice.setEnabled(true);

						// make form buttons disabled
						resetData.setVisible(false);
						confirmData.setVisible(false);
						cancelOperation.setVisible(false);

						// Hide text fields and labels
						lbl1.setVisible(false);
						lbl2.setVisible(false);
						lbl3.setVisible(false);
						lbl4.setVisible(false);
						lbl5.setVisible(false);
						lbl6.setVisible(false);
						lbl7.setVisible(false);

						txt1.setVisible(false);
						txt2.setVisible(false);
						txt3.setVisible(false);
						txt4.setVisible(false);
						txt5.setVisible(false);
						txt6.setVisible(false);
						txt7.setVisible(false);

						// Clear text in the text area
						txtAreaSummary.setText("");
					}
				});

		pInsertPlayerGameCenter2.add(Box.createGlue());
		pInsertPlayerGameCenter2.add(resetData);
		pInsertPlayerGameCenter2.add(Box.createGlue());
		pInsertPlayerGameCenter2.add(confirmData);
		pInsertPlayerGameCenter2.add(Box.createGlue());
		pInsertPlayerGameCenter2.add(cancelOperation);

		// Add panels to the main central panel
		pInsertPlayerGameCenter.add(pInsertPlayerGameCenter1);
		pInsertPlayerGameCenter.add(pInsertPlayerGameCenter2);

		/* TEXTAREA SOUTH */
		txtAreaSummary = new JTextArea();
		txtAreaSummary.setRows(5);
		JScrollPane scroll = new JScrollPane(txtAreaSummary, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		// Add sub panels to the main container
		pInsertGamePlayer.setLayout(new BorderLayout());

		pInsertGamePlayer.add(pInsertPlayerGameNorth, BorderLayout.NORTH);
		pInsertGamePlayer.add(pInsertPlayerGameCenter, BorderLayout.CENTER);
		pInsertGamePlayer.add(scroll, BorderLayout.SOUTH);

		/////////////////////////////////////////////////////////////////////////////////////
		///////////// create GUI for Insert Activities (Game records played by a
		///////////////////////////////////////////////////////////////////////////////////// Player)

		// Panels
		pInsertActivity = new JPanel();
		pInsertActivityNorth1 = new JPanel();
		pInsertActivityNorth2 = new JPanel();
		pInsertActivityCenter = new JPanel();
		pInsertActivityCenter1 = new JPanel();
		pInsertActivityCenter2 = new JPanel();

		// Boxes
		Box verticalPlayerGame = Box.createVerticalBox();

		/* PANEL NORTH */
		pInsertActivityNorth1.setBorder(new EmptyBorder(10, 10, 10, 50));
		pInsertActivityNorth1.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		pInsertActivityNorth2.setBorder(new EmptyBorder(10, 10, 30, 50));
		pInsertActivityNorth2.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

		verticalPlayerGame.add(Box.createVerticalStrut(25)); // height 25

		lblSelectPlayer = new JLabel("Select Player:");
		lblSelectGame = new JLabel("Select Game:");

		playerJComboBox = new JComboBox<String>();
		gameJComboBox = new JComboBox<String>();
		populatePlayerJComboBox();
		populateGameJComboBox();

		confirmChoice2 = new JButton("OK");
		confirmChoice2.addActionListener( // add ActionListener to show the form
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {

						// Show text fields and labels
						lblActivity1.setVisible(true);
						lblActivity2.setVisible(true);
						txtActivity1.setVisible(true);
						txtActivity2.setVisible(true);

						// Reset the text in the text fields
						txtActivity1.setText("");
						txtActivity2.setText("");

						// Show form buttons
						resetData2.setVisible(true);
						confirmData2.setVisible(true);
						cancelOperation2.setVisible(true);

						// Make form buttons enabled
						resetData2.setEnabled(true);
						confirmData2.setEnabled(true);
						cancelOperation2.setEnabled(true);

						// make combobox and button disabled
						playerJComboBox.setEnabled(false);
						gameJComboBox.setEnabled(false);
						confirmChoice2.setEnabled(false);

						// enable text fields
						txtActivity1.setEnabled(true);
						txtActivity2.setEnabled(true);

						// Clear text in the text area
						txtAreaSummaryActivity.setText("");
					}
				});

		pInsertActivityNorth1.add(lblSelectPlayer);
		pInsertActivityNorth1.add(playerJComboBox);
		pInsertActivityNorth2.add(lblSelectGame);
		pInsertActivityNorth2.add(gameJComboBox);
		pInsertActivityNorth2.add(confirmChoice2);

		verticalPlayerGame.add(pInsertActivityNorth1);
		verticalPlayerGame.add(pInsertActivityNorth2);

		/* PANEL CENTER */
		pInsertActivityCenter.setLayout(new GridLayout(2, 1, 6, 6));
		pInsertActivityCenter1.setLayout(new GridLayout(2, 2, 6, 6));
		pInsertActivityCenter2.setLayout(new BoxLayout(pInsertActivityCenter2, BoxLayout.X_AXIS));

		// sub panel center 1
		lblActivity1 = new JLabel("Playing Date (format yyyy-mm-dd)");
		lblActivity2 = new JLabel("Score");

		lblActivity1.setVisible(false);
		lblActivity2.setVisible(false);

		txtActivity1 = new JTextField(20);
		txtActivity2 = new JTextField(20);

		txtActivity1.setVisible(false);
		txtActivity2.setVisible(false);

		pInsertActivityCenter1.add(lblActivity1);
		pInsertActivityCenter1.add(txtActivity1);
		pInsertActivityCenter1.add(lblActivity2);
		pInsertActivityCenter1.add(txtActivity2);

		// sub panel center 2
		resetData2 = new JButton("Reset Fields");
		resetData2.setVisible(false);
		resetData2.addActionListener( // add ActionListener to reset form
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						txtActivity1.setText("");
						txtActivity2.setText("");
					}
				});
		confirmData2 = new JButton("Confirm");
		confirmData2.setVisible(false);
		confirmData2.addActionListener( // add ActionListener to insert new
										// player/game
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						boolean isAdded = false; // boolean to keep trace of the
													// success/failure of the
													// insert operation
						String newInformation = ""; // string to print the info
						isAdded = prepareStatementInsertActivity(txtActivity1.getText(), txtActivity2.getText());
						if (isAdded) {

							// Notify the user that the activity of the player
							// has been successfully added
							JOptionPane.showMessageDialog(null, "New Activity Added!\n"
									+ "See summary of the information at the bottom of the window.");

							newInformation += "NEW ACTIVITY ADDED.\n\n";
							newInformation += "Game Title: " + gameJComboBox.getSelectedItem() + "\n";
							newInformation += "Player Name: " + playerJComboBox.getSelectedItem() + "\n";
							newInformation += "Playing Date: " + txtActivity1.getText() + "\n";
							newInformation += "Score: " + txtActivity2.getText() + "\n";
						}

						txtAreaSummaryActivity.setText(newInformation);

						// make combo box and button enabled
						playerJComboBox.setEnabled(true);
						gameJComboBox.setEnabled(true);
						confirmChoice2.setEnabled(true);

						// Disable form buttons
						resetData2.setEnabled(false);
						confirmData2.setEnabled(false);

						// Disable text fields
						txtActivity1.setEnabled(false);
						txtActivity2.setEnabled(false);
					}
				});
		cancelOperation2 = new JButton("Cancel");
		cancelOperation2.setVisible(false);
		cancelOperation2.addActionListener( // add ActionListener to annul
											// insert operation
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {

						// make combo box and buttons enabled
						playerJComboBox.setEnabled(true);
						gameJComboBox.setEnabled(true);
						confirmChoice2.setEnabled(true);

						// make form buttons disabled
						resetData2.setVisible(false);
						confirmData2.setVisible(false);
						cancelOperation2.setVisible(false);

						// Hide text fields and labels
						lblActivity1.setVisible(false);
						lblActivity2.setVisible(false);

						txtActivity1.setVisible(false);
						txtActivity2.setVisible(false);

						// Clear text in the text area
						txtAreaSummaryActivity.setText("");
					}
				});

		pInsertActivityCenter2.add(Box.createGlue());
		pInsertActivityCenter2.add(resetData2);
		pInsertActivityCenter2.add(Box.createGlue());
		pInsertActivityCenter2.add(confirmData2);
		pInsertActivityCenter2.add(Box.createGlue());
		pInsertActivityCenter2.add(cancelOperation2);

		// Add panels to the main central panel
		pInsertActivityCenter.add(pInsertActivityCenter1);
		pInsertActivityCenter.add(pInsertActivityCenter2);

		/* TEXTAREA SOUTH */
		txtAreaSummaryActivity = new JTextArea();
		txtAreaSummaryActivity.setRows(5);
		JScrollPane scrollActivity = new JScrollPane(txtAreaSummaryActivity, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		// Add sub panels, boxes and text area to the main container
		pInsertActivity.setLayout(new BorderLayout());

		pInsertActivity.add(verticalPlayerGame, BorderLayout.NORTH);
		pInsertActivity.add(pInsertActivityCenter, BorderLayout.CENTER);
		pInsertActivity.add(scrollActivity, BorderLayout.SOUTH);

		////////////////////////////////////////////////////////////////////////////////////////////////
		/////////// create GUI for Report
		pTable = new JPanel();

		// create ResultSetTableModel and display database table
		try {

			// create TableModel for results of query to show players and their
			// played games information
			tableModel = new ResultSetTableModel(DRIVER, DATABASE_URL, USERNAME, PASSWORD, DEFAULT_QUERY);

			// set up JButton for submitting queries
			JButton submitButton = new JButton("Refresh Information");

			// create JTable delegate for tableModel
			JTable resultTable = new JTable(tableModel);

			pTable.add(submitButton, BorderLayout.NORTH);
			pTable.add(new JScrollPane(resultTable), BorderLayout.CENTER);

			// create event listener for submitButton
			submitButton.addActionListener(

			new ActionListener() {
				// pass query to table model
				public void actionPerformed(ActionEvent event) {
					// perform a new query
					try {
						tableModel.setQuery(DEFAULT_QUERY);
					} // end try
					catch (SQLException sqlException) {
						JOptionPane.showMessageDialog(null, sqlException.getMessage(), "Database error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});

			final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
			resultTable.setRowSorter(sorter);
			setSize(500, 250); // set window size
			setVisible(true); // display window
		} // end try
		catch (ClassNotFoundException classNotFound) {
			JOptionPane.showMessageDialog(null, "Database Driver not found", "Driver not found",
					JOptionPane.ERROR_MESSAGE);

			System.exit(1); // terminate application
		} // end catch
		catch (SQLException sqlException) {
			JOptionPane.showMessageDialog(null, sqlException.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);

			// ensure database connection is closed
			tableModel.disconnectFromDatabase();

			System.exit(1); // terminate application
		} // end catch

		// dispose of window when user quits application
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// ensure database connection is closed when user quits application
		addWindowListener(new WindowAdapter() {
			// disconnect from database and exit when window has closed
			public void windowClosed(WindowEvent event) {
				tableModel.disconnectFromDatabase();
				System.exit(0);
			}
		});


		////////////////////////////////////////////////////////////////////////////////////////////////
		// Initialize tabbed window
		tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

		// Add panels to the tabbed window
		tabs.addTab("Insert New Player/Game", pInsertGamePlayer);
		tabs.addTab("Integrate New Player Activity", pInsertActivity);
		//tabs.addTab("Update Player/Games", pUpdatePlayer);
		tabs.addTab("Report", pTable);

		///////////// Add JTabbedPane to the window
		add(tabs);
	}

	// Method to add a new player with the data provided in the form
	// It returns true if the operation went smooth, false otherwise
	private boolean prepareStatementInsertPlayer(String firstName, String lastName, String address, String postCode,
			String province, String phone, String username) {
		boolean isAdded = false; // boolean to keep trace of the success/failure
									// of the insert operation
		try {
			Class.forName(DRIVER);

			conn = DriverManager.getConnection(connectionUrl);

			// Get the maximum index in the player table
			String SQL = "SELECT MAX(PLAYER_ID) FROM [dbo].[PLAYER]";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(SQL);
			int maxIndexPlayer = 0; // integer for max index
			if (rs.next()) {
				if (isInt(rs.getString(1))) {
					maxIndexPlayer = Integer.parseInt(rs.getString(1));
				}
			}

			// Prepare statement for inserting the new player
			pst = conn.prepareStatement("Insert into [dbo].[PLAYER] "
					+ "(PLAYER_ID, FIRST_NAME, LAST_NAME, ADDRESS, POST_CODE, PROVINCE, PHONE_NUMBER, USERNAME) "
					+ "VALUES (?,?,?,?,?,?,?,?)");
			pst.setInt(1, maxIndexPlayer + 1);
			pst.setString(2, firstName);
			pst.setString(3, lastName);
			pst.setString(4, address);
			pst.setString(5, postCode);
			pst.setString(6, province);
			pst.setString(7, phone);
			pst.setString(8, username);

			pst.executeUpdate(); // execute insert

			isAdded = true; // return t
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "SQL error", null, JOptionPane.ERROR_MESSAGE);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Connection error", null, JOptionPane.ERROR_MESSAGE);
		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (Exception e) {
				}
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
				}
			}
		}
		return isAdded;
	}

	// Method to add a new game with the data provided in the form
	// It returns true if the operation went smooth, false otherwise
	private boolean prepareStatementInsertGame(String gameTitle) {
		boolean isAdded = false; // boolean to keep trace of the success/failure
									// of the insert operation
		try {
			Class.forName(DRIVER);

			conn = DriverManager.getConnection(connectionUrl);

			// Get the maximum index in the player table
			String SQL = "SELECT MAX(GAME_ID) FROM [dbo].[GAME]";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(SQL);
			int maxIndexGame = 0; // integer for max index
			if (rs.next()) {
				if (isInt(rs.getString(1))) {
					maxIndexGame = Integer.parseInt(rs.getString(1));
				}
			}

			// Prepare statement for inserting the new player
			pst = conn.prepareStatement("Insert into [dbo].[GAME] " + "(GAME_ID, GAME_TITLE) VALUES (?,?)");
			pst.setInt(1, maxIndexGame + 1);
			pst.setString(2, gameTitle);

			pst.executeUpdate(); // execute insert

			isAdded = true;
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "SQL error", null, JOptionPane.ERROR_MESSAGE);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Connection error", null, JOptionPane.ERROR_MESSAGE);
		} finally {
			System.out.println("Done!");

			if (pst != null) {
				try {
					pst.close();
				} catch (Exception e) {
				}
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
				}
			}
		}
		return isAdded;
	}

	// Function to check if a string can be parsed to an integer
	public static boolean isInt(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	// Method to populate the player JComboBox
	public void populatePlayerJComboBox() {
		try {
			SQLServerDataSource ds = new SQLServerDataSource();

			ds.setUser("lchiang");
			ds.setPassword("abc123");
			ds.setServerName("localhost");
			ds.setPortNumber(1433);
			ds.setDatabaseName("GAMES");

			conn = ds.getConnection(); // it establishes the connection

			// Get the maximum index in the player table
			String SQL = "SELECT MAX(PLAYER_ID) FROM [dbo].[PLAYER]";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(SQL);
			int maxIndexPlayer = 0; // integer for max index
			if (rs.next()) {
				if (isInt(rs.getString(1))) {
					maxIndexPlayer = Integer.parseInt(rs.getString(1));
				}
			}

			if (maxIndexPlayer > 0) { // if there is at least one player in the
										// list
				// We build SQL query to get the players
				SQL = "SELECT PLAYER_ID, FIRST_NAME, LAST_NAME FROM [dbo].[PLAYER]";
				stmt = conn.createStatement(); // pass argument to the Statement
												// object
				rs = stmt.executeQuery(SQL);

				while (rs.next()) { // next iterates inside the result
					playerJComboBox.addItem(rs.getString(1) + ". " + rs.getString(2) + " " + rs.getString(3));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
				}
			}
		}
	}

	// Method to populate the game JComboBox
	public void populateGameJComboBox() {
		try {
			SQLServerDataSource ds = new SQLServerDataSource();

			ds.setUser("lchiang");
			ds.setPassword("abc123");
			ds.setServerName("localhost");
			ds.setPortNumber(1433);
			ds.setDatabaseName("GAMES");

			conn = ds.getConnection(); // it establishes the connection

			// Get the maximum index in the player table
			String SQL = "SELECT MAX(GAME_ID) FROM [dbo].[GAME]";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(SQL);
			int maxIndexGame = 0; // integer for max index
			if (rs.next()) {
				if (isInt(rs.getString(1))) {
					maxIndexGame = Integer.parseInt(rs.getString(1));
				}
			}

			if (maxIndexGame > 0) { // if there is at least one game in the list
				// We build SQL query to get the games
				SQL = "SELECT GAME_ID, GAME_TITLE FROM [dbo].[GAME]";
				stmt = conn.createStatement(); // pass argument to the Statement
												// object
				rs = stmt.executeQuery(SQL);

				while (rs.next()) { // next iterates inside the result
					gameJComboBox.addItem(rs.getString(2));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
				}
			}
		}
	}

	// Method to add a new activity of a player with the data provided in the
	// form
	// It returns true if the operation went smooth, false otherwise
	private boolean prepareStatementInsertActivity(String playingDate, String score) {
		boolean isAdded = false; // boolean to keep trace of the success/failure
									// of the insert operation
		try {
			Class.forName(DRIVER);

			conn = DriverManager.getConnection(connectionUrl);

			// Get the maximum index in the player table
			String SQL = "SELECT MAX(PLAYER_GAME_ID) FROM [dbo].[PLAYER_GAME]";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(SQL);
			int maxIndexPlayerGame = 0; // integer for max index
			if (rs.next()) {
				if (isInt(rs.getString(1))) {
					maxIndexPlayerGame = Integer.parseInt(rs.getString(1));
				}
			}

			// Get index of the selected player
			SQL = "SELECT a.PLAYER_ID FROM "
					+ "(SELECT ROW_NUMBER() OVER(ORDER BY PLAYER_ID) AS ROWNUMBER, PLAYER_ID FROM [dbo].[PLAYER]) a "
					+ "WHERE a.ROWNUMBER = " + (playerJComboBox.getSelectedIndex() + 1);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(SQL);
			int indexPlayer = 0; // integer for player index
			if (rs.next()) {
				if (isInt(rs.getString(1))) {
					indexPlayer = Integer.parseInt(rs.getString(1));
				}
			}

			// Get the index of the selected game
			SQL = "SELECT a.GAME_ID FROM "
					+ "(SELECT ROW_NUMBER() OVER(ORDER BY GAME_ID) AS ROWNUMBER, GAME_ID FROM [dbo].[GAME]) a "
					+ "WHERE a.ROWNUMBER = " + (gameJComboBox.getSelectedIndex() + 1);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(SQL);
			int indexGame = 0; // integer for game index
			if (rs.next()) {
				if (isInt(rs.getString(1))) {
					indexGame = Integer.parseInt(rs.getString(1));
				}
			}

			// Prepare statement for inserting the new player activity
			pst = conn.prepareStatement("Insert into [dbo].[PLAYER_GAME] "
					+ "(PLAYER_ID, GAME_ID, PLAYING_DATE, SCORE, PLAYER_GAME_ID) " + "VALUES (?,?,?,?,?)");
			pst.setInt(1, indexPlayer);
			pst.setInt(2, indexGame);
			pst.setString(3, txtActivity1.getText());
			pst.setString(4, txtActivity2.getText());
			pst.setInt(5, maxIndexPlayerGame + 1);

			pst.executeUpdate(); // execute insert

			isAdded = true; // return t
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "SQL error", null, JOptionPane.ERROR_MESSAGE);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Connection error", null, JOptionPane.ERROR_MESSAGE);
		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (Exception e) {
				}
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
				}
			}
		}
		return isAdded;
	}

	public String returSelecUpdate(int idGame, String field) {
		// Declare the JDBC objects.
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(connectionUrl);

			// Create and execute an SQL statement that returns some data.
			String SQL = "SELECT FIRST_NAME,LAST_NAME,ADDRESS,POST_CODE,PROVINCE,PHONE_NUMBER,PLAYING_DATE,SCORE,G.GAME_ID, PG.PLAYER_GAME_ID, G.GAME_TITLE"
					+ " FROM [GAMES].[dbo].[PLAYER] P," + "[GAMES].[dbo].[PLAYER_GAME] PG," + "[GAMES].[dbo].[GAME] G"
					+ " WHERE P.PLAYER_ID = PG.PLAYER_ID" + " AND PG.GAME_ID = G.GAME_ID" + " AND PG.PLAYER_GAME_ID ="
					+ idGame;
			stmt = con.createStatement();
			rs = stmt.executeQuery(SQL);
			// Iterate through the data in the result set and display it.
			while (rs.next()) {
				if (field == "FIRSTNAME") {
					return rs.getString(1);
				}
				if (field == "LASTNAME") {
					return rs.getString(2);
				}
				if (field == "ADDRESS") {
					return rs.getString(3);
				}
				if (field == "POST_CODE") {
					return rs.getString(4);
				}
				if (field == "PROVINCE") {
					return rs.getString(5);
				}
				if (field == "PHONENUMBER") {
					return rs.getString(6);
				}
				if (field == "PLAYINGDATE") {
					return rs.getString(7);
				}
				if (field == "SCORE") {
					return rs.getString(8);
				}
				if (field == "GAMEID") {
					return rs.getString(9);
				}
				if (field == "PLAYER_GAME_ID") {
					return rs.getString(10);
				}
				if (field == "GAME_TITLE") {
					return rs.getString(11);
				}

			}
		}
		// Handle any errors that may have occurred.
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (con != null)
				try {
					con.close();
				} catch (Exception e) {
				}
		}
		return "";
	}
}
