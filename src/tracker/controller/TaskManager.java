package tracker.controller;

import tracker.model.Epic;
import tracker.model.Statuses;
import tracker.model.SubTask;
import tracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;


public class TaskManager {
    private static int id;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, SubTask> subTasks;

    public TaskManager() {
        id = 0;
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subTasks = new HashMap<>();
    }

    public int getId() {
        id += 1;
        return id;
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<SubTask> getSubtasks() {
        return new ArrayList<>(subTasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public int addNewTask(Task task) {
        final int id = getId();
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    public int addNewEpic(Epic epic) {
        final int id = getId();
        epic.setId(id);
        epics.put(id, epic);
        return id;
    }

    public int addNewSubTask(SubTask subTask) {
        final int id = getId();
        subTask.setId(id);
        subTasks.put(id, subTask);
        if (isEpicExists(subTask.getEpicId())) {
            epics.get(subTask.getEpicId()).getSubTasksList().add(subTask.getId());
            updateEpicStatus(subTask.getEpicId());
        }
        return id;
    }

    public SubTask getSubTask(int subTaskId) {
        return subTasks.get(subTaskId);
    }

    public boolean isEpicExists(int epicId) {
        for (Epic epic : epics.values()) {
            if (epic.getId() == epicId) {
                return true;
            }
        }
        return false;
    }

    public void changeSubTaskStatus(SubTask subTask, Statuses newStatus) {
        subTask.setStatus(newStatus);
        //статус эпика
        updateEpicStatus(subTask.getEpicId());
    }

    public void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Integer> epicSubTasks = epic.getSubTasksList();
        int countNew = 0;
        int countInProgres = 0;
        int countDone = 0;
        if (epicSubTasks.size() == 0) {
            epic.setStatus(Statuses.NEW);
        } else {
            for (Integer subtaskId : epicSubTasks) {
                SubTask subTask = getSubTask(subtaskId);
                switch (subTask.getStatus()) {
                    case NEW:
                        countNew += 1;
                        break;
                    case IN_PROGRES:
                        countInProgres += 1;
                        break;
                    case DONE:
                        countDone += 1;
                        break;
                    default:
                        break;
                }
            }
            if (countNew == epicSubTasks.size()) {
                epic.setStatus(Statuses.NEW);
            } else {
                if (countDone == epicSubTasks.size()) {
                    epic.setStatus(Statuses.DONE);
                } else {
                    epic.setStatus(Statuses.IN_PROGRES);
                }
            }
        }
    }

    public void removeTaskById(Integer taskId) {
        tasks.remove(taskId);
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeEpicById(Integer epicId) {
        final Epic epic = epics.remove(epicId);
        for (Integer subtaskId : epic.getSubTasksList()) {
            subTasks.remove(subtaskId);
        }
    }

    public void removeAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    public void removeSubTaskById(Integer subTaskId) {
        Epic epic = epics.get(subTasks.get(subTaskId).getEpicId());
        epic.getSubTasksList().remove(subTaskId);
        subTasks.remove(subTaskId);
        updateEpicStatus(epic.getId());
    }

    public void removeAllSubtasks() {
        subTasks.clear();
        for (Epic epic : getEpics()) {
            epic.getSubTasksList().clear();
            updateEpicStatus(epic.getId());
        }
    }
}
