package Nick.Modal;

import Helper.DBHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Customer {
    private String customerId;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;
    private int divisionId;
    private int countryId;
    private ObservableList<Appointment> customerAppointments = FXCollections.observableArrayList();

    /**
     * Constructor for the Customer class
     * @param customer_id
     * @param customer_name
     * @param address
     * @param postal_code
     * @param phone
     */
    public Customer(int customer_id, String customer_name, String address, String postal_code, String phone, int division_ID){

        this.customerId = String.valueOf(customer_id);
        this.customerName = customer_name;
        this.address = address;
        this.postalCode = postal_code;
        this.phone = phone;
        this.divisionId = division_ID;
        for (Divisions div: DBHelper.getDivisions()) {
            if(this.divisionId == div.getDivisionID()){
                this.countryId = div.getCountryID();
                break;
            }
        }
    }

    /**
     * getter method
     * @return customer id
     */
    public String getCustomerId() {
        return this.customerId;
    }
    /**
     * setter method
     * @param customer_ID
     */
    public void setCustomerId(int customer_ID) {
        this.customerId = String.valueOf(customer_ID);
    }
    /**
     * getter method
     * @return customer name
     */
    public String getCustomerName() {
        return this.customerName;
    }
    /**
     * setter method
     * @param customer_Name
     */
    public void setCustomerName(String customer_Name) {
        this.customerName = customer_Name;
    }
    /**
     * getter method
     * @return customer address
     */
    public String getAddress() {
        return this.address;
    }
    /**
     * setter method
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }
    /**
     * getter method
     * @return postal code
     */
    public String getPostalCode() {
        return this.postalCode;
    }
    /**
     * setter method
     * @param postal_Code
     */
    public void setPostalCode(String postal_Code) {
        this.postalCode = postal_Code;
    }
    /**
     * getter method
     * @return phone number
     */
    public String getPhone() {
        return this.phone;
    }
    /**
     * setter method
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
    /**
     * getter method
     * @return Division ID
     */
    public int getDivisionId() {
        return this.divisionId;
    }
    /**
     * setter method
     * @param division_ID
     */
    public void setDivisionId(int division_ID) {
        this.divisionId = division_ID;
    }
    /**
     * getter method
     * @return countryId
     */
    public int getCountryId() {
        return this.countryId;
    }
    /**
     * setter method
     * @param countryId
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
    /**
     * getter method
     * @return division name
     */
    public String getDivisionName(){
        String name = "";
        ObservableList<Divisions> divisions = DBHelper.getDivisions();
        for(Divisions div : divisions){
            if(div.getDivisionID() == this.getDivisionId()){
                name = div.getDivisionName();
                break;
            }
            else{
                name = "No Division Name Found.";
            }
        }
       return name ;
    }
    /**
     * getter method
     * @return countryName
     */
    public String getCountryName(){
        String name = "";
        ObservableList<Countries> countries = DBHelper.getCountries();
        for(Countries count : countries){
            if(count.getCountryID() == this.countryId){
                name = count.getCountryName();
                break;
            }
            else{
                name = "No Country Name Found";
            }
        }
        return name;
    }
    /**
     * getter method
     * @return list of all appointments for this user
     */
    public ObservableList<Appointment> getUserAppointments(){
        return this.customerAppointments;
    }
    /**
     * method that adds an appointment to this customers list of appointments
     * @param app
     */
    public void addAppointment(Appointment app){
        customerAppointments.add(app);
    }



}
