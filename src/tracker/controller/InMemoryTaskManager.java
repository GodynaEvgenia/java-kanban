package tracker.controller;

import tracker.exceptions.TaskManagerException;
import tracker.model.Epic;
import tracker.model.Statuses;
import tracker.model.SubTask;
import tracker.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private static int id;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, SubTask> subTasks;
    private HistoryManager history;
    private Set<Task> prioritizedTasks = new TreeSet<>(new Comparator<Task>() {
        @Override
        public int compare(Task o1, Task o2) {
            return o1.getStartTime().compareTo(o2.getStartTime());
        }
    });

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
        if (periodIsCrossed(task.getStartTime(), task.getDuration())) {
            throw new TaskManagerException("Период пересекается с другой задачей в треккере");
        }
        final int id = getId();
        task.setId(id);
        tasks.put(id, task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
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
        if (periodIsCrossed(subTask.getStartTime(), subTask.getDuration())) {
            throw new TaskManagerException("Период пересекается с другой задачей в треккере");
        }
        final int id = getId();
        subTask.setId(id);
        subTasks.put(id, subTask);
        if (isEpicExists(subTask.getEpicId())) {
            epics.get(subTask.getEpicId()).getSubTasksList().add(subTask.getId());
            updateEpicStage(subTask.getEpicId());
            if (subTask.getStartTime() != null) {
                prioritizedTasks.add(subTask);
            }
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
        return epics.containsKey(epicId);
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
        prioritizedTasks.remove(tasks.get(taskId));
        tasks.remove(taskId);
        history.remove(taskId);
    }

    @Override
    public void removeAllTasks() {
        getTasks().forEach(task -> history.remove(task.getId()));
        getTasks().forEach(prioritizedTasks::remove);
        tasks.clear();
    }

    @Override
    public void removeEpicById(Integer epicId) {
        history.remove(epicId);
        final Epic epic = epics.remove(epicId);
        epic.getSubTasksList()
                .stream()
                .forEach(subtaskId -> {
                    subTasks.remove(subtaskId);
                    history.remove(subtaskId);
                    prioritizedTasks.remove(subTasks.get(subtaskId));
                });
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
        prioritizedTasks.remove(subTasks.get(subTaskId));
        subTasks.remove(subTaskId);
        history.remove(subTaskId);
        updateEpicStage(epic.getId());
    }

    @Override
    public void removeAllSubtasks() {
        for (Integer subTaskId : subTasks.keySet()) {
            history.remove(subTaskId);
        }
        getSubtasks()
                .stream()
                .map(st -> prioritizedTasks.remove(st));

        subTasks.clear();
        for (Epic epic : getEpics()) {
            epic.getSubTasksList().clear();
            updateEpicStage(epic.getId());
        }
    }

    public List<Task> getHistory() {
        return history.getHistory();
    }

    @Override
    public void updateEpicEndTime(int epicId) {
        Epic epic = epics.get(epicId);
        LocalDateTime maxStartTimeOfSubtasks = epic.getSubTasksList().stream()
                .map(subtask -> {
                    return subTasks.get(subtask).getEndTime();
                })
                .filter(endTime -> endTime != null)
                .max(LocalDateTime::compareTo)
                .get();
        epic.setEndTime(maxStartTimeOfSubtasks);
    }

    @Override
    public void updateEpicStartTime(int epicId) {
        Epic epic = epics.get(epicId);
        LocalDateTime minStartTimeOfSubtasks = epic.getSubTasksList().stream()
                .map(subtask -> {
                    return subTasks.get(subtask).getEndTime();
                })
                .filter(endTime -> endTime != null)
                .min(LocalDateTime::compareTo)
                .get();
        epic.setEndTime(minStartTimeOfSubtasks);
    }

    @Override
    public void updateEpicDuration(int epicId) {
        Epic epic = epics.get(epicId);
        Long sumDurationsOfSubtasks = epic.getSubTasksList().stream()
                .mapToLong(subtask -> {
                    return subTasks.get(subtask).getDuration().toMinutes();
                })
                .sum();
        epic.setDuration(Duration.ofMinutes(sumDurationsOfSubtasks));
    }

    public void updateEpicStage(int epicId) {
        updateEpicStatus(epicId);
        updateEpicStartTime(epicId);
        updateEpicEndTime(epicId);
        updateEpicDuration(epicId);
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    public boolean periodIsCrossed(LocalDateTime startTime, Duration duration) {
        if (startTime == null) {
            return false;
        }
        return getPrioritizedTasks()
                .stream()
                .anyMatch(task -> {
                    return (task.getStartTime().isBefore(startTime.plus(duration)) &&
                            startTime.isBefore(task.getEndTime()));
                });
    }
}
