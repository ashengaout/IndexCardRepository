import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainGUI  {
    private JPanel mainGUI;
    private JTable dataTable;
    private JButton newCardButton;
    private JButton deleteCardButton;
    private JButton updateCardButton;
    private JComboBox listComboBox;
    private JButton newListButton;
    private JButton deleteListButton;
    private static Java2SQL connection2DB;
    private static ArrayList <Card> indexList;
    private static IndexTableModel indexTableModel;
    private static JComboBoxModel comboBoxModel;
    private static int listID;
    private static ArrayList listsDB;

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

            this.addCreateActionListener();
            this.addUpdateActionListener();
            this.addDeleteActionListener();
            this.displayData();
            this.createComboList();
            this.addListBoxActionListener();
            this.addNewListActionListener();
            this.addDeleteListListener();
            dataTable.setAutoCreateRowSorter(true);
            comboBoxModel.setSelectedItem(listsDB.get(0));
            listID = connection2DB.getListID(listComboBox.getSelectedItem().toString());
        }

        catch (SQLException x) {x.printStackTrace();}
    }

    //creates a card dialog object and opens that GUI and creates a new default card that is passed as a parameter
    //If save button is clicked then the card isUpdated and the fields are updated and passed into the DB (in Java2SQL class)
    public void addCreateActionListener() {
        newCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Card currentCard = new Card();
                CardDialogGUI cardDialogGUI = new CardDialogGUI(currentCard, connection2DB, indexTableModel, listID);
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
                    CardDialogGUI cardDialogGUI = new CardDialogGUI(currentCard, connection2DB, indexTableModel);
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
                    DeletePopUp deletePopUp = new DeletePopUp(currentCard, connection2DB, indexTableModel);
                    deletePopUp.createWindow();
                }
                else {
                    ErrorPopUP errorPopUP = new ErrorPopUP();
                    errorPopUP.createWindow();
                }
            }
        });
    }

    //this action listener displays data from each list when different lists are selected
    public void addListBoxActionListener() {
        listComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedList = listComboBox.getSelectedItem().toString();

                try {listID = connection2DB.getListID(selectedList);}
                catch (SQLException ex) {throw new RuntimeException(ex);}

                indexTableModel.updateData();
                indexTableModel.fireTableDataChanged();
            }
        });
    }

    //this method should create a new list when the create new list button is clicked
    public void addNewListActionListener() {
        newListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewListGUI newList = new NewListGUI(connection2DB, comboBoxModel);
                newList.createWindow();
            }
        });
    }

    //this method deletes the selected list
    public void addDeleteListListener() {
        deleteListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(listsDB.size() > 0 & indexList.size() == 0) {
                    DeleteListGUI deleteListGUI = new DeleteListGUI(connection2DB, listID, comboBoxModel, indexList);
                    deleteListGUI.createWindow();
                }

                else if(indexList.size() > 0) {
                    IndexDeletionError indexDeletionError = new IndexDeletionError();
                    indexDeletionError.createWindow();
                }

                else {
                    ErrorPopUP errorPopUP = new ErrorPopUP();
                    errorPopUP.createWindow();
                }
            }
        });
    }


    //sets combo model to the combo box object during the initial runtime
    public void createComboList() throws SQLException {
        listsDB = connection2DB.getLists();

        comboBoxModel = new JComboBoxModel(listsDB);
        comboBoxModel.setSelectedItem(listsDB.get(0));
        listComboBox.setModel(comboBoxModel);
    }

    //this method does the initial display of data when the main GUI pops up
    public void displayData() {
        try {indexList = connection2DB.getIndexCardsAlphabetical(1);}
        catch (SQLException ex) {throw new RuntimeException(ex);}

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
            return columnNames.length;
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

        public void updateData() {
                try {indexList = connection2DB.getIndexCardsAlphabetical(listID);}
                catch (SQLException ex) {throw new RuntimeException(ex);}
        }
    }

    //private class to create the combo box display list
    public static class JComboBoxModel extends DefaultComboBoxModel {
        private JComboBoxModel(ArrayList lists) {
            listsDB = lists;
        }

        @Override
        public int getSize() {
            return  listsDB.size();
        }

        @Override
        public Object getElementAt(int index) {
            return listsDB.get(index);
        }

        public void updateLists() throws SQLException {
            listsDB = connection2DB.getLists();
        }
    }
}
