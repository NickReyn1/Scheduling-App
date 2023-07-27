package Nick.Modal;

import java.sql.Date;

public class Divisions {
    private int divisionID;
    private String divisionName;
    private Date createDate;
    private int countryID;
    /**
     * Constructor for divisions class
     */
    public Divisions(int Division_ID, String Division, int Country_ID){
        this.divisionID = Division_ID;
        this.divisionName = Division;
        this.countryID = Country_ID;

    }
    public int getDivisionID(){
        return this.divisionID;
    }
    public int getCountryID(){
        return this.countryID;
    }
    public String getDivisionName(){
        return this.divisionName;
    }
}
