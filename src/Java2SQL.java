import javax.print.attribute.standard.ReferenceUriSchemesSupported;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Java2SQL {
    private Connection connection;

    public Java2SQL() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1434;encrypt=true;trustServerCertificate=true;databaseName=IndexCards;user=aShengaout1;password=abc");
    }

    //the three methods below create an arrayList containing card objects from the DB in varying orders
    //It creates an arrayList, obtains a resultSet and then the information in the resultSet is used to create a card object and place it into the list
    public ArrayList getIndexCardsAlphabetical() throws SQLException {
        ArrayList <Card> cardList = new ArrayList<>();
        String getCards = "SELECT * FROM IndexData ORDER BY Header;";

       PreparedStatement preparedStatement = connection.prepareStatement(getCards);
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

    public ArrayList getIndexCardsDateCreated() throws SQLException {
        ArrayList <Card> cardList = new ArrayList<>();
        String getCards = "SELECT * FROM IndexData ORDER BY DateCreated;";

        PreparedStatement preparedStatement = connection.prepareStatement(getCards);
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

    public ArrayList getIndexCardsDateUpdated() throws SQLException {
        ArrayList <Card> cardList = new ArrayList<>();
        String getCards = "SELECT * FROM IndexData ORDER BY DateUpdated";

        PreparedStatement preparedStatement = connection.prepareStatement(getCards);
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
        String createStatement = "INSERT INTO IndexData (Header, Body, DateCreated, DateUpdated) VALUES ('" + card.getHeader() + "', '" + card.getBody() + "', '" + Date.valueOf(LocalDate.now()) + "', '" + Date.valueOf(LocalDate.now()) + "')";
        if(card.getID() == 0 & card.isUpdated)   {
            PreparedStatement statement = connection.prepareStatement(createStatement);
            statement.execute();
        }
    }

    //this executes an SQL query that updates records based off card edits made in card dialog GUI
    public void updateCards(Card card) throws SQLException {
        String updateStatement = "UPDATE IndexData SET Header = '"+card.getHeader()+"', Body = '"+card.getBody()+"', DateUpdated = '"+Date.valueOf(LocalDate.now())+"' WHERE ID = "+card.getID();

        if(card.isUpdated) {
            PreparedStatement preparedStatement = connection.prepareStatement(updateStatement);
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

    public void closeConnection() throws SQLException {connection.close();}
}
