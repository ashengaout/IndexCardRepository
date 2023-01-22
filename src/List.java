public class List {
    private int ID;
    private String listName;

    public List(String name) {
        listName = name;
        ID = 0;
    }

    public List(int identity, String name) {
        ID = identity;
        listName = name;
    }

    public int getID() {return ID;}

    public String getListName() {return listName;}
}
