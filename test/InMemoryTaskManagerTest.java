package test;

import org.junit.jupiter.api.Test;
import tracker.controller.InMemoryTaskManager;
import tracker.model.Epic;
import tracker.model.SubTask;
import tracker.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryTaskManagerTest {
    @Test
    void createInMemoryTaskManager() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        assertNotNull(taskManager, " InMemoryTaskManager не проинициализирован");
    }

    @Test
    void createTasks() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task = new Task("Task createTasks", "Task createTasks description", taskManager.getId(),
                Duration.ofMinutes(30L), LocalDateTime.of(2025, 4, 23, 10, 30));
        final int taskId = taskManager.addNewTask(task);
        final Task savedTask = taskManager.getTask(task.getId());
        assertEquals(1, taskManager.getTasks().size(), "Task не создан");

        Epic epic = new Epic("Epic createTasks", "Epic createTasks description", taskManager.getId());
        final int epicId = taskManager.addNewEpic(epic);
        final Task savedEpic = taskManager.getEpic(epicId);
        assertEquals(1, taskManager.getEpics().size(), "Epic не создан");

        SubTask subTask = new SubTask("SubTask createTasks", "SubTask createTasks description", taskManager.getId(), epic.getId(),
                Duration.ofMinutes(30L), LocalDateTime.of(2025, 4, 22, 10, 30));
        final int subtaskId = taskManager.addNewSubTask(subTask);
        final SubTask savedSubTask = taskManager.getSubTask(subtaskId);
        assertEquals(1, taskManager.getSubtasks().size(), "Epic не создан");
    }

    @Test
    void checkIds() {
        /*проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;*/
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task = new Task("Task checkIds", "Task checkIds description", taskManager.getId(),
                Duration.ofMinutes(30L), LocalDateTime.of(2025, 4, 21, 10, 30));
        final int taskId = taskManager.addNewTask(task);

        Task task2 = new Task("Task2 checkIds", "Task2 checkIds description", taskId,
                Duration.ofMinutes(30L), LocalDateTime.of(2025, 4, 20, 10, 30));
        final int task2Id = taskManager.addNewTask(task2);
        assertFalse(taskId == task2Id);

    }
}