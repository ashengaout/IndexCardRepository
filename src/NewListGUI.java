import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class NewListGUI {
    private JFrame newList;
    private JPanel panel1;
    private JButton saveButton;
    private JButton cancelButton;
    private JTextField listNameTxt;
    private Java2SQL connection;
    private MainGUI.JComboBoxModel comboBoxModel;

    public NewListGUI(Java2SQL connection2DB, MainGUI.JComboBoxModel combo) {
        connection = connection2DB;
        this.addSaveButtonListener();
        this.addCancelButtonListener();
        comboBoxModel = combo;
    }

    public void addSaveButtonListener() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = listNameTxt.getText();
                List list = new List(name);

                try {
                    connection.createList(list);
                    comboBoxModel.updateLists();
                }
                catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                newList.dispose();
            }
        });
    }

    public void addCancelButtonListener() {
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newList.dispose();
            }
        });
    }

    public void createWindow() {
        newList = new JFrame("New List");
        newList.setContentPane(panel1);
        newList.setDefaultCloseOperation(newList.EXIT_ON_CLOSE);
        newList.pack();
        newList.setVisible(true);
    }
}
