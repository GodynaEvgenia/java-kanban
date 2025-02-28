package tracker.controller;

import tracker.model.Epic;
import tracker.model.Statuses;
import tracker.model.SubTask;
import tracker.model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    int getId();

    ArrayList<Task> getTasks();

    ArrayList<SubTask> getSubtasks();

    ArrayList<Epic> getEpics();

    int addNewTask(Task task);

    int addNewEpic(Epic epic);

    int addNewSubTask(SubTask subTask);

    SubTask getSubTask(int subTaskId);

    Task getTask(int taskId);

    Epic getEpic(int epicId);

    boolean isEpicExists(int epicId);

    void changeSubTaskStatus(SubTask subTask, Statuses newStatus);

    void updateEpicStatus(int epicId);

    void removeTaskById(Integer taskId);

    void removeAllTasks();

    void removeEpicById(Integer epicId);

    void removeAllEpics();

    void removeSubTaskById(Integer subTaskId);

    void removeAllSubtasks();

    List<Task> getHistory();

}
