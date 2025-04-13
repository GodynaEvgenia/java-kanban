package tracker.controller;

import tracker.exceptions.ManagerSaveException;
import tracker.model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Paths;

import static tracker.model.TasksType.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private static String filename;
    private static final String HOME = System.getProperty("user.home");
    private static final String RESOURCES_DIR = Paths.get(HOME, "IdeaProjects/java-kanban/src/tracker/resources/").toString();

    public FileBackedTaskManager(String filename) {
        super();
        this.filename = filename;

        loadSavedTasksToManager();

    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(SubTask subTask) {
        super.addSubtask(subTask);
        save();
    }

    @Override
    public int addNewTask(Task task) {
        int id = super.addNewTask(task);
        save();
        return id;
    }

    @Override
    public int addNewEpic(Epic epic) {
        int id = super.addNewEpic(epic);
        save();
        return id;
    }

    @Override
    public int addNewSubTask(SubTask subTask) {
        int id = super.addNewSubTask(subTask);
        save();
        return id;
    }

    @Override
    public void changeSubTaskStatus(SubTask subTask, Statuses newStatus) {
        super.changeSubTaskStatus(subTask, newStatus);
        save();
    }

    @Override
    public void updateEpicStatus(int epicId) {
        super.updateEpicStatus(epicId);
        save();
    }

    @Override
    public void removeTaskById(Integer taskId) {
        super.removeTaskById(taskId);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeEpicById(Integer epicId) {
        super.removeEpicById(epicId);
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeSubTaskById(Integer subTaskId) {
        super.removeSubTaskById(subTaskId);
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    public void save() {

        try (Writer fileWriter = new FileWriter(RESOURCES_DIR + "/" + filename, StandardCharsets.UTF_8)) {

            for (Task task : getTasks()) {
                String line = task.toString();
                fileWriter.write(line);
                fileWriter.write("\n");
            }

            for (Epic epic : getEpics()) {
                String line = epic.toString();
                fileWriter.write(line);
                fileWriter.write("\n");
            }

            for (SubTask subTask : getSubtasks()) {
                String line = subTask.toString();
                fileWriter.write(line);
                fileWriter.write("\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при записи в  файл: " + e.getMessage());
        }
    }

    public void loadSavedTasksToManager() {
        List<String> savedTasks = loadFromFile();

        for (String line : savedTasks) {
            String[] split = line.split(",");
            if (split[1].equals("TASK")) {
                Task task = stringToTask(line);
                addTask(task);
            } else if (split[1].equals("EPIC")) {
                Epic epic = stringToEpic(line);
                addEpic(epic);
            } else {
                SubTask subTask = stringToSubTask(line);
                addSubtask(subTask);
            }
        }
    }

    public static List<String> loadFromFile() {
        List<String> result = new ArrayList<>();
        try (Reader fileReader = new FileReader(RESOURCES_DIR + "/" + filename, StandardCharsets.UTF_8)) {

            BufferedReader br = new BufferedReader(fileReader);

            while (br.ready()) {
                String line = br.readLine();
                result.add(line);
            }
            // br.close();
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении файла: " + e.getMessage());
        }

        return result;
    }

    public static Task stringToTask(String line) {
        String[] split = line.split(",");
        Task task = new Task(split[2], split[4], Integer.valueOf(split[0]));

        return task;
    }

    public static Epic stringToEpic(String line) {
        String[] split = line.split(",");
        Epic epic = new Epic(split[2], split[4], Integer.valueOf(split[0]));
        return epic;
    }

    public static SubTask stringToSubTask(String line) {
        String[] split = line.split(",");
        SubTask subTask = new SubTask(split[2], split[4], Integer.valueOf(split[0]), Integer.valueOf(split[5]));
        return subTask;
    }
}
