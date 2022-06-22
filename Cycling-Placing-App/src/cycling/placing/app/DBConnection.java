package cycling.placing.app;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public Connection databaseLink;
    
    public Connection getConnection(){
        String dataBaseName = "cyclingapp";
        String dataBaseUser = "root";
        String dataBasePassword = "";
        String url= "jdbc:mysql://localhost/"+dataBaseName;
        
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, dataBaseUser, dataBasePassword);
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
        
        return databaseLink;
    }
}
