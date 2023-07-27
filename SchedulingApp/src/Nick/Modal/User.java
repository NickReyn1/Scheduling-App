package Nick.Modal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class User {
    private int User_ID;
    private String User_Name;
    private String Password;
    private ObservableList<Appointment> userAppointments = FXCollections.observableArrayList();

    /**
     * Constructor for the User class
     * @param user_id
     * @param user_name
     * @param password
     */
    public User(int user_id, String user_name, String password){

        this.User_ID = user_id;
        this.User_Name = user_name;
        this.Password = password;
    }

    /**
     * getter method
     * @return user id
     */
    public int getUser_ID() {
        return this.User_ID;
    }
    /**
     * setter method
     * @param user_ID
     */
    public void setUser_ID(int user_ID) {
        this.User_ID = user_ID;
    }
    /**
     * getter method
     * @return user name
     */
    public String getUser_Name() {
        return this.User_Name;
    }
    /**
     * setter method
     * @param user_Name
     */
    public void setUser_Name(String user_Name) {
        this.User_Name = user_Name;
    }
    /**
     * getter method
     * @return password
     */
    public String getPassword() {
        return this.Password;
    }
    /**
     * setter method
     * @param password
     */
    public void setPassword(String password) {
        this.Password = password;
    }
    /**
     * getter method
     * @return list of all appointments for this user
     */
    public ObservableList<Appointment> getUserAppointments(){
        return this.userAppointments;
    }
    /**
     * method that adds an appointment to this users list of appointments
     * @param app
     */
    public void addAppointment(Appointment app){
        userAppointments.add(app);
    }


}
