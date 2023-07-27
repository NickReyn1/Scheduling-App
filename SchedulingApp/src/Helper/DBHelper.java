package Helper;

import Nick.Modal.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;



import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * DBHelper class that contains callable methods that access the database tables.
 */
public abstract class DBHelper {

    /**
     * Method that accesses the customer table in the database and turns each entry into Customer objects
     * @return ObservableList containing Customer objects
     */
    public static ObservableList<Customer> getCustomers(){
        ObservableList<Customer> custList = FXCollections.observableArrayList();
        try{
            String sql = "SELECT * from customers";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                 int Customer_ID = rs.getInt("Customer_ID");
                 String Customer_Name = rs.getString("Customer_Name");
                 String Address = rs.getString("Address");
                 String Postal_Code = rs.getString("Postal_Code");
                 String Phone = rs.getString("Phone");
                 int Division_ID = rs.getInt("Division_ID");
                 Customer cust = new Customer(Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID);
                 custList.add(cust);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return custList;
    }

    /**
     * Method that accesses the appointment table in the database and turns each entry into appointment objects.
     * @return ObservableList containing Appointment objects
     */
    public static ObservableList<Appointment> getAppointments(){
        ObservableList<Appointment> appList = FXCollections.observableArrayList();
        try{
            String sql = "SELECT * from appointments";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int Appointment_ID = rs.getInt("Appointment_ID");
                String Title = rs.getString("Title");
                String Description = rs.getString("Description");
                String Location = rs.getString("Location");
                String Type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                LocalDate StartDate = start.toLocalDate();
                LocalTime StartTime = start.toLocalTime();
                LocalDate EndDate = end.toLocalDate();
                LocalTime EndTime = end.toLocalTime();
                int Customer_ID = rs.getInt("Customer_ID");
                int User_ID = rs.getInt("User_ID");
                int Contact_ID = rs.getInt("Contact_ID");
                Appointment cust = new Appointment(Appointment_ID, Title, Description, Location, Type, StartDate, StartTime, EndDate, EndTime, Customer_ID, User_ID, Contact_ID);
                //System.out.println(cust.getStart() + " /" + Start + " /" + rs.getTime("Start"));
                appList.add(cust);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return appList;
    }

    /**
     * Method that accesses the divisions table in the database and turns each entry into division objects.
     * @return ObservableList containing Division objects
     */
    public static ObservableList<Divisions> getDivisions(){
        ObservableList<Divisions> divList = FXCollections.observableArrayList();
        try{
            String sql = "SELECT * from first_level_divisions";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int divisionID = rs.getInt("Division_ID");
                String divisionName = rs.getString("Division");
                int countryID = rs.getInt("Country_ID");
                Divisions div = new Divisions(divisionID, divisionName, countryID);
                divList.add(div);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return divList;
    }

    /**
     * Method that accesses the countries table in the database and turns each entry into country objects
     * @return ObservableList containing Countries objects
     */
    public static ObservableList<Countries> getCountries(){
        ObservableList<Countries> countList = FXCollections.observableArrayList();
        try{
            String sql = "SELECT * from countries";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int countryID = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");

                Countries count = new Countries(countryID, countryName);
                countList.add(count);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return countList;
    }

    /**
     * Method that accesses the contact table in the database and turns each entry into Contact objects
     * @return ObservableList containing Contact objects
     */
    public static ObservableList<Contact> getContacts(){
        ObservableList<Contact> contList = FXCollections.observableArrayList();
        try{
            String sql = "SELECT * from contacts";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int contactID = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String contactEmail = rs.getString("Email");

                Contact cont = new Contact(contactID, contactName, contactEmail);
                contList.add(cont);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return contList;
    }

    /**
     * Method that accesses the user table in the database and turns each entry into User objects
     * @return ObservableList that contains User objects
     */
    public static ObservableList<User> getUsers(){
        ObservableList<User> userList = FXCollections.observableArrayList();
        try{
            String sql = "SELECT * from users";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int userID = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String userPassword = rs.getString("Password");

                User user = new User(userID, userName, userPassword);
                userList.add(user);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return userList;
    }


}
