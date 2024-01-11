import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class loading{
    JFrame frame;
    JLabel image = new JLabel(new ImageIcon("image/Puzzle_-_strategy_illustrated_icon_concept.jfif"));
    JProgressBar progressBar = new JProgressBar();

    /**
     * @wbp.parser.entryPoint
     */
    loading() {
        createGUI();
        addImage();
        addText();
        addProgressBar();
        addMessage();
        runningPBar();
    }

    public void createGUI() {
        frame = new JFrame();
        frame.getContentPane().setBackground(Color.GRAY);
        frame.getContentPane().setLayout(null);
        frame.setUndecorated(true);
        frame.setSize(602, 502);
        frame.setLocationRelativeTo(null);
       // frame.getContentPane().setBackground(Color.CYAN);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void addImage() {
        image.setLocation(0, 0);
        image.setSize(600, 393);
        frame.getContentPane().add(image);
    }

    public void addText() {
    }

    public void addMessage() {
    }

    public void addProgressBar() {
        progressBar.setBounds(95, 433, 400, 30);
        progressBar.setBorderPainted(true);
        progressBar.setStringPainted(true);
        progressBar.setBackground(Color.WHITE);
        progressBar.setForeground(Color.BLACK);
        progressBar.setValue(0);
        frame.getContentPane().add(progressBar);
    }

    public void runningPBar() {
        Timer timer = new Timer(10, new ActionListener() {
            private int i = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                progressBar.setValue(i);
                progressBar.setString("LOADING "+i+"%");
               // message.setText("LOADING " + i + "%");
                i++;

                if (i > 100) {
                    ((Timer) e.getSource()).stop(); // Stop the timer
                    frame.dispose();
                    playui ui = new playui();
                    ui.setVisible(true);
                }
            }
        });

        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new loading());
    }
}