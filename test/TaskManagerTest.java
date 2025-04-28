package test;

import org.junit.jupiter.api.Test;
import tracker.controller.InMemoryTaskManager;
import tracker.model.Epic;
import tracker.model.Statuses;
import tracker.model.SubTask;
import tracker.model.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskManagerTest {

    @Test
    void getTasks() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task("Task1", "Task1 desc", taskManager.getId(),
                Duration.ofMinutes(1L), LocalDateTime.of(2025, 4, 23, 9, 30));
        Task task2 = new Task("Task2", "Task2 desc", taskManager.getId(),
                Duration.ofMinutes(1L), LocalDateTime.of(2025, 4, 23, 10, 0));
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        assertEquals(2, taskManager.getTasks().size(), "Ошибка при получении списка задач");
    }

    @Test
    void getSubtasks() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic1 = new Epic("Epic1", "Epic1", taskManager.getId());
        taskManager.addNewEpic(epic1);
        SubTask subtask1 = new SubTask("Subtask1", "Subtask1", taskManager.getId(), epic1.getId(),
                Duration.ofMinutes(1L), LocalDateTime.of(2025, 4, 23, 10, 30));
        SubTask subtask2 = new SubTask("Subtask2", "Subtask2", taskManager.getId(), epic1.getId(),
                Duration.ofMinutes(1L), LocalDateTime.of(2025, 4, 23, 11, 00));
        taskManager.addNewSubTask(subtask1);
        taskManager.addNewSubTask(subtask2);
        assertEquals(2, taskManager.getSubtasks().size(), "Ошибка при получении списка подзадач");
    }

    @Test
    void getEpics() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic1 = new Epic("Epic1", "Epic1", taskManager.getId());
        taskManager.addNewEpic(epic1);
        assertEquals(1, taskManager.getEpics().size(), "Ошибка при получении списка эпиков");
    }

    @Test
    void addNewTask() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task("Task1", "Task1 desc", taskManager.getId(),
                Duration.ofMinutes(1L), LocalDateTime.of(2025, 4, 23, 11, 30));
        taskManager.addNewTask(task1);
        assertEquals(1, taskManager.getTasks().size(), "Ошибка при создании задачи");
    }

    @Test
    void addNewEpic() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic1 = new Epic("Epic1", "Epic1", taskManager.getId());
        taskManager.addNewEpic(epic1);
        assertEquals(1, taskManager.getEpics().size(), "Ошибка при создании эпика");
    }

    @Test
    void addNewSubTask() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic1 = new Epic("Epic1", "Epic1", taskManager.getId());
        taskManager.addNewEpic(epic1);
        SubTask subtask1 = new SubTask("Subtask1", "Subtask1", taskManager.getId(), epic1.getId(),
                Duration.ofMinutes(1L), LocalDateTime.of(2025, 4, 23, 12, 0));
        taskManager.addNewSubTask(subtask1);
        assertEquals(1, taskManager.getSubtasks().size(), "Ошибка при добавлении подзадаачи");
    }

    @Test
    void getSubTask() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic1 = new Epic("Epic1", "Epic1", taskManager.getId());
        taskManager.addNewEpic(epic1);
        SubTask subtask1 = new SubTask("Subtask1", "Subtask1", taskManager.getId(), epic1.getId(),
                Duration.ofMinutes(1L), LocalDateTime.of(2025, 4, 23, 12, 30));
        taskManager.addNewSubTask(subtask1);
        assertEquals(subtask1, taskManager.getSubTask(subtask1.getId()), "Ошибка при получении подзадаачи");
    }

    @Test
    void getTask() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task("Task1", "Task1", taskManager.getId(),
                Duration.ofMinutes(1L), LocalDateTime.of(2025, 4, 23, 13, 0));
        taskManager.addNewTask(task1);
        try{
            assertEquals(task1, taskManager.getTask(task1.getId()), "Ошибка при получении задаачи");
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    @Test
    void getEpic() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic1 = new Epic("Epic1", "Epic1", taskManager.getId());
        taskManager.addNewEpic(epic1);
        assertEquals(epic1, taskManager.getEpic(epic1.getId()), "Ошибка при получении эпика");
    }

    @Test
    void isEpicExists() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Epic createTasks", "Epic createTasks description", taskManager.getId());
        final int epicId = taskManager.addNewEpic(epic);
        assertTrue(taskManager.isEpicExists(epicId));
    }

    @Test
    void updateEpicStatusAllNew() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic1 = new Epic("Epic1", "Epic1", taskManager.getId());
        taskManager.addNewEpic(epic1);
        SubTask subtask1 = new SubTask("Subtask1", "Subtask1", taskManager.getId(), epic1.getId(),
                Duration.ofMinutes(1L), LocalDateTime.of(2025, 4, 23, 13, 30));
        SubTask subtask2 = new SubTask("Subtask2", "Subtask2", taskManager.getId(), epic1.getId(),
                Duration.ofMinutes(1L), LocalDateTime.of(2025, 4, 23, 14, 00));
        taskManager.addNewSubTask(subtask1);
        taskManager.addNewSubTask(subtask2);
        //taskManager.changeSubTaskStatus(subtask1, Statuses.IN_PROGRES);
        assertEquals(Statuses.NEW, epic1.getStatus(epic1.getId()), "Ошибка при обновлении статуса эпика");
    }

    @Test
    void updateEpicStatusAllDone() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic1 = new Epic("Epic1", "Epic1", taskManager.getId());
        taskManager.addNewEpic(epic1);
        SubTask subtask1 = new SubTask("Subtask1", "Subtask1", taskManager.getId(), epic1.getId(),
                Duration.ofMinutes(1L), LocalDateTime.of(2025, 4, 23, 13, 30));
        SubTask subtask2 = new SubTask("Subtask2", "Subtask2", taskManager.getId(), epic1.getId(),
                Duration.ofMinutes(1L), LocalDateTime.of(2025, 4, 23, 14, 00));
        taskManager.addNewSubTask(subtask1);
        taskManager.addNewSubTask(subtask2);
        taskManager.changeSubTaskStatus(subtask1, Statuses.DONE);
        taskManager.changeSubTaskStatus(subtask2, Statuses.DONE);
        assertEquals(Statuses.DONE, epic1.getStatus(epic1.getId()), "Ошибка при обновлении статуса эпика");
    }

    @Test
    void updateEpicStatusNewAndDone() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic1 = new Epic("Epic1", "Epic1", taskManager.getId());
        taskManager.addNewEpic(epic1);
        SubTask subtask1 = new SubTask("Subtask1", "Subtask1", taskManager.getId(), epic1.getId(),
                Duration.ofMinutes(1L), LocalDateTime.of(2025, 4, 23, 13, 30));
        SubTask subtask2 = new SubTask("Subtask2", "Subtask2", taskManager.getId(), epic1.getId(),
                Duration.ofMinutes(1L), LocalDateTime.of(2025, 4, 23, 14, 00));
        taskManager.addNewSubTask(subtask1);
        taskManager.addNewSubTask(subtask2);
        taskManager.changeSubTaskStatus(subtask1, Statuses.DONE);
        assertEquals(Statuses.IN_PROGRES, epic1.getStatus(epic1.getId()), "Ошибка при обновлении статуса эпика");
    }

    @Test
    void updateEpicStatusInProgress() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic1 = new Epic("Epic1", "Epic1", taskManager.getId());
        taskManager.addNewEpic(epic1);
        SubTask subtask1 = new SubTask("Subtask1", "Subtask1", taskManager.getId(), epic1.getId(),
                Duration.ofMinutes(1L), LocalDateTime.of(2025, 4, 23, 13, 30));
        SubTask subtask2 = new SubTask("Subtask2", "Subtask2", taskManager.getId(), epic1.getId(),
                Duration.ofMinutes(1L), LocalDateTime.of(2025, 4, 23, 14, 00));
        taskManager.addNewSubTask(subtask1);
        taskManager.addNewSubTask(subtask2);
        taskManager.changeSubTaskStatus(subtask1, Statuses.IN_PROGRES);
        taskManager.changeSubTaskStatus(subtask2, Statuses.IN_PROGRES);
        assertEquals(Statuses.IN_PROGRES, epic1.getStatus(epic1.getId()), "Ошибка при обновлении статуса эпика");
    }

    @Test
    void periodIsCrossed() {
        LocalDateTime period1StartTime = LocalDateTime.of(2025, 4, 23, 14, 00);
        Duration period1 = Duration.ofMinutes(30L);

        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task("Task1", "Task1", taskManager.getId(),
                Duration.ofMinutes(10L), LocalDateTime.of(2025, 4, 23, 14, 0));
        taskManager.addNewTask(task1);
        assertTrue(taskManager.periodIsCrossed(period1StartTime, period1));
    }

    @Test
    void updateEpicEndTime() {
    }

    @Test
    void updateEpicStartTime() {
    }

    @Test
    void updateEpicDuration() {
    }
}