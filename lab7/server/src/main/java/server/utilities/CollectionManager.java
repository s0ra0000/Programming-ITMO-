package server.utilities;

import common.exceptions.DatabaseHandlingException;
import common.interaction.StudyGroupRaw;
import common.interaction.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.commands.ICommand;
import common.datas.FormOfEducation;
import common.datas.StudyGroup;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import common.exceptions.IncorrectInputScriptException;

import java.io.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import server.customCollection.*;
/**
 * Receiver class
 */

public class CollectionManager {
    private final CommandManager commandManager;
    protected static CustomTreeMap<String, StudyGroup> studyGroupCollection = new CustomTreeMap<>();
    private final DatabaseCollectionManager databaseCollectionManager;

    private static Long lastId = 1L;
    //private final QueryManager queryManager;
    private final List<String> scriptStack = new ArrayList<>();
    private LocalDate initData;
    public final Logger LOG
            = LoggerFactory.getLogger(CollectionManager.class);
    /**
     * Constructor
     * @param commandManager Command manager
     * @throws IOException On input error
     */
    public CollectionManager(CommandManager commandManager,DatabaseCollectionManager databaseCollectionManager) throws IOException, SQLException {
        this.databaseCollectionManager = databaseCollectionManager;
        this.commandManager = commandManager;
        loadCollectionFromCsv();
    }

    /**
     * Load collection from CSV file.
     */
    public void loadCollectionFromCsv() throws IOException, SQLException {
        studyGroupCollection = databaseCollectionManager.getCollection();
        initData = LocalDate.now();
    }

    /**
     * @return last ID of Collection
     */
    static public Long getLastId(){
        return lastId;
    }

    /**
     *
     * @param arg key of TreeMap
     */
    public void insert(String arg, StudyGroupRaw studyGroupRaw, User user) {

        try {
            if(!studyGroupCollection.containsKey(arg)){
                databaseCollectionManager.insertStudyGroup(studyGroupRaw,arg,user);
                studyGroupCollection.put(arg,
                        new StudyGroup(getLastId()+1L,
                                studyGroupRaw.getName(),
                                studyGroupRaw.getCoordinates(),
                                LocalDate.now(),
                                studyGroupRaw.getStudentsCount(),
                                studyGroupRaw.getTransferredStudents(),
                                studyGroupRaw.getFormOfEducation(),
                                studyGroupRaw.getSemesterEnum(),
                                studyGroupRaw.getGroupAdmin(),
                                user.getUsername())
                        );
                ResponseOutputer.append("?????????????? ?????????????????? Study Group!");
                LOG.info("?????????????? ?????????????????? Study Group!");
            }
            else {
                ResponseOutputer.append("???????? ???? ?????????? ??????????????????????????");
                LOG.error("???????? ???? ?????????? ??????????????????????????");
            }
        }
        catch(IncorrectInputScriptException err){
            throw new IncorrectInputScriptException();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Print collection
     */
    public void show() {
        printCollection(studyGroupCollection);
    }
//
    /**
     * Print all server.commands with description
     */
    public void help() {
        for (Map.Entry<String, ICommand> entry : commandManager.getCommands().entrySet()) {
            String key = entry.getKey();
            ICommand value = entry.getValue();
            ResponseOutputer.append(value.getDescription());
        }
    }

    /**
     * Clear collection
     */
    public void clear(User user) throws SQLException {
        for(Map.Entry<String,StudyGroup> studyGroup : studyGroupCollection.entrySet()){
            if(studyGroup.getValue().getUserName().equals(user.getUsername())){
                String key = studyGroup.getKey();
                databaseCollectionManager.removeStudyGroupByKey(key);
                studyGroupCollection.remove(key);
            }
        }
        ResponseOutputer.append("?????????????? ???????????????? ??????????????????!");
        LOG.info("?????????????? ???????????????? ??????????????????!");
    }

    /**
     * Print information of collection
     */
    public void info(){
        ResponseOutputer.append("?????? ??????????????????: " + studyGroupCollection.getClass().getName());
        ResponseOutputer.append("???????? ??????????????????????????: " + initData);
        ResponseOutputer.append("???????????????????? ??????????????????: " + studyGroupCollection.size());
    }

    /**
     * Update collection with key
     * @param arg id of StudyGroup
     */
    public void update(String arg,StudyGroupRaw studyGroupRaw,User user){
        try{
            final Long id = Long.parseLong(arg);
            boolean updated = false;

            for(Map.Entry<String, StudyGroup> studyGroup : studyGroupCollection.entrySet()){
                if(studyGroup.getValue().getId().equals(id)){
                    if(user.getUsername().equals(studyGroup.getValue().getUserName())){
                        databaseCollectionManager.updateStudyGroupByID(id,studyGroupRaw,user);
                        studyGroupCollection.replace(studyGroup.getKey(),
                                new StudyGroup(
                                        id,
                                        studyGroupRaw.getName(),
                                        studyGroupRaw.getCoordinates(),
                                        LocalDate.now(),
                                        studyGroupRaw.getStudentsCount(),
                                        studyGroupRaw.getTransferredStudents(),
                                        studyGroupRaw.getFormOfEducation(),
                                        studyGroupRaw.getSemesterEnum(),
                                        studyGroupRaw.getGroupAdmin(),
                                        studyGroupRaw.getUsername()
                                )
                        );
                        updated = true;
                    }
                    else{
                        updated = false;
                        ResponseOutputer.append("?? ?????? ?????? ??????????????. ?????? ???????????? ???? ?????????????????????? ??????!");
                    }
                }
            }
            if(updated) {
                ResponseOutputer.append("?????????????? ??????????????????!");
                LOG.info("?????????????? ??????????????????!");
            }
            else {
                LOG.error("???? ?????????????? ????????????????. ?????? ???????????? id!");
            }

        }
        catch(NumberFormatException | SQLException err){
            ResponseOutputer.append("id ???????????? ???????? ????????????!");
            LOG.error("id ???????????? ???????? ????????????!");
        }

    }

    /**
     * Remove element of collection with key
     * @param arg key of TreeMap
     */
    public void remove(String arg, User user) throws SQLException {
        if(studyGroupCollection.get(arg).getUserName().equals(user.getUsername())){
            if(studyGroupCollection.containsKey(arg) && studyGroupCollection.get(arg).getUserName().equals(user.getUsername())){
                databaseCollectionManager.removeStudyGroupByKey(arg);
                studyGroupCollection.remove(arg);
                ResponseOutputer.append("?????????????? ??????????????!");
                LOG.info("?????????????? ??????????????!");
            }
        }else{
            ResponseOutputer.append("?? ?????? ?????? ??????????????. ?????? ???????????? ???? ?????????????????????? ??????!");
        }
    }

    /**
     * Exit program without saving
     */
    public void exit(){
        ResponseOutputer.append("???? ????????????????!");
    }

    /**
     * Execute command from file
     * @param arg script name
     */
    public void executeScript(String arg){
        ResponseOutputer.append("");
        LOG.info("???????????? ?????????????? ???? ??????????????...");
    }

    /**
     * Print last 11 command history
     */
    public void history(){
        List<String> commandHistory = commandManager.getCommandHistory();
        for(int i=0;i<commandHistory.size();i++){
            ResponseOutputer.append(i+1+" " + commandHistory.get(i));
        }
    }

    /**
     * Filter by name
     * @param arg Group name
     */
    public void filter_contains_name(String arg){
        try{
            Map<String,StudyGroup> filteredByName = studyGroupCollection.entrySet()
                    .stream()
                    .filter(studyGroup -> studyGroup.getValue().getName().toLowerCase().contains(arg.toLowerCase()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            if(!filteredByName.isEmpty()) printCollection(filteredByName);
            else {
                ResponseOutputer.append("?????? ?????????? ??????????????????!");
                LOG.error("?????? ?????????? ??????????????????!");
            }
        }catch(IllegalArgumentException err){
            ResponseOutputer.append("?????? ?????????? ??????????????????!");
            LOG.error("?????? ?????????? ??????????????????!");
        }
    }

    /**
     * Filter by transferred students
     * @param arg number of transferred students
     */
    public void filter_less_than_transferred_students(String arg){
        try{
            Map <String,StudyGroup> filteredByTransferredStudents = studyGroupCollection.entrySet()
                    .stream()
                    .filter(studyGroup -> Long.parseLong(arg) > studyGroup.getValue().getTransferredStudents())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            if(!filteredByTransferredStudents.isEmpty()) printCollection(filteredByTransferredStudents);

            else {
                ResponseOutputer.append("?????? ?????????? ??????????????????!");
                LOG.error("?????? ?????????? ??????????????????!");
            }
        }catch(IllegalArgumentException err){
            ResponseOutputer.append("???????????? ???????? ????????????");
            LOG.error("???????????? ???????? ????????????");
        }
    }

    /**
     * filter by form of education
     * @param arg form of education
     */
    public void filter_greater_than_form_of_education(String arg){
        FormOfEducation formOfEducation = null;
        try{
            formOfEducation = FormOfEducation.valueOf(arg.toUpperCase());
            FormOfEducation finalFormOfEducation = formOfEducation;
            Map<String,StudyGroup> filteredByFormOfEducation = studyGroupCollection.entrySet()
                    .stream()
                    .filter(studyGroup -> finalFormOfEducation.compareTo(studyGroup.getValue().getFormOfEducation()) < 0 )
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            if(!filteredByFormOfEducation.isEmpty()) printCollection(filteredByFormOfEducation);

            else {
                ResponseOutputer.append("?????? ?????????? ??????????????????!");
                LOG.error("?????? ?????????? ??????????????????!");
            }

        }catch(IllegalArgumentException err){
            ResponseOutputer.append("?????? ?????????? ??????????????????!");
            LOG.error("?????? ?????????? ??????????????????!");
        }
    }


    /**
     * replace if element is greater
     * @param arg key of TreeMap
     */
    public void replace_if_greater(String arg,StudyGroupRaw studyGroupRaw,User user){
        if(studyGroupCollection.get(arg).getUserName().equals(user.getUsername())){
            if(studyGroupCollection.get(arg) != null){
                StudyGroup replaceStudyGroup =
                        new StudyGroup(
                                getLastId()+1,
                                studyGroupRaw.getName(),
                                studyGroupRaw.getCoordinates(),
                                LocalDate.now(),
                                studyGroupRaw.getStudentsCount(),
                                studyGroupRaw.getTransferredStudents(),
                                studyGroupRaw.getFormOfEducation(),
                                studyGroupRaw.getSemesterEnum(),
                                studyGroupRaw.getGroupAdmin(),
                                studyGroupRaw.getUsername()
                        );
                if(studyGroupCollection.get(arg).getStudentsCount().compareTo(replaceStudyGroup.getStudentsCount()) < 0){
                    studyGroupCollection.replace(arg,replaceStudyGroup);
                    ResponseOutputer.append("?????????????? ???????????????? ????????????????");
                    LOG.info("?????????????? ???????????????? ????????????????");
                }
                ResponseOutputer.append("???? ???????????????? ????????????????");
                LOG.info("???? ???????????????? ????????????????");
            }
            else {
                ResponseOutputer.append("???? ?????????????? ??????????????. ?????? ???????????? ??????????.");
                LOG.error("???? ?????????????? ??????????????. ?????? ???????????? ??????????.");
            }
        }
        else{
            ResponseOutputer.append("?? ?????? ?????? ??????????????. ?????? ???????????? ???? ?????????????????????? ??????!");
        }

    }

    /**
     * Print collection as tableView;
     * @param collection collection, which will print
     */

    public void printCollection(Map<String, StudyGroup> collection){
        Map<String, StudyGroup> sortedMap = sortValues(collection);
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("id","Key","name","Coord-X", "Coord-Y", "creatDate","St", "T-St","formEdu","SEM","A-Name","passId","CNTR","LOC-X","LOC-Y","LOC-Z","LOC-Name","username");
        at.addRule();
        sortedMap.forEach((key,entry)-> {
            at.addRow(
                    String.valueOf(entry.getId()),
                    String.valueOf(key),
                    String.valueOf(entry.getName()),
                    String.valueOf(entry.getCoordinates().getX()),
                    String.valueOf(entry.getCoordinates().getY()),
                    String.valueOf(entry.getCreationDate()),
                    String.valueOf(entry.getStudentsCount()),
                    String.valueOf(entry.getTransferredStudents()),
                    String.valueOf(entry.getFormOfEducation()),
                    String.valueOf(entry.getSemesterEnum()),
                    String.valueOf(entry.getGroupAdmin().getName()),
                    String.valueOf(entry.getGroupAdmin().getPassportID()),
                    String.valueOf(entry.getGroupAdmin().getCountry()),
                    String.valueOf(entry.getGroupAdmin().getLocation().getX()),
                    String.valueOf(entry.getGroupAdmin().getLocation().getY()),
                    String.valueOf(entry.getGroupAdmin().getLocation().getZ()),
                    String.valueOf(entry.getGroupAdmin().getLocation().getName()),
                    String.valueOf(entry.getUserName())
            );
        });
        at.addRule();
        at.getContext().setWidth(200);
        CWC_LongestLine cwc = new CWC_LongestLine();
        at.getRenderer().setCWC(cwc);
        String render = at.render();
        ResponseOutputer.append(render);
    }
    public static <K, V extends Comparable<V>> Map<K, V> sortValues(final Map<K, V> m)
    {
        Comparator<K> com = new Comparator<K>()
        {
            public int compare(K k1, K k2)
            {
                int compare = m.get(k1).compareTo(m.get(k2));
                if(compare == 0)
                {
                    return 1;
                }
                else
                {
                    return compare;
                }
            }
        };Map<K, V> sortedByValues = new TreeMap<K, V>(com);
        sortedByValues.putAll(m);
        return sortedByValues;
    }

}