import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    public static int id;
    public HashMap<Integer, Task> tasks;
    public HashMap<Integer, Epic> epics;
    public HashMap<Integer, SubTask> subTasks;

    TaskManager() {
        id = 0;
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subTasks = new HashMap<>();
    }

    public int geId() {
        id += 1;
        return id;
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void addSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        //обновляем массив подзадач для эпика
        epics.get(subTask.getEpicId()).getSubTasksList().add(subTask.getId());
    }

    public SubTask getSubTask(int subTaskId) {
        return subTasks.get(subTaskId);
    }

    public void printTasks() {
        System.out.println("Список задач:");
        for (Task task : getTasks().values()) {
            System.out.println(task.toString());
        }
    }

    public void printEpics() {
        System.out.println("Список эпиков:");
        for (Epic epic : getEpics().values()) {
            System.out.println(epic.toString());
        }
    }

    public void printSubTasks() {
        System.out.println("Список подзадач:");
        for (SubTask subTask : getSubTasks().values()) {
            System.out.println(subTask.toString());
        }
    }

    public void changeTaskStatus(Task task, Statuses status) {
        task.changeStatus(status);
        tasks.put(task.getId(), task);
    }

    public void changeSubTaskStatus(SubTask subTask, Statuses newStatus) {
        subTask.changeStatus(newStatus);
        subTasks.put(subTask.getId(), subTask);
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
            epic.changeStatus(Statuses.NEW);
        } else {
            for (Integer subtaskId : epicSubTasks) {
                SubTask subTask = getSubTask(subtaskId);
                switch (subTask.status) {
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
                epic.changeStatus(Statuses.NEW);
            } else {
                if (countDone == epicSubTasks.size()) {
                    epic.changeStatus(Statuses.DONE);
                } else {
                    epic.changeStatus(Statuses.IN_PROGRES);
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
        epics.remove(epicId);
    }

    public void removeAllEpics() {
        epics.clear();
    }

    public void removeSubTaskById(Integer subTaskId) {
        Epic epic = epics.get(subTasks.get(subTaskId).getEpicId());
        epic.getSubTasksList().remove(subTaskId);
        subTasks.remove(subTaskId);
        updateEpicStatus(epic.getId());
    }

    public void removeAllSubtasks() {
        subTasks.clear();
        for (Epic epic : getEpics().values()) {
            epic.getSubTasksList().clear();
            updateEpicStatus(epic.getId());
        }
    }
}
