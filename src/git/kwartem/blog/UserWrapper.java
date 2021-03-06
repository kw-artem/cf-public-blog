package git.kwartem.blog;

import java.sql.*;
import java.util.HashMap;

public class UserWrapper {

    private Connection connection;

    public UserWrapper(Connection connection){
        this.connection = connection;
    }

    public boolean isUserExist(User user) throws SQLException {

        PreparedStatement pstatement = connection.
                prepareStatement("select * from users where login=?");
        pstatement.setString(1, user.getLogin());
        ResultSet resultSet = pstatement.executeQuery();

        return resultSet.next();
    }

    public boolean submitUserToDB(User user) throws SQLException {

        PreparedStatement pstatement = connection.
                prepareStatement("insert into users (login, password, email) values (?, ?, ?)");
        pstatement.setString(1, user.getLogin());
        pstatement.setString(2, user.getPassword());
        pstatement.setString(3, user.getEmail());
        int updatedRowsCount = pstatement.executeUpdate();

        return updatedRowsCount > 0;
    }

    public boolean toLoginUser(User user) throws SQLException {

        System.out.println("login: "+ user.getLogin()+", password: "+user.getPassword());
        PreparedStatement pstatement = connection.
                prepareStatement("select * from users where login=? and password=?");
        pstatement.setString(1, user.getLogin());
        pstatement.setString(2, user.getPassword());
        ResultSet resultSet = pstatement.executeQuery();

        boolean result = resultSet.next();
        if (result){
            user.setEmail(resultSet.getString("email"));
        }

        return result;
    }

    public void getUserData(User user){

    }

    //to do:
    //rewrite, create a global collection like HashMap/Set all of blog users
    public HashMap<Integer,String> getAllUserLogins() throws SQLException {

        HashMap<Integer,String> user_logins = new HashMap<Integer,String>();

        PreparedStatement pstatement = connection.prepareStatement("select user_id, login from users");
        ResultSet resultSet = pstatement.executeQuery();

        while(resultSet.next()){
            user_logins.put(resultSet.getInt(1), resultSet.getString(2));
        }

        return user_logins;
    }
}
