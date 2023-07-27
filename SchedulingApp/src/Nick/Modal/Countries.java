package Nick.Modal;

import java.sql.Date;

public class Countries {

    private int countryID;
    private String countryName;

    /**
     * constructor for countries class
     */
    public Countries(int Country_ID, String Country) {
        this.countryID = Country_ID;
        this.countryName = Country;

    }

    public int getCountryID(){
        return this.countryID;
    }
    public String getCountryName(){
        return this.countryName;
    }

}
