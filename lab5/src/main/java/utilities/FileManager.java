package utilities;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import customCollection.CustomTreeMap;
import datas.*;
import exceptions.IncorrectInputScriptException;

/**
 * File manager, saving and loading collection from file.
 */
public class FileManager {
    private final String path;
    private long lastId = 1L;

    /**
     * Constructor
     * @param path Path of CSV file
     * @throws FileNotFoundException If file not found
     */
    public FileManager(String path) throws FileNotFoundException {
        this.path = path;
    }

    /**
     * Read collection from CSV file
     * @return collection TreeMap
     */
    public CustomTreeMap<String, StudyGroup> readCollection() throws IOException {
        CustomTreeMap<String, StudyGroup> studyGroupCollection = new CustomTreeMap<>();
        String[] data;
        int row = 0;
        try {
            InputStream inputStream = new FileInputStream(path);
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
            while ((data = reader.readNext()) != null) {
                row++;
                try{
                    if(data.length == 16) throw new IndexOutOfBoundsException();
                    if (Long.parseLong(data[1]) > lastId) {
                        lastId = Long.parseLong(data[1]);
                    }
                    Coordinates coordinates = new Coordinates(Integer.parseInt(data[3]), Long.parseLong(data[4]));
                    Location location = new Location(Float.parseFloat(data[13]), Long.parseLong(data[14]), Integer.parseInt(data[15]), data[16]);
                    Person groupAdmin = new Person(data[10], data[11], Country.valueOf(data[12]), location);
                    StudyGroup studyGroup = new StudyGroup(
                            Long.parseLong(data[1]),
                            data[2],
                            coordinates,
                            LocalDate.parse(data[5]),
                            Long.parseLong(data[6]),
                            Long.parseLong(data[7]),
                            FormOfEducation.valueOf(data[8]),
                            Semester.valueOf(data[9]),
                            groupAdmin
                    );
                    studyGroupCollection.put(data[0], studyGroup);
                }catch(IndexOutOfBoundsException | IllegalArgumentException err){
                    System.out.println("Ошибка в " + row + " строке!");
                }
            }
            System.out.println("Данные добавлены в коллекцию!");
            reader.close();
        }catch ( FileNotFoundException err ){
            System.out.println("Отказано в доступе для чтения из файла!");
            System.exit(0);
        }

        return studyGroupCollection;
    }

    /**
     * Write collection to CSV file
     * @param studyGroupCollection collection TreeMap
     */
    public void writeCollection(CustomTreeMap<String,StudyGroup> studyGroupCollection) {
        try {
            OutputStream outputStream = new FileOutputStream(path);
            CSVWriter writer = new CSVWriter(new OutputStreamWriter(outputStream), ',', '\0');
            studyGroupCollection.forEach((key, value) -> {
                String[] data = new String[17];
                data[0] = key;
                String[] values = value.toString().split(",");
                System.arraycopy(values, 0, data, 1, values.length);
                writer.writeNext(data);

            });
            writer.close();
            System.out.println("Успешно сохранено в файл!");
        }catch (IOException err){
            System.out.println("Отказано в доступе для записи в файл!");
        }
    }
    public Long getLastId(){
        return lastId;
    }
}
