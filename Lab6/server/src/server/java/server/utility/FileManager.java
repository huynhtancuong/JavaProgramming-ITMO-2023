package server.utility;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;
import common.data.SpaceMarine;

import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Operates the file for saving/loading collection.
 */
public class FileManager {
    private String envVariable;
    public FileManager(String envVariable) {
        this.envVariable = envVariable;
    }

    /**
     * Writes collection to a file.
     * @param collection Write collection.
     */
    public void writeCollection(Collection<?> collection) {
        if (System.getenv().get(envVariable) != null) {
            try (FileWriter collectionFileWriter = new FileWriter(new File(System.getenv().get(envVariable)))) {
                XStream xstream = new XStream();
                String xml = xstream.toXML(collection);
                collectionFileWriter.write(xml);
                ResponseOutputer.appenderror("Коллекция успешна сохранена в файл!");
            } catch (IOException exception) {
                ResponseOutputer.appenderror("Загрузочный файл является директорией/не может быть открыт!");
            }
        } else ResponseOutputer.appenderror("Системная переменная с загрузочным файлом не найдена!");
    }

    /**
     * Reads collection from a file.
     * @return Read collection.
     */
    public LinkedList<SpaceMarine> readCollection() {
        if (System.getenv().get(envVariable) != null) {
            try (Scanner collectionFileScanner = new Scanner(new File(System.getenv().get(envVariable)))) {
                collectionFileScanner.useDelimiter("\\Z");
                LinkedList<SpaceMarine> collection;
                XStream xstream = new XStream();
                xstream.addPermission(NoTypePermission.NONE);
                xstream.addPermission(NullPermission.NULL);
                xstream.addPermission(PrimitiveTypePermission.PRIMITIVES);
                xstream.allowTypesByWildcard(new String[] {
                        "java.util.LinkedList",
                        "common.data.SpaceMarine"
                });

                String content = collectionFileScanner.next();
                Object xml = xstream.fromXML(content);
                collection = (LinkedList<SpaceMarine>) xml;
                ResponseOutputer.append("Коллекция успешна загружена!");
                return collection;
            } catch (FileNotFoundException exception) {
                ResponseOutputer.appenderror("Загрузочный файл не найден!");
            } catch (NoSuchElementException exception) {
                ResponseOutputer.appenderror("Загрузочный файл пуст!");
            } catch (NullPointerException exception) {
                ResponseOutputer.appenderror("В загрузочном файле не обнаружена необходимая коллекция!");
            } catch (IllegalStateException exception) {
                ResponseOutputer.appenderror("Непредвиденная ошибка! " + exception);
                System.exit(0);
            } catch (IllegalArgumentException exception) {
                ResponseOutputer.appenderror("Непредвиденная ошибка! " + exception);
                System.exit(0);
            }
        } else ResponseOutputer.appenderror("Системная переменная с загрузочным файлом не найдена!");
        return new LinkedList<SpaceMarine>();
    }



    @Override
    public String toString() {
        String string = "FileManager (класс для работы с загрузочным файлом)";
        return string;
    }
}