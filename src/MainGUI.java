import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainGUI  {
    private JPanel mainGUI;
    private JComboBox sortBox;
    private JTable dataTable;
    private JButton newCardButton;
    private JButton deleteCardButton;
    private JButton updateCardButton;
    private static Java2SQL connection2DB;
    private static ArrayList <Card> indexList;
    private static IndexTableModel indexTableModel;

    public static void main(String[] args) {
        JFrame mainGUIFrame = new JFrame ("Cards");
        mainGUIFrame.setContentPane(new MainGUI().mainGUI);
        mainGUIFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainGUIFrame.pack();
        mainGUIFrame.setVisible(true);
    }

    public MainGUI() {
        try {
            connection2DB = new Java2SQL();
            connection2DB.getIndexCardsAlphabetical();

            this.addSortActionListener();
            this.addCreateActionListener();
            this.addUpdateActionListener();
            this.addDeleteActionListener();
            this.displayData();
        }

        catch (SQLException x) {x.printStackTrace();}
    }

    //retrieves proper order and places it into the index list, also calls the redisplay method
    public void addSortActionListener() {
        sortBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayData();
            }
        });
    }

    //creates a card dialog object and opens that GUI and creates a new default card that is passed as a parameter
    //If save button is clicked then the card isUpdated and the fields are updated and passed into the DB (in Java2SQL class)
    public void addCreateActionListener() {
        newCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Card currentCard = new Card();
                String selected = sortBox.getSelectedItem().toString();

                CardDialogGUI cardDialogGUI = new CardDialogGUI(currentCard, connection2DB, indexTableModel, selected);

                cardDialogGUI.createWindow("New Card");
            }
        });
    }

    //when clicked the index of the selected row is obtained and a card dialog pops up passing the selected card as a parameter
    //if save is clicked in card dialog then the card isUpdated and the header, body, and dateUpdated are all updated and the card is then passed into the DB
    public void addUpdateActionListener() {
        updateCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int x = dataTable.getSelectedRow();

                if(x > -1) {
                    Card currentCard = indexList.get(x);
                    String selected = sortBox.getSelectedItem().toString();

                    CardDialogGUI cardDialogGUI = new CardDialogGUI(currentCard, connection2DB, indexTableModel, selected);

                    cardDialogGUI.createWindow("Update Card");
                }

                else {
                    ErrorPopUP errorPopUP = new ErrorPopUP();
                    errorPopUP.createWindow();
                }
            }
        });
    }

    //this creates a DeletePopUp objects and obtains the selected card from the index list.
    //If yes is clicked then the card is deleted from the DB
    public void addDeleteActionListener() {
        deleteCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int x = dataTable.getSelectedRow();

                if(x > -1) {
                    Card currentCard = indexList.get(x);
                    String selected = sortBox.getSelectedItem().toString();

                    DeletePopUp deletePopUp = new DeletePopUp(currentCard, connection2DB, indexTableModel, selected);

                    deletePopUp.createWindow();
                }

                else {
                    ErrorPopUP errorPopUP = new ErrorPopUP();
                    errorPopUP.createWindow();
                }
            }
        });
    }

    public void displayData() {
        if(sortBox.getSelectedItem().toString().equals("Date Created")) {
            try {indexList = connection2DB.getIndexCardsDateCreated();}
            catch (SQLException ex) {throw new RuntimeException(ex);}
        }

        else if(sortBox.getSelectedItem().toString().equals("Date Updated")) {
            try {indexList = connection2DB.getIndexCardsDateUpdated();}
            catch (SQLException ex) {throw new RuntimeException(ex);}
        }

        else {
            try {indexList = connection2DB.getIndexCardsAlphabetical();}
            catch (SQLException ex) {throw new RuntimeException(ex);}
        }

        indexTableModel = new IndexTableModel(indexList);
        dataTable.setModel(indexTableModel);
        dataTable.setVisible(true);
    }

    //private class to create table model that then displays in data table
    public static class IndexTableModel extends AbstractTableModel {

        private final String[] columnNames = {"ID", "Header", "Body", "Date Created", "Date Updated"};

        private IndexTableModel(ArrayList<Card> data) {
            indexList = data;
        }

        @Override
        public int getRowCount() {
            return indexList.size();
        }

        @Override
        public int getColumnCount() {
            return 5;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            String data = null;

            switch (columnIndex) {
                case 0 -> data = String.valueOf(indexList.get(rowIndex).getID());
                case 1 -> data = indexList.get(rowIndex).getHeader();
                case 2 -> data = indexList.get(rowIndex).getBody();
                case 3 -> data = String.valueOf(indexList.get(rowIndex).getDateCreated());
                case 4 -> data = String.valueOf(indexList.get(rowIndex).getDateUpdated());
            }

            return data;
        }

        @Override
        public String getColumnName(int column) {return columnNames[column];}

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return super.getColumnClass(columnIndex);
        }

        public void updateData(String selected) {
            if(selected.equals("Date Created")) {
                try {indexList = connection2DB.getIndexCardsDateCreated();}
                catch (SQLException ex) {throw new RuntimeException(ex);}
            }

        else if(selected.equals("Date Updated")) {
                try {indexList = connection2DB.getIndexCardsDateUpdated();}
                catch (SQLException ex) {throw new RuntimeException(ex);}
            }

            else {
                try {indexList = connection2DB.getIndexCardsAlphabetical();}
                catch (SQLException ex) {throw new RuntimeException(ex);}
            }

            indexTableModel = new IndexTableModel(indexList);
        }
    }
}
