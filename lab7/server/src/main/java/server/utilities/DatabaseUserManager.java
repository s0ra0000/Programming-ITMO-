package server.utilities;

import common.interaction.ResponseCode;
import common.interaction.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.TreeMap;

public class DatabaseUserManager {
    public final Logger LOG
            = LoggerFactory.getLogger(DatabaseUserManager.class);

    private final String SELECT_USER_BY_ID = "SELECT * FROM " + DatabaseHandler.USER_TABLE +
            " WHERE " + DatabaseHandler.USER_TABLE_ID + " = ?";
    private final String SELECT_USER_BY_USERNAME = "SELECT * FROM " + DatabaseHandler.USER_TABLE +
            " WHERE " + DatabaseHandler.USER_TABLE_USERNAME + " = ?";
    private final String SELECT_USER_BY_USERNAME_AND_PASSWORD = SELECT_USER_BY_USERNAME + " AND " +
            DatabaseHandler.USER_TABLE_PASSWORD + " = ?";

    private final String INSERT_USER = "INSERT INTO " +
            DatabaseHandler.USER_TABLE + " (" +
            DatabaseHandler.USER_TABLE_USERNAME + ", " +
            DatabaseHandler.USER_TABLE_PASSWORD + ") VALUES (?, ?)";

    public static ResponseCode responseCode;
    private DatabaseHandler databaseHandler;
    private HashMap<String, String> userDatabase = new HashMap<>();

    public DatabaseUserManager(DatabaseHandler databaseHandler){
        this.databaseHandler = databaseHandler;
    }

    public User getUserID(int id) throws SQLException {
        User user;
        PreparedStatement preparedStatementUserByID = null;
        try{
            preparedStatementUserByID = databaseHandler.getPreparedStatement(SELECT_USER_BY_ID,false);
            preparedStatementUserByID.setInt(1,id);
            ResultSet resultSet = preparedStatementUserByID.executeQuery();
            LOG.info("Выполнен запрос SELECT_ALL_COORDINATES_BY_ID.");
            if(resultSet.next()) {
                user = new User(
                        resultSet.getString(DatabaseHandler.USER_TABLE_USERNAME),
                        resultSet.getString(DatabaseHandler.USER_TABLE_PASSWORD)
                );
            } else throw new SQLException();
        } catch (SQLException exception) {
            LOG.info("Выполнен запрос SELECT_ALL_COORDINATES_BY_ID.");
            throw new SQLException(exception);
        }finally {
            databaseHandler.closePreparedStatement(preparedStatementUserByID);
        }
        return user;
    }

    public boolean checkUserByUsernameAndPassword(User user) throws Exception {
        PreparedStatement preparedSelectUserByUsernameAndPasswordStatement = null;
        try {
            preparedSelectUserByUsernameAndPasswordStatement =
                    databaseHandler.getPreparedStatement(SELECT_USER_BY_USERNAME_AND_PASSWORD, false);
            preparedSelectUserByUsernameAndPasswordStatement.setString(1, user.getUsername());
            preparedSelectUserByUsernameAndPasswordStatement.setString(2, user.getPassword());
            ResultSet resultSet = preparedSelectUserByUsernameAndPasswordStatement.executeQuery();
            LOG.info("Выполнен запрос SELECT_USER_BY_USERNAME_AND_PASSWORD.");
            return resultSet.next();
        } catch (SQLException exception) {
            LOG.error("Произошла ошибка при выполнении запроса SELECT_USER_BY_USERNAME_AND_PASSWORD!");
            throw new Exception();
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectUserByUsernameAndPasswordStatement);
        }
    }

    public long getUserIdByUsername(User user) throws Exception {
        long userId;
        PreparedStatement preparedSelectUserByUsernameStatement = null;
        try {
            preparedSelectUserByUsernameStatement =
                    databaseHandler.getPreparedStatement(SELECT_USER_BY_USERNAME, false);
            preparedSelectUserByUsernameStatement.setString(1, user.getUsername());
            ResultSet resultSet = preparedSelectUserByUsernameStatement.executeQuery();
            LOG.info("Выполнен запрос SELECT_USER_BY_USERNAME.");
            if (resultSet.next()) {
                userId = resultSet.getLong(DatabaseHandler.USER_TABLE_ID);
            } else userId = -1;
            return userId;
        } catch (SQLException exception) {
            LOG.error("Произошла ошибка при выполнении запроса SELECT_USER_BY_USERNAME!");
            throw new Exception();
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectUserByUsernameStatement);
        }
    }

    public boolean insertUser(User user) throws Exception {
        PreparedStatement preparedInsertUserStatement = null;
        try {
            if (getUserIdByUsername(user) != -1) return false;
            preparedInsertUserStatement =
                    databaseHandler.getPreparedStatement(INSERT_USER, false);
            preparedInsertUserStatement.setString(1, user.getUsername());
            preparedInsertUserStatement.setString(2, user.getPassword());
            if (preparedInsertUserStatement.executeUpdate() == 0) throw new SQLException();
            LOG.info("Выполнен запрос INSERT_USER.");
            return true;
        } catch (SQLException exception) {
            LOG.error("Произошла ошибка при выполнении запроса INSERT_USER!");
            throw new Exception();
        } finally {
            databaseHandler.closePreparedStatement(preparedInsertUserStatement);
        }
    }


    public void registerNewUser(String username, String password){
        userDatabase.put(username,password);
        System.out.println(userDatabase.get(username));
    }
    public boolean checkExistUser(String username){
        return userDatabase.containsKey(username);
    }
  public void insertUser(){
        userDatabase.put("admin","admin");
        userDatabase.put("user","user");
    }


    public boolean checkUser(User user){
//        System.out.println("username: "+user.getUsername());
//        System.out.println("password: "+user.getPassword());
//        System.out.println("database: " + userDatabase.get(user.getUsername()));
        return userDatabase.get(user.getUsername()).equals(user.getPassword());
    }
}
