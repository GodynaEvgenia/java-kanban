package test;

import org.junit.jupiter.api.Test;
import tracker.controller.InMemoryTaskManager;
import tracker.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TaskTest {

    @Test
    void checkEqualsById() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task = new Task("Test checkEqualsById", "Test checkEqualsById description", taskManager.getId(),
                Duration.ofMinutes(30L), LocalDateTime.of(2025, 4, 19, 10, 30));
        final int taskId = taskManager.addNewTask(task);
        final Task savedTask = taskManager.getTask(task.getId());

        assertEquals(task, savedTask, "Экземпляры класса Task не совпадают");
        assertNotNull(savedTask, " Таск не найден");

    }

}