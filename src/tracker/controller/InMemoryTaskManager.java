package tracker.controller;

import tracker.model.Epic;
import tracker.model.Statuses;
import tracker.model.SubTask;
import tracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private static int id;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, SubTask> subTasks;
    private HistoryManager history;

    public InMemoryTaskManager() {
        id = 0;
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subTasks = new HashMap<>();
        history = new InMemoryHistoryManager();
    }

    public int getId() {
        id += 1;
        return id;
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<SubTask> getSubtasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public int addNewTask(Task task) {
        final int id = getId();
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    @Override
    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addSubtask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
    }

    @Override
    public int addNewEpic(Epic epic) {
        final int id = getId();
        epic.setId(id);
        epics.put(id, epic);
        return id;
    }

    @Override
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

    @Override
    public SubTask getSubTask(int subTaskId) {
        if (subTasks.containsKey(subTaskId)) {
            history.addTask(subTasks.get(subTaskId));
        }
        return subTasks.get(subTaskId);
    }

    @Override
    public Task getTask(int taskId) {
        if (tasks.containsKey(taskId)) {
            history.addTask(tasks.get(taskId));
        }
        return tasks.get(taskId);
    }

    @Override
    public Epic getEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            history.addTask(epics.get(epicId));
        }
        return epics.get(epicId);
    }

    @Override
    public boolean isEpicExists(int epicId) {
        for (Epic epic : epics.values()) {
            if (epic.getId() == epicId) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void changeSubTaskStatus(SubTask subTask, Statuses newStatus) {
        subTask.setStatus(newStatus);
        //статус эпика
        updateEpicStatus(subTask.getEpicId());
    }

    @Override
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
                SubTask subTask = subTasks.get(subtaskId);
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

    @Override
    public void removeTaskById(Integer taskId) {
        tasks.remove(taskId);
        history.remove(taskId);
    }

    @Override
    public void removeAllTasks() {
        for (Integer taskId : tasks.keySet()) {
            history.remove(taskId);
        }
        tasks.clear();
    }

    @Override
    public void removeEpicById(Integer epicId) {
        history.remove(epicId);
        final Epic epic = epics.remove(epicId);
        for (Integer subtaskId : epic.getSubTasksList()) {
            subTasks.remove(subtaskId);
            history.remove(subtaskId);
        }
    }

    @Override
    public void removeAllEpics() {
        for (Integer epicId : epics.keySet()) {
            history.remove(epicId);
        }
        for (Integer subTaskId : subTasks.keySet()) {
            history.remove(subTaskId);
        }
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void removeSubTaskById(Integer subTaskId) {
        Epic epic = epics.get(subTasks.get(subTaskId).getEpicId());
        epic.getSubTasksList().remove(subTaskId);
        subTasks.remove(subTaskId);
        history.remove(subTaskId);
        updateEpicStatus(epic.getId());
    }

    @Override
    public void removeAllSubtasks() {
        for (Integer subTaskId : subTasks.keySet()) {
            history.remove(subTaskId);
        }
        subTasks.clear();
        for (Epic epic : getEpics()) {
            epic.getSubTasksList().clear();
            updateEpicStatus(epic.getId());
        }
    }

    public List<Task> getHistory() {
        return history.getHistory();
    }

}
