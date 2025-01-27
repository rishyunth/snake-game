import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseHandler {
    public Connection connection;
    public void connectToDatabase(){
        String url = "jdbc:mysql://localhost:3306/snakegame"; // Change to your database URL
        String username = "root"; // Change to your database username
        String password = "rishi2005";

        try {
            connection = DriverManager.getConnection(url, username, password);
                System.out.println("Connected to database");

            } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Failed to connect to database");
        }
    }

    public void insertData(String playerName, int score){
        connectToDatabase();
        if (connection!=null){
            String insertQuery = "INSERT INTO GameData (Name, Score) VALUES (?,?)";

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
               // preparedStatement.setInt(1, Id);
                preparedStatement.setString(1, playerName);
                preparedStatement.setInt(2, score);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0){
                    System.out.println("Data inserted successfully");
                }else
                    System.out.println("Failed to insert data");
            }catch (SQLException e){
                e.printStackTrace();
                System.out.println("Error while handling data");
            }
            finally {
                closeDBConnection();
            }
        }
    }

    public void closeDBConnection(){
        if (connection!=null){
            try {
                connection.close();
                System.out.println("Database connection closed");
            }catch (SQLException e){
                e.printStackTrace();
                System.out.println("Failed to close database connection");
            }
        }
    }
}