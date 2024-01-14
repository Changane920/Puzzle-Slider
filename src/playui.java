import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class playui extends JFrame implements MouseListener {
	Image img;
	private JPanel contentPane;
	private JLabel img6;
	private File file;
	private JComboBox cbImgType;
	private Connection cn;
	private Statement st;
	private PreparedStatement getData;
	private JLabel[] labelArr = new JLabel[5];
	private JPanel imgPanel;
	private getData data = new getData();
	private String type;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					playui frame = new playui();
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
	public playui() {
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage("image/puzzle(1).png"));
		setTitle("Feliz Arcade");
		setBackground(Color.LIGHT_GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 800);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Welcome to Feliz Arcade");
		lblNewLabel.setIcon(new ImageIcon("image/puzzle(1).png"));
		lblNewLabel.setBackground(SystemColor.activeCaption);
		lblNewLabel.setForeground(new Color(0, 0, 0));
		lblNewLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 26));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(266, 0, 454, 81);
		contentPane.add(lblNewLabel);

		JButton btnLeaderBoard = new JButton("Leaderboard");
		btnLeaderBoard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				leaderboard showlb = new leaderboard();
				showlb.setVisible(true);
			}
		});
		btnLeaderBoard.setBackground(new Color(255, 255, 255));
		btnLeaderBoard.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnLeaderBoard.setBackground(Color.black);
				btnLeaderBoard.setForeground(Color.white);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btnLeaderBoard.setBackground(Color.white);
				btnLeaderBoard.setForeground(Color.black);
			}
		});
		btnLeaderBoard.setFont(new Font("JetBrains Mono", Font.PLAIN, 16));
		btnLeaderBoard.setBounds(383, 704, 167, 35);
		contentPane.add(btnLeaderBoard);

		JLabel lblNewLabel_1 = new JLabel("Image Type");
		lblNewLabel_1.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(51, 93, 85, 14);
		contentPane.add(lblNewLabel_1);

		// make image change according to the index
		cbImgType = new JComboBox();
		cbImgType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (cbImgType.getSelectedIndex()) {
				case 0:
					data.setType("anime");
					data.setLoadIndex(0);
					LoadImg("Anime");
					break;
				case 1:
					data.setType("cartoon");
					data.setLoadIndex(1);
					LoadImg("Cartoon");
					break;
				case 2:
					data.setType("plant");
					data.setLoadIndex(2);
					LoadImg("Plant");
					break;
				}
			}
		});

		cbImgType.setModel(new DefaultComboBoxModel(new String[] { "Anime", "Cartoon", "Plant" }));
		// make startup with index 0
		cbImgType.setSelectedIndex(data.getLoadIndex());
		cbImgType.setBounds(146, 90, 121, 22);
		contentPane.add(cbImgType);

		imgPanel = new JPanel();
		imgPanel.setBackground(Color.LIGHT_GRAY);
		imgPanel.setBounds(51, 119, 908, 586);
		contentPane.add(imgPanel);
		imgPanel.setLayout(new GridLayout(2, 3, 5, 5));

		for (int i = 0; i < labelArr.length; i++) {
			imgPanel.add(labelArr[i]);
		}

		img6 = new JLabel("");
		img6.setHorizontalAlignment(SwingConstants.LEADING);
		img6.setBounds(627, 306, 250, 250);
		img6.setIcon(new ImageIcon("image/fileclose.png"));
		img6.setBackground(Color.LIGHT_GRAY);
		img6.setOpaque(true);
		imgPanel.add(img6);
		img6.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseEntered(MouseEvent e) {
				img6.setIcon(new ImageIcon("image/fileopen.png"));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				img6.setIcon(new ImageIcon("image/fileclose.png"));
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser filechooser = new JFileChooser();
				filechooser.setDialogTitle("Choose Your File");
				filechooser.setFileSelectionMode(filechooser.FILES_ONLY);
				int returnval = filechooser.showOpenDialog(filechooser);
				if (returnval == JFileChooser.APPROVE_OPTION) {
					file = filechooser.getSelectedFile();
					getData data = new getData();
					data.setImgPath(file.getPath());
					BufferedImage img = null;
					try {
						img = ImageIO.read(file);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					data.setImg(img);
					game game = new game();
					game.setVisible(true);
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				img6.setBackground(Color.LIGHT_GRAY);
			}
		});
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

	public void LoadImg(String getType) {
		try {
			cn = getConnection();
			st = cn.createStatement();
			st.execute("use puzzleslide");
			getData = cn.prepareStatement("select * from " + getType + "");

			ResultSet getPath = getData.executeQuery();
			int index = 0;
			while (getPath.next()) {
				String tempPath = getPath.getString("path");

				ImageIcon getImageIcon = new ImageIcon(tempPath);
				Image getImg = getImageIcon.getImage();
				Image scaleImg = getImg.getScaledInstance(250, 250, Image.SCALE_SMOOTH);
				ImageIcon setImageIcon = new ImageIcon(scaleImg);
				// increase the size of array
				JLabel temp = new JLabel();
				temp.setIcon(setImageIcon);
				temp.addMouseListener(this);
				labelArr[index] = temp;
				index++;
			}
			if(imgPanel!=null) {
				imgPanel.removeAll();
				imgPanel.repaint();
				imgPanel.revalidate();
				for (int i = 0; i < labelArr.length; i++) {
					imgPanel.add(labelArr[i]);
				}
				imgPanel.add(img6);				
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setFile(String path) {
		data.setImgPath(path);
		file = new File(data.getImgPath());
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(data.getImgPath()));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		data.setImg(img);
		// pass the data to game form
		game game = new game();
		game.setString(type);
		game.setVisible(true);
		this.dispose();
	}

	String tableName;

	@Override
	public void mouseClicked(MouseEvent e) {
		try {
			cn = getConnection();
			st = cn.createStatement();
			st.execute("use puzzleslide");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			switch (cbImgType.getSelectedIndex()) {
			case 0:
				tableName = "anime";
				break;
			case 1:
				tableName = "cartoon";
				break;
			case 2:
				tableName = "plant";
				break;
			}

			getData = cn.prepareStatement("select * from " + tableName + " where name = ?");

			String tempPath;

			if (e.getSource() == labelArr[0]) {
				getData.setString(1, "img1");
			} else if (e.getSource() == labelArr[1]) {
				getData.setString(1, "img2");
			} else if (e.getSource() == labelArr[2]) {
				getData.setString(1, "img3");
			} else if (e.getSource() == labelArr[3]) {
				getData.setString(1, "img4");
			} else if (e.getSource() == labelArr[4]) {
				getData.setString(1, "img5");
			}
			ResultSet rst = getData.executeQuery();
			if (rst.next()) {
				tempPath = rst.getString("path");
				setFile(tempPath);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	Icon temp;

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		JLabel label = (JLabel) e.getSource();
		temp = label.getIcon();
		ImageIcon getIcon = (ImageIcon) label.getIcon();
		Image image = getIcon.getImage();
		int width = image.getWidth(null);
		int height = image.getHeight(null);

		// Create a BufferedImage from the image
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = bufferedImage.getGraphics();
		g.drawImage(image, 0, 0, null);

		// Apply a blur effect using RescaleOp
		float[] scales = { 0.5f, 0.5f, 0.5f, 2f };
		float[] offsets = new float[4];
		RescaleOp rescaleOp = new RescaleOp(scales, offsets, null);
		bufferedImage = rescaleOp.filter(bufferedImage, null);

		// Create a new ImageIcon with the blurred image
		ImageIcon blurredIcon = new ImageIcon(bufferedImage);

		// Set the JLabel's icon to the blurred image
		label.setIcon(blurredIcon);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		JLabel label = (JLabel) e.getSource();
		label.setIcon(temp);
	}
}
