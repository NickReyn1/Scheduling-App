package Helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class JDBC {

    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbUrl = protocol+vendor+location+databaseName+"?connectionTimeZone = SERVER";
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String userName = "sqlUser";
    private static String password = "Passw0rd!";
    public static Connection databaseConnection;

    /**
     * opens a connection to the mysql database
     */
    public static void openConnection(){

        try{
          Class.forName(driver);
            databaseConnection = DriverManager.getConnection(jdbUrl, userName, password);

        }
        catch(SQLException e){
            System.out.println("Error accessing database: " + e.getMessage());
        }
        catch(ClassNotFoundException e){
            System.out.println("Error accessing database: " + e.getMessage());
        }
    }

    /**
     * closes a connection to the mysql database
     */
    public static void closeConnection(){
        try{
            databaseConnection.close();
            System.out.println("Connection closed.");
        }
        catch(Exception e){
            System.out.println("Error: "+ e.getMessage());
        }
    }
    public static Connection getConnection(){
        return databaseConnection;
    }
}
