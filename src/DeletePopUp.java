import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class DeletePopUp extends Container {
    private JFrame deleteFrame;
    private JPanel deletePopUp;
    private JTextArea areYouSureYouTextArea;
    private JButton yesButton;
    private JButton cancelButton;
    private Card currentCard;
    private Java2SQL connection2DB;
    private MainGUI.IndexTableModel indexTableModel;
    private String selected;

    public DeletePopUp(Card inputCard, Java2SQL connection, MainGUI.IndexTableModel indexModel, String sortBox) {
        addYesButtonActionListener();
        addCancelButtonActionListener();
        currentCard = inputCard;
        connection2DB = connection;
        indexTableModel = indexModel;
        selected = sortBox;
    }

    //this needs to set isUpdated to true and then the GUI needs to close
    public void addYesButtonActionListener() {
        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentCard.setUpdated(true);
                try {connection2DB.deleteCards(currentCard);}
                catch (SQLException ex) {throw new RuntimeException(ex);}
                deleteFrame.dispose();
                indexTableModel.updateData(selected);
                indexTableModel.fireTableDataChanged();
            }
        });
    }

    //it sets isUpdated to false and should exit the GUI
    public void addCancelButtonActionListener() {
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentCard.setUpdated(false);
                deleteFrame.dispose();
            }
        });
    }

    public void createWindow() {
        deleteFrame = new JFrame("");
        deleteFrame.setContentPane(deletePopUp);
        deleteFrame.setDefaultCloseOperation(deleteFrame.EXIT_ON_CLOSE);
        deleteFrame.pack();
        deleteFrame.setVisible(true);
    }

    public void updateData() {

    }
}
