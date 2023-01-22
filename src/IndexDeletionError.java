import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IndexDeletionError {
    private JFrame frame;
    private JPanel panel1;
    private JTextArea pleaseEnsureTheListTextArea;
    private JButton backToMainMenuButton;

    public IndexDeletionError() {
        this.addActionListener();
    }

    public void addActionListener() {
        backToMainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }

    public void createWindow() {
        frame = new JFrame("ERROR");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
