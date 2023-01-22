import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class CardDialogGUI extends Container {
    private JFrame newCard;
    private JPanel cardDialogGUI;
    private Card currentCard;
    private JTextField headerTxtField;
    private JButton saveButton;
    private JButton cancelButton;
    private JTextArea bodyTxtField;
    private Java2SQL connection;
    private MainGUI.IndexTableModel tableModel;
    private int listIdentity;

    public CardDialogGUI(Card inputCard, Java2SQL connection2DB, MainGUI.IndexTableModel model) {
        connection = connection2DB;
        this.saveButtonActionListener();
        this.cancelButtonActionListener();
        currentCard = inputCard;
        tableModel = model;
        headerTxtField.setText(currentCard.getHeader());
        bodyTxtField.setText(currentCard.getBody());
    }

    public CardDialogGUI(Card inputCard, Java2SQL connection2DB, MainGUI.IndexTableModel model, int listID) {
        connection = connection2DB;
        this.saveButtonActionListener();
        this.cancelButtonActionListener();
        currentCard = inputCard;
        tableModel = model;
        headerTxtField.setText(currentCard.getHeader());
        bodyTxtField.setText(currentCard.getBody());
        listIdentity = listID;
    }

    public void saveButtonActionListener() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentCard.setUpdated(true);
                currentCard.setHeader(headerTxtField.getText());
                currentCard.setBody(bodyTxtField.getText());
                currentCard.setDateUpdated();
                currentCard.setListID(listIdentity);

                if(currentCard.getID() != 0) {
                    currentCard.setDateUpdated();

                    try {connection.updateCards(currentCard);}
                    catch (SQLException ex) {throw new RuntimeException(ex);}
                }

                else {
                    try {connection.createCards(currentCard);}
                    catch (SQLException ex) {throw new RuntimeException(ex);}
                }

                newCard.dispose();
                tableModel.updateData();
                tableModel.fireTableDataChanged();
            }
        });
    }

    public void cancelButtonActionListener() {
        cancelButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            currentCard.setUpdated(false);
            newCard.dispose();
        }
    });}

    public void createWindow(String title) {
        newCard = new JFrame(title);
        newCard.setContentPane(cardDialogGUI);
        newCard.setDefaultCloseOperation(newCard.EXIT_ON_CLOSE);
        newCard.pack();
        newCard.setVisible(true);
    }
}
