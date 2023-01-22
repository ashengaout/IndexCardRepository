import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Java2SQL {
    private Connection connection;

    public Java2SQL() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1434;encrypt=true;trustServerCertificate=true;databaseName=IndexCards;user=aShengaout1;password=abc");
    }

    //this method obtains the listID based off the listName
    public int getListID(String listName) throws SQLException {
        String getID = "SELECT listID FROM Lists WHERE listName = ?";
        int ID = 0;

        PreparedStatement preparedStatement = connection.prepareStatement(getID);
        preparedStatement.setString(1, listName);
        ResultSet resultSet = preparedStatement.executeQuery();

       while(resultSet.next()) {
           ID = resultSet.getInt(1);
       }
       
       return ID;
    }

    //this method returns the list of lists from the DB to populate the combo box with
    public ArrayList getLists() throws SQLException {
        ArrayList <String> listList = new ArrayList<>();
        String getLists = "SELECT * FROM Lists";

        PreparedStatement preparedStatement = connection.prepareStatement(getLists);
        ResultSet resultSet = preparedStatement.executeQuery();

        while(resultSet.next()) {
            String name = resultSet.getString(2);

            listList.add(name);
        }

        return listList;
    }

    //this method creates a new list in the DB
    public void createList(List list) throws SQLException {
        if(list.getID() == 0) {
            String createList = "INSERT INTO Lists (ListName) VALUES (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(createList);

            preparedStatement.setString(1, list.getListName());
            preparedStatement.execute();
        }
    }

    //this method deletes list from the DB
    public void deleteList(int ID) throws SQLException {
        String deleteStatement = "DELETE FROM Lists WHERE ListID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(deleteStatement);

        preparedStatement.setInt(1, ID);
        preparedStatement.execute();
    }

    //Method creates an arrayList, obtains a resultSet and then the information in the resultSet is used to create a card object and place it into the list
    public ArrayList getIndexCardsAlphabetical(int selected) throws SQLException {
        ArrayList <Card> cardList = new ArrayList<>();
        String getCards = "SELECT * FROM IndexData WHERE ListID = ? ORDER BY Header;";

       PreparedStatement preparedStatement = connection.prepareStatement(getCards);
       preparedStatement.setInt(1, selected);
       ResultSet resultSet = preparedStatement.executeQuery();

        while(resultSet.next()) {
            int ID = resultSet.getInt(1);
            String header = resultSet.getString(2);
            String body = resultSet.getString(3);
            Date created = (Date) resultSet.getObject(4);
            Date updated = (Date) resultSet.getObject(5);

            Card card = new Card(ID, header, body, created.toLocalDate(), updated.toLocalDate());
            cardList.add(card);
        }

        return cardList;
    }

    //this executes an SQL query that creates a new record using a card object made in card dialog GUI
    //use prepared statement and use setObject on all values in card
    //get ID out of result set
    public void createCards(Card card) throws SQLException {
        String createStatement = "INSERT INTO IndexData (Header, Body, DateCreated, DateUpdated, ListID) VALUES (? ,?, ?, ?, ?)";
        if(card.getID() == 0 & card.isUpdated)   {
            PreparedStatement statement = connection.prepareStatement(createStatement);
            statement.setString(1, card.getHeader());
            statement.setString(2, card.getBody());
            statement.setObject(3, Date.valueOf(LocalDate.now()));
            statement.setObject(4, Date.valueOf(LocalDate.now()));
            statement.setObject(5, card.getListID());
            statement.execute();
        }
    }

    //this executes an SQL query that updates records based off card edits made in card dialog GUI
    public void updateCards(Card card) throws SQLException {
        String updateStatement = "UPDATE IndexData SET Header = ?, Body = ?, DateUpdated = ? WHERE ID = "+card.getID();

        if(card.isUpdated) {
            PreparedStatement preparedStatement = connection.prepareStatement(updateStatement);
            preparedStatement.setString(1, card.getHeader());
            preparedStatement.setString(2, card.getBody());
            preparedStatement.setObject(3, Date.valueOf(LocalDate.now()));
            preparedStatement.execute();
        }
    }

    //this executes an SQL query that deletes card from the DB
    public void deleteCards(Card card) throws SQLException {
        String deleteStatement = "DELETE FROM IndexData WHERE ID = "+card.getID();

        if(card.isUpdated) {
            PreparedStatement preparedStatement = connection.prepareStatement(deleteStatement);
            preparedStatement.execute();
        }
    }
}
