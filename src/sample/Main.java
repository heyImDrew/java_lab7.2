package sample;

import java.sql.*;
import java.util.ArrayList;

public class Main {

    public static void minimal_msg(Statement statement) throws SQLException {
        String sqlexec = "SELECT message, name FROM messages_lab.message LEFT JOIN messages_lab.human ON messages_lab.message.msg_from = messages_lab.human.id";
        ResultSet data = statement.executeQuery(sqlexec);
        int minSymbols = 257;
        String minName = "Noone";
        String msg = "Nothing";
        while (data.next()) {
            if (data.getString("message").length() < minSymbols) {
                minSymbols = data.getString("message").length();
                minName = data.getString("name");
                msg = data.getString("message");
            }
        }
        System.out.println("The shortest message: " + msg + ". Send by " + minName + "\n");
    }

    public static void show_all(Statement statement) throws SQLException {
        String sqlexec = "SELECT * FROM messages_lab.human";
        ResultSet data = statement.executeQuery(sqlexec);
        System.out.print("\nPersons:");
        while (data.next()) {
            System.out.print("\nName: " + data.getString("name") + ". Birth: " + data.getString("birth"));
        }
        System.out.println("\n");
    }

    public static void show_with_msg(Statement statement) throws SQLException {
        String sqlexec = "SELECT msg_to FROM messages_lab.message";
        ResultSet data = statement.executeQuery(sqlexec);
        ArrayList<Integer> hm = new ArrayList<Integer>();
        while (data.next()) {
            if(!hm.contains(data.getInt("msg_to"))) {
                hm.add(data.getInt("msg_to"));
            }
        }
        System.out.println("Persons who got messages:");
        for (int i = 0; i < hm.size(); i++) {
            String query = "SELECT * FROM messages_lab.human WHERE id=?;";
            PreparedStatement preparedStmt = statement.getConnection().prepareStatement(query);
            preparedStmt.setInt(1, hm.get(i));
            ResultSet data_1 = preparedStmt.executeQuery();
            while (data_1.next()) {
                System.out.println(data_1.getString("name"));
            }
        }
    }

    public static void show_with_title(Statement statement, String title) throws SQLException {
        String query = "SELECT msg_to FROM messages_lab.message WHERE title=?;";
        PreparedStatement preparedStmt = statement.getConnection().prepareStatement(query);
        preparedStmt.setString(1, title);
        ResultSet data = preparedStmt.executeQuery();
        System.out.println("\nPersons who got message with title '"+ title +"':");
        while (data.next()) {
            String query1 = "SELECT * FROM messages_lab.human WHERE id=?;";
            PreparedStatement preparedStmt1 = statement.getConnection().prepareStatement(query1);
            preparedStmt1.setString(1, data.getString("msg_to"));
            ResultSet data_1 = preparedStmt1.executeQuery();
            while (data_1.next()) {
                System.out.println(data_1.getString("name"));
            }
        }
        System.out.println("\n");
    }

    public static void main(String[] strings) throws ClassNotFoundException, SQLException {
        // GET CONNECTION
        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();
        Statement statement = connection.createStatement();

        // TASKS EXECUTE
        show_all(statement);
        minimal_msg(statement);
        show_with_msg(statement);
        show_with_title(statement, "Second");

        // CLOSE CONNECTION
        connection.close();
    }
}
