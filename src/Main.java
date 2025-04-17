import tracker.controller.FileBackedTaskManager;
import tracker.controller.InMemoryTaskManager;
import tracker.model.Epic;
import tracker.model.Statuses;
import tracker.model.SubTask;
import tracker.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new FileBackedTaskManager("saved.csv");//InMemoryTaskManager(); FileBackedTaskManager("saved.csv")

        printTasks(taskManager.getTasks());
        printEpics(taskManager.getEpics());
        printSubTasks(taskManager.getSubtasks());

        /*Создайте две задачи, а также эпик с двумя подзадачами и эпик с одной подзадачей.*/
        Task task1 = new Task("Обучение", "Сдать все работы на платформе Practicum до дедлайна", taskManager.getId(),
                Duration.ofMinutes(30L), LocalDateTime.of(2025, 4, 16, 9, 30));
        Task task2 = new Task("Бег", "Вернуть полезную привычку в жизнь", taskManager.getId(),
                Duration.ofMinutes(200L), LocalDateTime.of(2025, 4, 16, 12, 30));
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);

        Epic epic1 = new Epic("Восхождение", "Восхождение на Эльбрус", taskManager.getId());
        taskManager.addNewEpic(epic1);

        SubTask subtask1 = new SubTask("Купить снаряжение и одежду", "Купить снаряжение и одежду", taskManager.getId(), epic1.getId(),
                Duration.ofMinutes(60L), LocalDateTime.of(2025, 4, 16, 19, 00));
        SubTask subtask2 = new SubTask("Купить авиабилеты", "Купить авиабилеты", taskManager.getId(), epic1.getId(),
                Duration.ofMinutes(200L), LocalDateTime.of(2025, 4, 15, 8, 00));
        taskManager.addNewSubTask(subtask1);
        taskManager.addNewSubTask(subtask2);


        System.out.println("Пересечение: ");

        Epic epic2 = new Epic("Сбросить_вес", "Сбросить 5 кг", taskManager.getId());
        taskManager.addNewEpic(epic2);

        SubTask subTask3 = new SubTask("Составить меню", "Составить меню", taskManager.getId(), epic2.getId(),
                Duration.ofMinutes(20L), LocalDateTime.of(2025, 4, 18, 9, 30));
        taskManager.addNewSubTask(subTask3);

        Epic epic3 = new Epic("Ремонт", "Ремонт", taskManager.getId());
        taskManager.addNewEpic(epic3);
        SubTask subTask4 = new SubTask("Купить краску", "Купить краску", taskManager.getId(), epic3.getId(),
                Duration.ofMinutes(30L), LocalDateTime.of(2025, 4, 19, 12, 30));
        SubTask subTask5 = new SubTask("Заказать двери", "Закаать двери", taskManager.getId(), epic3.getId(),
                Duration.ofMinutes(30L), LocalDateTime.of(2025, 4, 19, 10, 30));
        taskManager.addNewSubTask(subTask4);
        taskManager.addNewSubTask(subTask5);

        taskManager.removeSubTaskById(subtask2.getId());

        /*Распечатайте списки эпиков, задач и подзадач*/
        printTasks(taskManager.getTasks());
        printEpics(taskManager.getEpics());
        printSubTasks(taskManager.getSubtasks());

        //просмотр
        Task task = taskManager.getTask(task1.getId());
        task = taskManager.getTask(task2.getId());
        task = taskManager.getTask(task1.getId());
        task = taskManager.getTask(task2.getId());
        task = taskManager.getSubTask(subtask1.getId());
        task = taskManager.getSubTask(subTask4.getId());
        task = taskManager.getEpic(epic3.getId());

        /*Измените статусы созданных объектов, распечатайте их.
        Проверьте, что статус задачи и подзадачи сохранился,
         а статус эпика рассчитался по статусам подзадач.*/
        task1.setStatus(Statuses.IN_PROGRES);
        task2.setStatus(Statuses.IN_PROGRES);
        taskManager.changeSubTaskStatus(subtask1, Statuses.IN_PROGRES);
        taskManager.changeSubTaskStatus(subTask3, Statuses.IN_PROGRES);

        printHistory(taskManager);

        taskManager.removeAllEpics();

        printHistory(taskManager);

    }

    public static void printTasks(ArrayList<Task> tasks) {
        System.out.println("Список задач:");
        for (Task task : tasks) {
            System.out.println(task.toString());
        }
    }

    public static void printEpics(ArrayList<Epic> epics) {
        System.out.println("Список эпиков:");
        for (Epic epic : epics) {
            System.out.println(epic.toString());
        }
    }

    public static void printSubTasks(ArrayList<SubTask> subTasks) {
        System.out.println("Список подзадач:");
        for (SubTask subTask : subTasks) {
            System.out.println(subTask.toString());
        }
    }

    private static void printHistory(InMemoryTaskManager taskManager) {
        System.out.println("История:");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
    }
}
