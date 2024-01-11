import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class leaderboard extends JFrame {

	private JPanel contentPane;
	private Connection cn;
	private JTable table;
	private Statement st;
	private PreparedStatement pst;
	private JMenuItem mnEasy,mnMedium,mnHard;
	private getData data = new getData();

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
		setBounds(100, 100, 419, 452);
		setLocationRelativeTo(null);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnEasy = new JMenuItem("Easy");
		mnEasy.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				mnEasy.setBackground(Color.black);
				mnEasy.setForeground(Color.white);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				mnEasy.setBackground(Color.DARK_GRAY);
				mnMedium.setBackground(Color.white);
				mnMedium.setForeground(Color.black);
				mnHard.setBackground(Color.white);
				mnHard.setForeground(Color.black);
			}
		});
		mnEasy.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
		mnEasy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reloadDB("Easy");
			}
		});
		mnEasy.setHorizontalAlignment(SwingConstants.CENTER);
		menuBar.add(mnEasy);
		
		mnMedium = new JMenuItem("Medium");
		mnMedium.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				mnMedium.setBackground(Color.black);
				mnMedium.setForeground(Color.white);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				mnEasy.setBackground(Color.white);
				mnEasy.setForeground(Color.black);
				mnMedium.setBackground(Color.DARK_GRAY);
				mnHard.setBackground(Color.white);
				mnHard.setForeground(Color.black);
			}
		});
		mnMedium.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
		mnMedium.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reloadDB("Medium");
			}
		});
		mnMedium.setHorizontalAlignment(SwingConstants.CENTER);
		menuBar.add(mnMedium);
		
		mnHard = new JMenuItem("Hard");
		mnHard.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				mnHard.setBackground(Color.black);
				mnHard.setForeground(Color.white);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				mnEasy.setBackground(Color.white);
				mnEasy.setForeground(Color.black);
				mnMedium.setBackground(Color.white);
				mnMedium.setForeground(Color.black);
				mnHard.setBackground(Color.DARK_GRAY);
			}
		});
		mnHard.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
		mnHard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reloadDB("Hard");
			}
		});
		mnHard.setHorizontalAlignment(SwingConstants.CENTER);
		menuBar.add(mnHard);
		
		JMenuItem mnBack = new JMenuItem("Back");
		mnBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				mnBack.setBackground(Color.black);
				mnBack.setForeground(Color.white);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				mnBack.setBackground(Color.gray);
				mnBack.setForeground(Color.white);
			}
		});
		mnBack.setForeground(Color.WHITE);
		mnBack.setBackground(Color.GRAY);
		mnBack.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
		mnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String temp = data.getType();
				if(temp.equalsIgnoreCase("anime")) {
					data.setLoadIndex(0);
				}
				else if(temp.equalsIgnoreCase("cartoon")) {
					data.setLoadIndex(1);
				}
				else if(temp.equalsIgnoreCase("plant")) {
					data.setLoadIndex(2);
				}
				dispose();
				playui ui = new playui();
				ui.setVisible(true);
			}
		});
		mnBack.setHorizontalAlignment(SwingConstants.CENTER);
		menuBar.add(mnBack);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(30, 10));
		
		table = new JTable();
		table.setFont(new Font("JetBrains Mono", Font.PLAIN, 13));
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Name", "Score"
			}
		));
		//contentPane.add(table, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane(table);
		contentPane.add(scrollPane, BorderLayout.NORTH);
	}
	private Connection getConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			cn = DriverManager.getConnection("jdbc:mysql://localhost:3306","root","root");
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
			
			pst = cn.prepareStatement("select name, minute, second from player where level = \""+str+"\" order by concat(minute,second)");
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
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
