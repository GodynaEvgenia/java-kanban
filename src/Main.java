import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        /*Создайте две задачи, а также эпик с двумя подзадачами и эпик с одной подзадачей.*/
        Task task1 = new Task("Обучение", "Сдать все работы на платформе Practicum до дедлайна", taskManager.geId());
        Task task2 = new Task("Бег", "Вернуть полезную привычку в жизнь", taskManager.geId());
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Восхождение на Эльбрус", "Восхождение на Эльбрус", taskManager.geId());
        taskManager.addEpic(epic1);

        SubTask subtask1 = new SubTask("Купить снаряжение и одежду", "Купить снаряжение и одежду", taskManager.geId(), epic1.getId());
        SubTask subtask2 = new SubTask("Купить авиабилеты", "Купить авиабилеты", taskManager.geId(), epic1.getId());
        taskManager.addSubTask(subtask1);
        taskManager.addSubTask(subtask2);

        Epic epic2 = new Epic("Сбросить 5 кг", "Сбросить 5 кг", taskManager.geId());
        taskManager.addEpic(epic2);

        SubTask subTask3 = new SubTask("Составить меню", "Составить меню", taskManager.geId(), epic2.getId());
        taskManager.addSubTask(subTask3);

        Epic epic3 = new Epic("Ремонт", "Ремонт", taskManager.geId());
        taskManager.addEpic(epic3);
        SubTask subTask4 = new SubTask("Купить краску", "Купить краску", taskManager.geId(), epic3.getId());
        SubTask subTask5 = new SubTask("Заказать двери", "Закаать двери", taskManager.geId(), epic3.getId());
        taskManager.addSubTask(subTask4);
        taskManager.addSubTask(subTask5);

        /*Распечатайте списки эпиков, задач и подзадач*/
        taskManager.printTasks();
        taskManager.printEpics();
        taskManager.printSubTasks();

        /*Измените статусы созданных объектов, распечатайте их.
        Проверьте, что статус задачи и подзадачи сохранился,
         а статус эпика рассчитался по статусам подзадач.*/
        taskManager.changeTaskStatus(task1, Statuses.IN_PROGRES);
        taskManager.changeTaskStatus(task2, Statuses.IN_PROGRES);
        taskManager.changeSubTaskStatus(subtask1, Statuses.IN_PROGRES);
        taskManager.changeSubTaskStatus(subTask3, Statuses.IN_PROGRES);

        taskManager.printTasks();
        taskManager.printEpics();
        taskManager.printSubTasks();

        taskManager.changeSubTaskStatus(subTask3, Statuses.DONE);
        taskManager.printEpics();

        /*И, наконец, попробуйте удалить одну из задач и один из эпиков.*/
        taskManager.removeTaskById(task1.getId());
        taskManager.removeEpicById(epic2.getId());

        taskManager.printTasks();
        taskManager.printEpics();
        taskManager.printSubTasks();

        taskManager.removeAllSubtasks();
        taskManager.printTasks();
        taskManager.printEpics();
        taskManager.printSubTasks();

    }
}
