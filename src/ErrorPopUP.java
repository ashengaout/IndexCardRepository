import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ErrorPopUP {
    private JFrame deleteFrame;
    private JPanel ErrorPopUp;
    private JButton backToMainGUIButton;

    public ErrorPopUP() {
        backToMainGUIButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteFrame.dispose();
            }
        });
    }

    public void createWindow() {
        deleteFrame = new JFrame("ERROR");
        deleteFrame.setContentPane(ErrorPopUp);
        deleteFrame.setDefaultCloseOperation(deleteFrame.EXIT_ON_CLOSE);
        deleteFrame.pack();
        deleteFrame.setVisible(true);
    }
}
