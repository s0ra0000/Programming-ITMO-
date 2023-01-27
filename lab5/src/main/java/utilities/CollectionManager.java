
package utilities;

import commands.ICommand;
import datas.FormOfEducation;
import datas.StudyGroup;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import exceptions.IncorrectInputScriptException;
import exceptions.ScriptRecursionException;
import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import customCollection.*;
/**
 * Receiver class
 */

public class CollectionManager {
    private final CommandManager commandManager;
    private final FileManager fileManager;
    protected static CustomTreeMap<String, StudyGroup> studyGroupCollection = new CustomTreeMap<>();

    private static Long lastId = 1L;
    private final QueryManager queryManager;
    private final List<String> scriptStack = new ArrayList<>();
    private LocalDate initData;

    /**
     * Constructor
     * @param commandManager Command manager
     * @param queryManager  Query manager
     * @param fileManager Fil manager
     * @throws IOException On input error
     */
    public CollectionManager(CommandManager commandManager, QueryManager queryManager,FileManager fileManager)throws IOException {
        this.fileManager = fileManager;
        this.commandManager = commandManager;
        this.queryManager = queryManager;
        loadCollectionFromCsv();
    }

    /**
     * Load collection from CSV file.
     */
    public void loadCollectionFromCsv() throws IOException {
        studyGroupCollection = fileManager.readCollection();
        lastId = fileManager.getLastId();
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
    public void insert(String arg) {
        try {
            if(!studyGroupCollection.containsKey(arg)){
                StudyGroup studyGroup = queryManager.createQueryOfStudyGroup();
                studyGroupCollection.put(arg, studyGroup);
                System.out.println("Успешно добавлено Study Group!");
            }
            else{
                System.out.println("Ключ не может перекрываться");
            }
        }
        catch(IncorrectInputScriptException err){
            throw new IncorrectInputScriptException();
        }

    }

    /**
     * Print collection
     */
    public void show() {
        printCollection(studyGroupCollection);
    }

    /**
     * Print all commands with description
     */
    public void help() {
        for (Map.Entry<String, ICommand> entry : commandManager.getCommands().entrySet()) {
            String key = entry.getKey();
            ICommand value = entry.getValue();
            System.out.println(value.getDescription());
        }
    }

    /**
     * Clear collection
     */
    public void clear(){
        studyGroupCollection.clear();
        System.out.println("Успешно очистили коллекцию!");
    }

    /**
     * Print information of collection
     */
    public void info(){
        System.out.println("Тип коллекции: " + studyGroupCollection.getClass().getName());
        System.out.println("Дата инициализации: " + initData);
        System.out.println("Количество элементов: " + studyGroupCollection.size());
    }

    /**
     * Update collection with key
     * @param arg id of StudyGroup
     */
    public void update(String arg){
            try{
                final Long id = Long.parseLong(arg);
                boolean updated = false;

                for(Map.Entry<String, StudyGroup> studyGroup : studyGroupCollection.entrySet()){
                    if(studyGroup.getValue().getId().equals(id)){
                        studyGroupCollection.replace(studyGroup.getKey(), queryManager.createQueryOfStudyGroup());
                        updated = true;
                    }
                }
                if(updated){
                    System.out.println("Успешно обновлено!");
                }
                else{
                    System.out.println("Не удалось обновить. Нет такого id!");
                }
            }
            catch(NumberFormatException err){
                System.out.println("id должно быть цифром!");
            }

    }

    /**
     * Remove element of collection with key
     * @param arg key of TreeMap
     */
    public void remove(String arg){
        if(studyGroupCollection.remove(arg) == null){
            System.out.println("Не удалось удалить. Нет такого ключа.");
        }
        else{
            System.out.println("Успешно удалено!");
        }
    }

    /**
     * Save collection to file
     */
    public void save(){
            fileManager.writeCollection(studyGroupCollection);

    }

    /**
     * Exit program without saving
     */
    public void exit(){
        System.out.println("До свидания!");
        System.exit(0);
    }

    /**
     * Execute command from file
     * @param arg script name
     */
    public void executeScript(String arg){
        try {
            Scanner tmpScanner = queryManager.getScanner();
            Scanner fileReader = new Scanner(new File(arg));
            scriptStack.add(arg);
            queryManager.setScanner(fileReader);
            queryManager.setScriptMode(true);
            while (fileReader.hasNextLine()) {
                try {
                    String[] data = fileReader.nextLine().split(" ");
                    if(data[0].equals("execute_script")){
                        for(String scriptName: scriptStack){
                            if(data[1].equals(scriptName)) throw new ScriptRecursionException();
                        }
                    }
                    commandManager.executeCommand(data);
                }
                catch(IncorrectInputScriptException err){
                    System.out.println("Скрипт неверный");
                    break;
                }
                catch (ScriptRecursionException err){
                    System.out.println("Скрипт не может быть вызываться рекурсивно");
                    break;
                }
            }
            fileReader.close();
            queryManager.setScanner(tmpScanner);
            queryManager.setScriptMode(false);
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден!");
        }
    }

    /**
     * Print last 11 command history
     */
    public void history(){
        List<String> commandHistory = commandManager.getCommandHistory();
        for(int i=0;i<commandHistory.size();i++){
            System.out.println(i+1+" " + commandHistory.get(i));
        }
    }

    /**
     * Filter by name
     * @param arg Group name
     */
    public void filter_contains_name(String arg){
        Map<String,StudyGroup> filteredByName = studyGroupCollection.entrySet()
                .stream()
                .filter(studyGroup -> studyGroup.getValue().getName().toLowerCase().contains(arg.toLowerCase()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if(!filteredByName.isEmpty()){
            printCollection(filteredByName);
        }
        else{
            System.out.println("Нет таких элементов!");
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
            if(!filteredByTransferredStudents.isEmpty()){
                printCollection(filteredByTransferredStudents);
            }
            else{
                System.out.println("Нет таких элементов!");
            }
        }catch(IllegalArgumentException err){
            System.out.println("Должно быть цифром");
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

            if(!filteredByFormOfEducation.isEmpty()){
                printCollection(filteredByFormOfEducation);
            }
            else{
                System.out.println("Нет таких элементов!");
            }
        }catch(IllegalArgumentException err){
            System.out.println("Нет таких элементов!");
        }



    }

    /**
     * Remove if element is lower
     */
    public void removeLower(){
        StudyGroup removeStudyGroup = queryManager.createQueryOfStudyGroup();
        studyGroupCollection.entrySet().removeIf(studyGroup -> studyGroup.getValue().compareTo(removeStudyGroup) < 0);
        System.out.println("Успешно удалили из коллекции");
    }

    /**
     * replace if element is greater
     * @param arg key of TreeMap
     */
    public void replace_if_greater(String arg){
        if(studyGroupCollection.get(arg) != null){
            StudyGroup replaceStudyGroup = queryManager.createQueryOfStudyGroup();
            if(studyGroupCollection.get(arg).getStudentsCount().compareTo(replaceStudyGroup.getStudentsCount()) < 0){
                studyGroupCollection.replace(arg,replaceStudyGroup);
            }
            System.out.println("Успешно заменили элементы");
        }
        else{
            System.out.println("Не удалось удалить. Нет такого ключа.");
        }
    }

    /**
     * Print collection as tableView;
     * @param collection collection, which will print
     */

    public void printCollection(Map<String, StudyGroup> collection){
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("Key","id","name","Coord-X", "Coord-Y", "creatDate","St", "T-St","formEdu","SEM","A-Name","passId","CNTR","LOC-X","LOC-Y","LOC-Z","LOC-Name");
        at.addRule();
        collection.forEach((key,entry)-> {
                    at.addRow(
                            String.valueOf(key),
                            String.valueOf(entry.getId()),
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
                            String.valueOf(entry.getGroupAdmin().getLocation().getName())
                    );
                });
            at.addRule();
            at.getContext().setWidth(200);
            CWC_LongestLine cwc = new CWC_LongestLine();
            at.getRenderer().setCWC(cwc);
            String render = at.render();
            System.out.println(render);
    }
}