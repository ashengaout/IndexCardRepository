import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class DeleteListGUI {
    private JButton yesButton;
    private JButton cancelButton;
    private JFrame deleteFrame;
    private JPanel panel1;
    private JTextArea areYouSureYouTextArea;
    private Java2SQL connection;
    private MainGUI.JComboBoxModel comboBoxModel;
    private int ID;
    private ArrayList<Card> cardList;

    public DeleteListGUI(Java2SQL connection, int ID, MainGUI.JComboBoxModel comboBoxModel, ArrayList<Card> indexList) {
        this.connection = connection;
        this.ID = ID;
        this.addCancelButtonListener();
        this.addYesButtonListener();
        this.comboBoxModel = comboBoxModel;
        cardList = indexList;
    }

    public void addYesButtonListener() {
        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    connection.deleteList(ID);
                    comboBoxModel.updateLists();
                }
                catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                deleteFrame.dispose();
            }
        });
    }

    public void addCancelButtonListener() {
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteFrame.dispose();
            }
        });
    }

    public void createWindow() {
        deleteFrame = new JFrame("");
        deleteFrame.setContentPane(panel1);
        deleteFrame.setDefaultCloseOperation(deleteFrame.EXIT_ON_CLOSE);
        deleteFrame.pack();
        deleteFrame.setVisible(true);
    }
}
