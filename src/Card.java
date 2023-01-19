import java.time.LocalDate;

public class Card {
    private int ID;
    private String header;
    private String body;
    private LocalDate dateCreated;
    private LocalDate dateUpdated;
    boolean isUpdated;

    Card() {}

    Card(String headerInput, String bodyInput) {
        ID = 0;
        header = headerInput;
        body = bodyInput;
        dateCreated = LocalDate.now();
        dateUpdated = LocalDate.now();
        isUpdated = false;
    }

    Card(int cardID, String headerInput, String bodyInput, LocalDate created, LocalDate updated) {
        ID = cardID;
        header = headerInput;
        body = bodyInput;
        dateCreated = created;
        dateUpdated = updated;
        isUpdated = false;
    }

    public int getID() {return ID;}

    public String getHeader() {return header;}

    public String getBody() {return body;}

    public LocalDate getDateUpdated() {return dateUpdated;}

    public LocalDate getDateCreated() {return dateCreated;}

    public void setHeader(String newHeader) {header = newHeader;}

    public void setBody(String newBody) {body = newBody;}

    public void setDateUpdated() {dateUpdated = LocalDate.now();}

    public void setDateCreated() {this.dateCreated = LocalDate.now();}

    public void setUpdated(boolean x) {isUpdated = x;}
}
