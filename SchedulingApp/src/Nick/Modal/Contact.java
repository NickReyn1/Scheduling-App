package Nick.Modal;

public class Contact {
    private int Contact_ID;
    private String Contact_Name;
    private String Email;

    /**
     * Constructor for the Contact class
     */
    public Contact(int Contact_ID, String Contact_Name, String Email){
        this.Contact_ID = Contact_ID;
        this.Contact_Name = Contact_Name;
        this.Email = Email;
    }

    /**
     * getter method
     * @return contact ID
     */
    public int getContact_ID(){
        return this.Contact_ID;
    }
    /**
     * getter method
     * @return contact name
     */
    public String getContact_Name(){
        return this.Contact_Name;
    }
    /**
     * getter method
     * @return contact email
     */
    public String getEmail(){
        return this.Email;
    }
    /**
     * setter method
     * @param newID new id for contact
     */
    public void setContact_ID(int newID){
        this.Contact_ID = newID;
    }
    /**
     * setter method
     * @param newName new name for contact
     */
    public void setContact_Name(String newName){
        this.Contact_Name = newName;
    }
    /**
     * setter method
     * @param newEmail new email for contact
     */
    public void setEmail(String newEmail){
       this.Email = newEmail;
    }
}
