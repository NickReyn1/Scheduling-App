package Nick.Modal;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private int Appointment_ID;
    private String Title;
    private String Description;
    private String Location;
    private String Type;
    private LocalDate StartDate;
    private LocalDate EndDate;
    private LocalTime StartTime;
    private LocalTime EndTime;
    private int Customer_ID;
    private int User_ID;
    private int Contact_ID;

    /**
     * Constructor for the Appointment class
     * @param appointment_id
     * @param title
     * @param description
     * @param location
     * @param type
     * @param startDate
     * @param endDate
     * @param customer_id
     * @param user_id
     * @param contact_id
     */
    public Appointment(int appointment_id, String title, String description, String location, String type, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, int customer_id, int user_id, int contact_id){
        this.Appointment_ID = appointment_id;
        this.Title = title;
        this.Description = description;
        this.Location = location;
        this.Type = type;
        this.StartDate = startDate;
        this.EndDate = endDate;
        this.StartTime = startTime;
        this.EndTime = endTime;
        this.Customer_ID = customer_id;
        this.User_ID = user_id;
        this.Contact_ID = contact_id;
        //System.out.println(this.Start + " /" + start);
    }

    /**
     * getter method
     * @return Appointment ID
     */
    public int getAppointment_ID() {
        return this.Appointment_ID;
    }
    /**
     * setter method
     * @param appointment_ID
     */
    public void setAppointment_ID(int appointment_ID) {
        this.Appointment_ID = appointment_ID;
    }
    /**
     * getter method
     * @return Appointment Title
     */
    public String getTitle() {
        return this.Title;
    }
    /**
     * setter method
     * @param title
     */
    public void setTitle(String title) {
        this.Title = title;
    }
    /**
     * getter method
     * @return Appointment Description
     */
    public String getDescription() {
        return this.Description;
    }
    /**
     * setter method
     * @param description
     */
    public void setDescription(String description) {
        this.Description = description;
    }
    /**
     * getter method
     * @return Appointment Location
     */
    public String getLocation() {
        return this.Location;
    }
    /**
     * setter method
     * @param location
     */
    public void setLocation(String location) {
        this.Location = location;
    }
    /**
     * getter method
     * @return Appointment Type
     */
    public String getType() {
        return this.Type;
    }
    /**
     * setter method
     * @param type
     */
    public void setType(String type) {
        this.Type = type;
    }
    /**
     * getter method
     * @return Appointment Start time
     */
    public LocalTime getStartTime() {
        return this.StartTime;
    }
    /**
     * setter method
     * @param start
     */
    public void setStartTime(LocalTime start) {
        this.StartTime = start;
    }
    /**
     * getter method
     * @return Appointment End time
     */
    public LocalTime getEndTime() {
        return this.EndTime;
    }
    /**
     * setter method
     * @param end
     */
    public void setEndTime(LocalTime end) {
        this.EndTime = end;
    }
    /**
     * getter method
     * @return Appointment Start time
     */
    public LocalDate getStartDate() {
        return this.StartDate;
    }
    /**
     * setter method
     * @param start
     */
    public void setStartDate(LocalDate start) {
        this.StartDate = start;
    }
    /**
     * getter method
     * @return Appointment End time
     */
    public LocalDate getEndDate() {
        return this.EndDate;
    }
    /**
     * setter method
     * @param end
     */
    public void setEndDate(LocalDate end) {
        this.EndDate = end;
    }
    /**
     * getter method
     * @return Customer_ID
     */
    public int getCustomer_ID() {
        return this.Customer_ID;
    }
    /**
     * setter method
     * @param customer_ID
     */
    public void setCustomer_ID(int customer_ID) {
        this.Customer_ID = customer_ID;
    }
    /**
     * getter method
     * @return User_ID
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
     * @return Contact_ID
     */
    public int getContact_ID() {
        return this.Contact_ID;
    }
    /**
     * setter method
     * @param contact_ID
     */
    public void setContact_ID(int contact_ID) {
        this.Contact_ID = contact_ID;
    }



}
