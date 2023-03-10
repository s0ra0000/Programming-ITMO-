package server.utilities;
import org.postgresql.*;

import java.sql.*;


public class DatabaseHandler {
    public static String STUDY_GROUP_TABLE = "study_group";
    public static String COORDINATE_TABLE = "coordinate";
    public static String FORM_OF_EDUCATION_TABLE = "form_of_education";
    public static String SEMESTER_TABLE = "semester";
    public static String ADMIN_TABLE = "admin";
    public static String COUNTRY_TABLE = "country";
    public static String LOCATION_TABLE = "location";
    public static String USER_TABLE = "user_table";

    public static String STUDY_GROUP_TABLE_ID = "groupid";
    public static String STUDY_GROUP_TABLE_KEY = "key";
    public static String STUDY_GROUP_TABLE_NAME = "name";
    public static String STUDY_GROUP_TABLE_COORDINATE_ID = "coordinateid";
    public static String STUDY_GROUP_TABLE_LOCAL_DATE = "localdate";
    public static String STUDY_GROUP_TABLE_STUDENTS_COUNT = "studentscount";
    public static String STUDY_GROUP_TABLE_TRANSFERRED_STUDENTS = "transferred_students";
    public static String STUDY_GROUP_TABLE_FORM_OF_EDUCATION_ID = "formofeducationid";
    public static String STUDY_GROUP_TABLE_SEMESTER_ID = "semesterid";
    public static String STUDY_GROUP_TABLE_ADMIN_ID = "adminid";
    public static String STUDY_GROUP_TABLE_USER_ID = "userid";

    public static String COORDINATE_TABLE_ID = "coordinateid";
    public static String COORDINATE_TABLE_X = "coordinate_x";
    public static String COORDINATE_TABLE_Y = "coordinate_y";

    public static String FORM_OF_EDUCATION_TABLE_ID = "formofeducationid";
    public static String FORM_OF_EDUCATION_TABLE_NAME = "formofeducationname";

    public static String SEMESTER_TABLE_ID = "semesterid";
    public static String SEMESTER_TABLE_TYPE = "semestertype";

    public static String ADMIN_TABLE_ID = "adminid";
    public static String ADMIN_TABLE_NAME = "adminname";
    public static String ADMIN_TABLE_PASSPORT_ID = "passportid";
    public static String ADMIN_TABLE_COUNTRY_ID = "countryid";
    public static String ADMIN_TABLE_LOCATION_ID = "locationid";

    public static String COUNTRY_TABLE_ID = "countryid";
    public static String COUNTRY_TABLE_NAME = "countryname";

    public static String LOCATION_TABLE_ID = "locationid";
    public static String LOCATION_TABLE_X = "location_x";
    public static String LOCATION_TABLE_Y = "location_y";
    public static String LOCATION_TABLE_Z = "location_z";
    public static String LOCATION_TABLE_NAME = "locationname";

    public static String USER_TABLE_ID = "userid";
    public static String USER_TABLE_USERNAME = "username";
    public static String USER_TABLE_PASSWORD = "password";


    private final String JDBC_DRIVER = "org.postgresql.Driver";

    private String URL;
    private String username;
    private String password;
    private Connection connection;

    public DatabaseHandler(String URL, String username, String password){
        this.URL = URL;
        this.username = username;
        this.password = password;
    }
    public void connectToDatabase(){
        try{
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(URL, username,password);
            System.out.println("Connected to Database!");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Could not connect to Database");
            System.out.println(e);
            System.exit(-1);

        }
    }

    public PreparedStatement getPreparedStatement(String sqlStatement, boolean generateKeys) throws SQLException {
        PreparedStatement preparedStatement;
        try {
            if(connection == null) throw  new SQLException();
            int autoGeneratedKeys = generateKeys ? Statement.RETURN_GENERATED_KEYS: Statement.NO_GENERATED_KEYS;
            preparedStatement = connection.prepareStatement(sqlStatement,autoGeneratedKeys);
            return preparedStatement;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new SQLException();
        }
    }

    public void closePreparedStatement(PreparedStatement sqlStatement){
        if(sqlStatement == null) return;
        try{
            sqlStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void closeConnection(){
        if(connection == null) return;
        try{
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void setCommitMode(){
        try {
            if(connection == null) throw new SQLException();
            connection.setAutoCommit(false);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void setNormalMode(){
        try {
            if(connection == null) throw new SQLException();
            connection.setAutoCommit(true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void commit() {
        try {
            if (connection == null) throw new SQLException();
            connection.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Roll back database status.
     */
    public void rollback() {
        try {
            if (connection == null) throw new SQLException();
            connection.rollback();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Set save point of database.
     */
    public void setSavepoint() {
        try {
            if (connection == null) throw new SQLException();
            connection.setSavepoint();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
