import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

public class leaderboard extends JFrame {

	private JPanel contentPane;
	private Connection cn;
	private JTable table;
	private Statement st;
	private PreparedStatement pst;
	private getData data = new getData();
	private JTextField txtName;
	private String level;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton radioBtnEasy,radioBtnMedium,radioBtnHard;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					leaderboard frame = new leaderboard();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public leaderboard() {
		setIconImage(Toolkit.getDefaultToolkit().getImage("image/puzzle(1).png"));
		setResizable(false);
		setTitle("Leaderboard");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 800);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		table = new JTable();
		table.setFont(new Font("JetBrains Mono", Font.PLAIN, 15));
		//change the font and size of table header
		JTableHeader header = table.getTableHeader();
		header.setFont(new Font("JetBrains Mono", Font.PLAIN, 15));
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Name", "Score" }));
		//align the text to the center
		DefaultTableCellRenderer cell = new DefaultTableCellRenderer();
		cell.setHorizontalAlignment(JLabel.CENTER); 
		 for (int i = 0; i < table.getColumnCount(); i++) {
             table.getColumnModel().getColumn(i).setCellRenderer(cell);
         }

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(5, 49, 974, 684);
		contentPane.add(scrollPane);

		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String temp = data.getType();

				if (temp.equalsIgnoreCase("anime")) {
					data.setLoadIndex(0);
				} else if (temp.equalsIgnoreCase("cartoon")) {
					data.setLoadIndex(1);
				} else if (temp.equalsIgnoreCase("plant")) {
					data.setLoadIndex(2);
				}
				dispose();
				playui ui = new playui();
				ui.setVisible(true);
			}
		});
		btnBack.setForeground(Color.BLACK);
		btnBack.setBackground(Color.BLACK);
		btnBack.setOpaque(false);
		btnBack.setBorder(new RoundedBorder(10));

		btnBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnBack.setForeground(Color.magenta);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btnBack.setForeground(Color.black);
			}
		});
		btnBack.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
		btnBack.setBounds(872, 12, 107, 23);
		contentPane.add(btnBack);

		txtName = new JTextField();
		txtName.setEnabled(false);
		txtName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(radioBtnEasy.isSelected()) {
					level = "Easy";
				}
				else if(radioBtnMedium.isSelected()) {
					level = "Medium";
				}
				else if(radioBtnHard.isSelected()) {
					level = "Hard";
				}
				if(level != null) {
					getConnection();
					try {
						st = cn.createStatement();
						st.execute("use puzzleslide");
						DefaultTableModel model = (DefaultTableModel) table.getModel();
						model.setRowCount(0);

						pst = cn.prepareStatement("select name, minute, second from player where level = \"" +level+ "\" AND name like \""+txtName.getText()+"%\" order by concat(minute,second)");
						ResultSet rs = pst.executeQuery();
						while (rs.next()) {
							Object[] row = new Object[table.getColumnCount()];
							row[0] = rs.getString("name");
							row[1] = rs.getString("minute") + ":" + rs.getString("second");
							model.addRow(row);
						}
					} catch (SQLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				}
				else {
					return;
				}
			}
		});
		
		txtName.setBounds(99, 12, 143, 20);
		contentPane.add(txtName);
		txtName.setColumns(10);

		JLabel lblName = new JLabel("Username :");
		lblName.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
		lblName.setBounds(10, 14, 80, 14);
		contentPane.add(lblName);
		
		radioBtnEasy = new JRadioButton("Easy");
		radioBtnEasy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtName.setEnabled(true);
				txtName.setText(null);
				//call the database
				reloadDB("Easy");
			}
		});
		buttonGroup.add(radioBtnEasy);
		radioBtnEasy.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
		radioBtnEasy.setBounds(268, 11, 57, 23);
		contentPane.add(radioBtnEasy);
		
		radioBtnMedium = new JRadioButton("Medium");
		radioBtnMedium.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtName.setEnabled(true);
				txtName.setText(null);
				//call the database
				reloadDB("Medium");
			}
		});
		buttonGroup.add(radioBtnMedium);
		radioBtnMedium.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
		radioBtnMedium.setBounds(341, 11, 80, 23);
		contentPane.add(radioBtnMedium);
		
		radioBtnHard = new JRadioButton("Hard");
		radioBtnHard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtName.setEnabled(true);
				txtName.setText(null);
				//call the database
				reloadDB("Hard");
			}
		});
		buttonGroup.add(radioBtnHard);
		radioBtnHard.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
		radioBtnHard.setBounds(429, 11, 80, 23);
		contentPane.add(radioBtnHard);
	}

	private Connection getConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			cn = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "root");
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cn;
	}

	private void reloadDB(String str) {
		getConnection();
		try {
			st = cn.createStatement();
			st.execute("use puzzleslide");
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setRowCount(0);

			pst = cn.prepareStatement("select name, minute, second from player where level = \"" + str
					+ "\" order by concat(minute,second)");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Object[] row = new Object[table.getColumnCount()];
				row[0] = rs.getString("name");
				row[1] = rs.getString("minute") + ":" + rs.getString("second");
				model.addRow(row);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
