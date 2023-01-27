package server.utilities;

import common.datas.*;
import common.exceptions.DatabaseHandlingException;
import common.interaction.StudyGroupRaw;
import common.interaction.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.Server;
import server.customCollection.CustomTreeMap;

import javax.xml.crypto.Data;
import javax.xml.transform.Result;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class DatabaseCollectionManager {
    public final Logger LOG
            = LoggerFactory.getLogger(DatabaseCollectionManager.class);
    private final String SELECT_ALL_STUDY_GROUP = "SELECT * FROM " + DatabaseHandler.STUDY_GROUP_TABLE;
    private final String SELECT_STUDY_GROUP_BY_ID = SELECT_ALL_STUDY_GROUP + " WHERE " +
            DatabaseHandler.STUDY_GROUP_TABLE_ID;
    private final String SELECT_STUDY_GROUP_BY_ID_AND_USER_ID = SELECT_STUDY_GROUP_BY_ID + " AND " +
            DatabaseHandler.STUDY_GROUP_TABLE_USER_ID;
    private final String INSERT_STUDY_GROUP = "INSERT INTO " +
            DatabaseHandler.STUDY_GROUP_TABLE + " (" +
            DatabaseHandler.STUDY_GROUP_TABLE_KEY + ", " +
            DatabaseHandler.STUDY_GROUP_TABLE_NAME + ", " +
            DatabaseHandler.STUDY_GROUP_TABLE_COORDINATE_ID + ", " +
            DatabaseHandler.STUDY_GROUP_TABLE_LOCAL_DATE + ", " +
            DatabaseHandler.STUDY_GROUP_TABLE_STUDENTS_COUNT + ", " +
            DatabaseHandler.STUDY_GROUP_TABLE_TRANSFERRED_STUDENTS + ", " +
            DatabaseHandler.STUDY_GROUP_TABLE_FORM_OF_EDUCATION_ID + ", " +
            DatabaseHandler.STUDY_GROUP_TABLE_SEMESTER_ID + ", " +
            DatabaseHandler.STUDY_GROUP_TABLE_ADMIN_ID + ", " +
            DatabaseHandler.STUDY_GROUP_TABLE_USER_ID + ") " +
            "VALUES (?,?,?,?,?,?,?,?,?,?)";
    private final String UPDATE_STUDY_GROUP = "UPDATE " + DatabaseHandler.STUDY_GROUP_TABLE +
            " SET " +
            DatabaseHandler.STUDY_GROUP_TABLE_NAME + " = ?, " +
            DatabaseHandler.STUDY_GROUP_TABLE_LOCAL_DATE + " = ?, " +
            DatabaseHandler.STUDY_GROUP_TABLE_STUDENTS_COUNT + " = ?, " +
            DatabaseHandler.STUDY_GROUP_TABLE_TRANSFERRED_STUDENTS + " = ?, " +
            DatabaseHandler.STUDY_GROUP_TABLE_FORM_OF_EDUCATION_ID + " = ?, " +
            DatabaseHandler.STUDY_GROUP_TABLE_SEMESTER_ID + " = ? " +
            " WHERE " + DatabaseHandler.STUDY_GROUP_TABLE_ID + " = ? ";
    private final String DELETE_STUDY_GROUP = "DELETE FROM " + DatabaseHandler.STUDY_GROUP_TABLE +
            " WHERE " + DatabaseHandler.STUDY_GROUP_TABLE_KEY + " = ? ";

    private final String SELECT_ALL_COORDINATES = "SELECT * FROM " + DatabaseHandler.COORDINATE_TABLE;
    private final String SELECT_ALL_COORDINATES_BY_ID = SELECT_ALL_COORDINATES + " WHERE " +
            DatabaseHandler.COORDINATE_TABLE_ID + " = ?";
    private final String INSERT_COORDINATE = "INSERT INTO " +
            DatabaseHandler.COORDINATE_TABLE + " (" +
            DatabaseHandler.COORDINATE_TABLE_X + ", " +
            DatabaseHandler.COORDINATE_TABLE_Y + ") " +
            "VALUES (?,?)";
    private final String UPDATE_COORDINATE = "UPDATE " + DatabaseHandler.COORDINATE_TABLE +
            " SET " +
            DatabaseHandler.COORDINATE_TABLE_X + " = ?, " +
            DatabaseHandler.COORDINATE_TABLE_Y + " = ?" +
            " WHERE " + DatabaseHandler.COORDINATE_TABLE_ID + " = ?";

    private final String SELECT_ALL_FORM_OF_EDUCATION = "SELECT * FROM " + DatabaseHandler.FORM_OF_EDUCATION_TABLE;
    private final String SELECT_ALL_FORM_OF_EDUCATIO_BY_ID = SELECT_ALL_FORM_OF_EDUCATION + " WHERE " +
            DatabaseHandler.FORM_OF_EDUCATION_TABLE_ID + " = ?";

    private final String SELECT_ALL_SEMESTER = "SELECT * FROM " + DatabaseHandler.SEMESTER_TABLE;
    private final String SELECT_ALL_SEMESTER_BY_ID = SELECT_ALL_SEMESTER + " WHERE " +
            DatabaseHandler.SEMESTER_TABLE_ID + " = ?";

    private final String SELECT_ALL_COUNTRY = "SELECT * FROM " + DatabaseHandler.COUNTRY_TABLE;
    private final String SELECT_ALL_COUNTRY_BY_ID = SELECT_ALL_COUNTRY + " WHERE " +
            DatabaseHandler.COUNTRY_TABLE_ID + " = ?";

    private final String SELECT_ALL_LOCATION = "SELECT * FROM " + DatabaseHandler.LOCATION_TABLE;
    private final String SELECT_ALL_LOCATION_BY_ID = SELECT_ALL_LOCATION + " WHERE " +
            DatabaseHandler.LOCATION_TABLE_ID + " = ?";
    private final String INSERT_LOCATION = "INSERT INTO " +
            DatabaseHandler.LOCATION_TABLE + " (" +
            DatabaseHandler.LOCATION_TABLE_X + ", " +
            DatabaseHandler.LOCATION_TABLE_Y + ", " +
            DatabaseHandler.LOCATION_TABLE_Z + ", " +
            DatabaseHandler.LOCATION_TABLE_NAME + ") " +
            "VALUES (?,?,?,?)";
    private final String UPDATE_LOCATION = "UPDATE " + DatabaseHandler.LOCATION_TABLE +
            " SET " +
            DatabaseHandler.LOCATION_TABLE_X + " = ?, " +
            DatabaseHandler.LOCATION_TABLE_Y + " = ?, " +
            DatabaseHandler.LOCATION_TABLE_Z + " = ?, " +
            DatabaseHandler.LOCATION_TABLE_NAME + " = ?" +
            " WHERE " + DatabaseHandler.LOCATION_TABLE_ID + " = ?";

    private final String SELECT_ALL_ADMIN = "SELECT * FROM " + DatabaseHandler.ADMIN_TABLE;
    private final String SELECT_ALL_ADMIN_BY_ID = SELECT_ALL_ADMIN + " WHERE " +
            DatabaseHandler.ADMIN_TABLE_ID + " = ?";
    private final String INSERT_ADMIN = "INSERT INTO " +
            DatabaseHandler.ADMIN_TABLE + " (" +
            DatabaseHandler.ADMIN_TABLE_NAME + ", " +
            DatabaseHandler.ADMIN_TABLE_PASSPORT_ID + ", " +
            DatabaseHandler.ADMIN_TABLE_COUNTRY_ID + ", " +
            DatabaseHandler.ADMIN_TABLE_LOCATION_ID + ") " +
            "VALUES (?,?,?,?)";
    private final String UPDATE_ADMIN = "UPDATE " + DatabaseHandler.ADMIN_TABLE +
            " SET " +
            DatabaseHandler.ADMIN_TABLE_NAME + " = ?, " +
            DatabaseHandler.ADMIN_TABLE_PASSPORT_ID + " = ?, " +
            DatabaseHandler.ADMIN_TABLE_COUNTRY_ID + " = ?" +
            " WHERE " + DatabaseHandler.ADMIN_TABLE_ID + " = ?";



    private DatabaseHandler databaseHandler;
    private DatabaseUserManager databaseUserManager;
    public DatabaseCollectionManager(DatabaseHandler databaseHandler,DatabaseUserManager databaseUserManager){
        this.databaseHandler = databaseHandler;
        this.databaseUserManager =databaseUserManager;
    }

    LocalDate localDate = LocalDate.now();

    private StudyGroup createStudyGroup(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong(DatabaseHandler.STUDY_GROUP_TABLE_ID);
        String name = resultSet.getString(DatabaseHandler.STUDY_GROUP_TABLE_NAME);
        Coordinates coordinates = getCoordinatesByID(resultSet.getInt(DatabaseHandler.STUDY_GROUP_TABLE_COORDINATE_ID));
        LocalDate localDate = resultSet.getDate(DatabaseHandler.STUDY_GROUP_TABLE_LOCAL_DATE).toLocalDate();
        Long studentsCount = resultSet.getLong(DatabaseHandler.STUDY_GROUP_TABLE_STUDENTS_COUNT);
        long transferredStudents = resultSet.getLong(DatabaseHandler.STUDY_GROUP_TABLE_TRANSFERRED_STUDENTS);
        FormOfEducation formOfEducation = getFormOfEducationByID(resultSet.getInt(DatabaseHandler.STUDY_GROUP_TABLE_FORM_OF_EDUCATION_ID));
        Semester semester = getSemesterByID(resultSet.getInt(DatabaseHandler.STUDY_GROUP_TABLE_SEMESTER_ID));
        Person person = getPersonID(resultSet.getInt(DatabaseHandler.STUDY_GROUP_TABLE_ADMIN_ID));
        String username = databaseUserManager.getUserID(resultSet.getInt(DatabaseHandler.STUDY_GROUP_TABLE_USER_ID)).getUsername();
        return new StudyGroup(
                id,
                name,
                coordinates,
                localDate,
                studentsCount,
                transferredStudents,
                formOfEducation,
                semester,
                person,
                username
        );
    }

    public CustomTreeMap<String,StudyGroup> getCollection() throws SQLException {
        CustomTreeMap<String,StudyGroup> studyGroupCollection = new CustomTreeMap<String,StudyGroup>();
        PreparedStatement preparedStatementAllStatement = null;
        try{
            preparedStatementAllStatement = databaseHandler.getPreparedStatement(SELECT_ALL_STUDY_GROUP,false);
            ResultSet resultSet = preparedStatementAllStatement.executeQuery();
            while (resultSet.next()){
                studyGroupCollection.put(resultSet.getString(DatabaseHandler.STUDY_GROUP_TABLE_KEY),createStudyGroup(resultSet));
            }
        } catch (SQLException exception) {
            throw new SQLException();
        }finally {
            databaseHandler.closePreparedStatement(preparedStatementAllStatement);
        }
        return studyGroupCollection;
    }



    private Coordinates getCoordinatesByID(int id) throws SQLException {
        Coordinates coordinates;
        PreparedStatement preparedStatementCoordinatesByID = null;
        try{
            preparedStatementCoordinatesByID = databaseHandler.getPreparedStatement(SELECT_ALL_COORDINATES_BY_ID,false);
            preparedStatementCoordinatesByID.setInt(1,id);
            ResultSet resultSet = preparedStatementCoordinatesByID.executeQuery();
            LOG.info("Выполнен запрос SELECT_ALL_COORDINATES_BY_ID.");
            if(resultSet.next()) {
                coordinates = new Coordinates(
                        resultSet.getInt(DatabaseHandler.COORDINATE_TABLE_X),
                        resultSet.getLong(DatabaseHandler.COORDINATE_TABLE_Y)
                );
            } else throw new SQLException();
        } catch (SQLException exception) {
            LOG.info("Выполнен запрос SELECT_ALL_COORDINATES_BY_ID.");
            throw new SQLException(exception);
        }finally {
            databaseHandler.closePreparedStatement(preparedStatementCoordinatesByID);
        }
        return coordinates;
    }

    private FormOfEducation getFormOfEducationByID(int id) throws SQLException {
        FormOfEducation formOfEducation;
        PreparedStatement preparedStatementFormOfEducationByID = null;
        try{
            preparedStatementFormOfEducationByID = databaseHandler.getPreparedStatement(SELECT_ALL_FORM_OF_EDUCATIO_BY_ID,false);
            preparedStatementFormOfEducationByID.setInt(1,id);
            ResultSet resultSet = preparedStatementFormOfEducationByID.executeQuery();
            LOG.info("Выполнен запрос SELECT_ALL_COORDINATES_BY_ID.");
            if(resultSet.next()) {
                formOfEducation = FormOfEducation.valueOf(resultSet.getString(DatabaseHandler.FORM_OF_EDUCATION_TABLE_NAME));
            } else throw new SQLException();
        } catch (SQLException exception) {
            LOG.info("Выполнен запрос SELECT_ALL_COORDINATES_BY_ID.");
            throw new SQLException(exception);
        }finally {
            databaseHandler.closePreparedStatement(preparedStatementFormOfEducationByID);
        }
        return formOfEducation;
    }

    private Semester getSemesterByID(int id) throws SQLException {
        Semester semester;
        PreparedStatement preparedStatementSemesterByID = null;
        try{
            preparedStatementSemesterByID = databaseHandler.getPreparedStatement(SELECT_ALL_SEMESTER_BY_ID,false);
            preparedStatementSemesterByID.setInt(1,id);
            ResultSet resultSet = preparedStatementSemesterByID.executeQuery();
            LOG.info("Выполнен запрос SELECT_ALL_COORDINATES_BY_ID.");
            if(resultSet.next()) {
                semester = Semester.valueOf(resultSet.getString(DatabaseHandler.SEMESTER_TABLE_TYPE));
            } else throw new SQLException();
        } catch (SQLException exception) {
            LOG.info("Выполнен запрос SELECT_ALL_COORDINATES_BY_ID.");
            throw new SQLException(exception);
        }finally {
            databaseHandler.closePreparedStatement(preparedStatementSemesterByID);
        }
        return semester;
    }

    private Country getCountryID(int id) throws SQLException {
        Country country;
        PreparedStatement preparedStatementCountryByID = null;
        try{
            preparedStatementCountryByID = databaseHandler.getPreparedStatement(SELECT_ALL_COUNTRY_BY_ID,false);
            preparedStatementCountryByID.setInt(1,id);
            ResultSet resultSet = preparedStatementCountryByID.executeQuery();
            LOG.info("Выполнен запрос SELECT_ALL_COORDINATES_BY_ID.");
            if(resultSet.next()) {
                country = Country.valueOf(resultSet.getString(DatabaseHandler.COUNTRY_TABLE_NAME));
            } else throw new SQLException();
        } catch (SQLException exception) {
            LOG.info("Выполнен запрос SELECT_ALL_COORDINATES_BY_ID.");
            throw new SQLException(exception);
        }finally {
            databaseHandler.closePreparedStatement(preparedStatementCountryByID);
        }
        return country;
    }

    private Location getLocationID(int id) throws SQLException {
        Location location;
        PreparedStatement preparedStatementLocationByID = null;
        try{
            preparedStatementLocationByID = databaseHandler.getPreparedStatement(SELECT_ALL_LOCATION_BY_ID,false);
            preparedStatementLocationByID.setInt(1,id);
            ResultSet resultSet = preparedStatementLocationByID.executeQuery();
            LOG.info("Выполнен запрос SELECT_ALL_COORDINATES_BY_ID.");
            if(resultSet.next()) {
                location = new Location(
                        resultSet.getFloat(DatabaseHandler.LOCATION_TABLE_X),
                        resultSet.getLong(DatabaseHandler.LOCATION_TABLE_Y),
                        resultSet.getInt(DatabaseHandler.LOCATION_TABLE_Z),
                        resultSet.getString(DatabaseHandler.LOCATION_TABLE_NAME)
                );
            } else throw new SQLException();
        } catch (SQLException exception) {
            LOG.info("Выполнен запрос SELECT_ALL_COORDINATES_BY_ID.");
            throw new SQLException(exception);
        }finally {
            databaseHandler.closePreparedStatement(preparedStatementLocationByID);
        }
        return location;
    }
    private Person getPersonID(int id) throws SQLException {
        Person person;
        PreparedStatement preparedStatementAdminByID = null;
        try{
            preparedStatementAdminByID = databaseHandler.getPreparedStatement(SELECT_ALL_ADMIN_BY_ID,false);
            preparedStatementAdminByID.setInt(1,id);
            ResultSet resultSet = preparedStatementAdminByID.executeQuery();
            LOG.info("Выполнен запрос SELECT_ALL_COORDINATES_BY_ID.");
            if(resultSet.next()) {
                Country country = getCountryID(resultSet.getInt(DatabaseHandler.ADMIN_TABLE_COUNTRY_ID));
                Location location = getLocationID(resultSet.getInt(DatabaseHandler.ADMIN_TABLE_LOCATION_ID));
                person = new Person(
                        resultSet.getString(DatabaseHandler.ADMIN_TABLE_NAME),
                        resultSet.getString(DatabaseHandler.ADMIN_TABLE_PASSPORT_ID),
                        country,
                        location
                );
            } else throw new SQLException();
        } catch (SQLException exception) {
            LOG.info("Выполнен запрос SELECT_ALL_COORDINATES_BY_ID.");
            throw new SQLException(exception);
        }finally {
            databaseHandler.closePreparedStatement(preparedStatementAdminByID);
        }
        return person;
    }
    public StudyGroup insertStudyGroup(StudyGroupRaw studyGroupRaw,String key,User user) throws Exception {
        StudyGroup studyGroup;
        PreparedStatement preparedStatementInsertStudyGroup = null;
        PreparedStatement preparedStatementInsertCoordinate = null;
        PreparedStatement preparedStatementInsertAdmin = null;
        PreparedStatement preparedStatementInsertLocation = null;
        try{
            databaseHandler.setCommitMode();
            databaseHandler.setSavepoint();

            preparedStatementInsertStudyGroup = databaseHandler.getPreparedStatement(INSERT_STUDY_GROUP,true);
            preparedStatementInsertCoordinate = databaseHandler.getPreparedStatement(INSERT_COORDINATE, true);
            preparedStatementInsertAdmin = databaseHandler.getPreparedStatement(INSERT_ADMIN,true);
            preparedStatementInsertLocation = databaseHandler.getPreparedStatement(INSERT_LOCATION,true);

            preparedStatementInsertLocation.setFloat(1,studyGroupRaw.getGroupAdmin().getLocation().getX());
            preparedStatementInsertLocation.setDouble(2,studyGroupRaw.getGroupAdmin().getLocation().getX());
            preparedStatementInsertLocation.setInt(3,studyGroupRaw.getGroupAdmin().getLocation().getZ());
            preparedStatementInsertLocation.setString(4,studyGroupRaw.getGroupAdmin().getLocation().getName());

            if(preparedStatementInsertLocation.executeUpdate() == 0) throw new SQLException();
            ResultSet generatedLocationKey = preparedStatementInsertLocation.getGeneratedKeys();
            int locationID;
            if(generatedLocationKey.next()){
                locationID = generatedLocationKey.getInt(1);
            }else throw new SQLException();

            preparedStatementInsertAdmin.setString(1,studyGroupRaw.getGroupAdmin().getName());
            preparedStatementInsertAdmin.setString(2,studyGroupRaw.getGroupAdmin().getPassportID());
            preparedStatementInsertAdmin.setInt(3,getCountryID(studyGroupRaw.getGroupAdmin().getCountry()));
            preparedStatementInsertAdmin.setInt(4,locationID);
            
            if(preparedStatementInsertAdmin.executeUpdate() == 0) throw new SQLException();
            ResultSet generatedAdminKey = preparedStatementInsertAdmin.getGeneratedKeys();
            int adminID;
            if(generatedAdminKey.next()){
                adminID = generatedAdminKey.getInt(1);
            }else {
                throw new SQLException();
            }
            
            preparedStatementInsertCoordinate.setInt(1,studyGroupRaw.getCoordinates().getX());
            preparedStatementInsertCoordinate.setLong(2,studyGroupRaw.getCoordinates().getY());
            
            if(preparedStatementInsertCoordinate.executeUpdate() == 0) throw new SQLException();
            ResultSet generatedCoordinateKey = preparedStatementInsertCoordinate.getGeneratedKeys();
            int coordinateID;
            if(generatedCoordinateKey.next()){
                coordinateID = generatedCoordinateKey.getInt(1);
            } else{
                throw new SQLException();
            }
            preparedStatementInsertStudyGroup.setString(1,key);
            preparedStatementInsertStudyGroup.setString(2,studyGroupRaw.getName());
            preparedStatementInsertStudyGroup.setInt(3,coordinateID);
            preparedStatementInsertStudyGroup.setDate(4,java.sql.Date.valueOf(localDate));
            preparedStatementInsertStudyGroup.setLong(5,studyGroupRaw.getStudentsCount());
            preparedStatementInsertStudyGroup.setLong(6,studyGroupRaw.getTransferredStudents());
            preparedStatementInsertStudyGroup.setInt(7,getFormOfEducationID(studyGroupRaw.getFormOfEducation()));
            preparedStatementInsertStudyGroup.setInt(8,getSemesterID(studyGroupRaw.getSemesterEnum()));
            preparedStatementInsertStudyGroup.setInt(9,adminID);
            preparedStatementInsertStudyGroup.setLong(10,databaseUserManager.getUserIdByUsername(user));

            if(preparedStatementInsertStudyGroup.executeUpdate() == 0) throw new SQLException();
            ResultSet generatedStudyGroupKey = preparedStatementInsertStudyGroup.getGeneratedKeys();
            long studyGroupID;
            if(generatedStudyGroupKey.next()){
                studyGroupID = generatedStudyGroupKey.getLong(1);
            }else throw new SQLException();
            LOG.info("Выполнен запрос INSERT_STUDY_GROUP");

            studyGroup = new StudyGroup(
                    studyGroupID,
                    studyGroupRaw.getName(),
                    studyGroupRaw.getCoordinates(),
                    studyGroupRaw.getLocalDate(),
                    studyGroupRaw.getStudentsCount(),
                    studyGroupRaw.getTransferredStudents(),
                    studyGroupRaw.getFormOfEducation(),
                    studyGroupRaw.getSemesterEnum(),
                    studyGroupRaw.getGroupAdmin(),
                    user.getUsername()
            );

            databaseHandler.commit();
            return studyGroup;
        } catch (SQLException exception) {
            LOG.error("Произошла ошибка при выполнении группы запросов на добавление нового объекта!");
            databaseHandler.rollback();
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedStatementInsertStudyGroup);
            databaseHandler.closePreparedStatement(preparedStatementInsertCoordinate);
            databaseHandler.closePreparedStatement(preparedStatementInsertAdmin);
            databaseHandler.closePreparedStatement(preparedStatementInsertLocation);
            databaseHandler.setNormalMode();
        }
    }

    public void updateStudyGroupByID(long studyGroupID, StudyGroupRaw studyGroupRaw,User user) throws SQLException {
        PreparedStatement preparedStatementUpdateStudyGroupByID = null;
        PreparedStatement preparedStatementUpdateCoordinateByID = null;
        PreparedStatement preparedStatementUpdateLocationByID = null;
        PreparedStatement preparedStatementUpdateAdminByID = null;


        try {
            databaseHandler.setCommitMode();
            databaseHandler.setSavepoint();

            preparedStatementUpdateLocationByID = databaseHandler.getPreparedStatement(UPDATE_LOCATION,true);
            preparedStatementUpdateAdminByID = databaseHandler.getPreparedStatement(UPDATE_ADMIN,true);
            preparedStatementUpdateCoordinateByID = databaseHandler.getPreparedStatement(UPDATE_COORDINATE,true);
            preparedStatementUpdateStudyGroupByID = databaseHandler.getPreparedStatement(UPDATE_STUDY_GROUP,true);


            preparedStatementUpdateStudyGroupByID.setString(1,studyGroupRaw.getName());
            preparedStatementUpdateStudyGroupByID.setDate(2, Date.valueOf(LocalDate.now()));
            preparedStatementUpdateStudyGroupByID.setLong(3,studyGroupRaw.getStudentsCount());
            preparedStatementUpdateStudyGroupByID.setLong(4,studyGroupRaw.getTransferredStudents());
            preparedStatementUpdateStudyGroupByID.setInt(5,getFormOfEducationID(studyGroupRaw.getFormOfEducation()));
            preparedStatementUpdateStudyGroupByID.setInt(6,getSemesterID(studyGroupRaw.getSemesterEnum()));
            preparedStatementUpdateStudyGroupByID.setLong(7,studyGroupID);

            if(preparedStatementUpdateStudyGroupByID.executeUpdate() == 0) throw new SQLException();
            ResultSet generatedStudyGroupKey = preparedStatementUpdateStudyGroupByID.getGeneratedKeys();
            int coordinateID;
            int adminID;
            int userID;
            if(generatedStudyGroupKey.next()){
                coordinateID = generatedStudyGroupKey.getInt(4);
                adminID = generatedStudyGroupKey.getInt(10);
                userID = generatedStudyGroupKey.getInt(11);
            } else throw new SQLException();

            preparedStatementUpdateCoordinateByID.setInt(1,studyGroupRaw.getCoordinates().getX());
            preparedStatementUpdateCoordinateByID.setLong(2,studyGroupRaw.getCoordinates().getY());
            preparedStatementUpdateCoordinateByID.setLong(3,coordinateID);
            if(preparedStatementUpdateCoordinateByID.executeUpdate() == 0) throw new SQLException();

            preparedStatementUpdateAdminByID.setString(1,studyGroupRaw.getGroupAdmin().getName());
            preparedStatementUpdateAdminByID.setString(2,studyGroupRaw.getGroupAdmin().getPassportID());
            preparedStatementUpdateAdminByID.setInt(3,getCountryID(studyGroupRaw.getGroupAdmin().getCountry()));
            preparedStatementUpdateAdminByID.setInt(4,adminID);

            if(preparedStatementUpdateAdminByID.executeUpdate() == 0) throw new SQLException();
            ResultSet generatedAdminKey = preparedStatementUpdateAdminByID.getGeneratedKeys();
            int locationID;
            if(generatedAdminKey.next()){
                locationID = generatedAdminKey.getInt(5);
            } else throw new SQLException();

            preparedStatementUpdateLocationByID.setFloat(1,studyGroupRaw.getGroupAdmin().getLocation().getX());
            preparedStatementUpdateLocationByID.setLong(2,studyGroupRaw.getGroupAdmin().getLocation().getY());
            preparedStatementUpdateLocationByID.setInt(3,studyGroupRaw.getGroupAdmin().getLocation().getZ());
            preparedStatementUpdateLocationByID.setString(4,studyGroupRaw.getGroupAdmin().getLocation().getName());
            preparedStatementUpdateLocationByID.setInt(5,locationID);
            if(preparedStatementUpdateLocationByID.executeUpdate() == 0) throw new SQLException();

            databaseHandler.commit();
        } catch (SQLException exception) {
            databaseHandler.rollback();
            exception.printStackTrace();
        } finally {
            databaseHandler.closePreparedStatement(preparedStatementUpdateLocationByID);
            databaseHandler.closePreparedStatement(preparedStatementUpdateAdminByID);
            databaseHandler.closePreparedStatement(preparedStatementUpdateCoordinateByID);
            databaseHandler.closePreparedStatement(preparedStatementUpdateStudyGroupByID);
            databaseHandler.setNormalMode();
        }
    }

    public void removeStudyGroupByKey(String key) throws SQLException {
        PreparedStatement preparedStatementDeleteStudyGroup = null;
        try {
            databaseHandler.setCommitMode();
            databaseHandler.setSavepoint();

            preparedStatementDeleteStudyGroup = databaseHandler.getPreparedStatement(DELETE_STUDY_GROUP,false);
            preparedStatementDeleteStudyGroup.setString(1,key);
            if(preparedStatementDeleteStudyGroup.executeUpdate() == 0) throw new SQLException();
            LOG.info("Выполнен запрос REMOVE_BY_KEY");
            databaseHandler.commit();
        } catch (SQLException exception){
            throw new SQLException();
        }finally {
            databaseHandler.closePreparedStatement(preparedStatementDeleteStudyGroup);
            databaseHandler.setNormalMode();
        }
    }




    public int getCountryID(Country country){
        int id;
        switch (country){
            case GERMANY:
                id = 1;
                break;
            case FRANCE:
                id = 2;
                break;
            case SPAIN:
                id = 3;
                break;
            case CHINA:
                id = 4;
                break;
            case ITALY:
                id = 5;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + country);
        }
        return id;
    }

    public int getFormOfEducationID(FormOfEducation formOfEducation){
        int id;
        switch (formOfEducation){
            case DISTANCE_EDUCATION:
                id = 1;
                break;
            case FULL_TIME_EDUCATION:
                id = 2;
                break;
            case EVENING_CLASSES:
                id = 3;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + formOfEducation);
        }
        return id;
    }

    public int getSemesterID(Semester semester){
        int id;
        switch (semester){
            case SECOND:
                id = 1;
                break;
            case THIRD:
                id = 2;
                break;
            case SEVENTH:
                id = 3;
                break;
            case EIGHT:
                id = 4;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + semester);
        }
        return id;
    }
}
