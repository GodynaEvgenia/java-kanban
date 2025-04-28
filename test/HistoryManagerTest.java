package test;

import org.junit.jupiter.api.Test;
import tracker.controller.InMemoryHistoryManager;
import tracker.controller.InMemoryTaskManager;
import tracker.model.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HistoryManagerTest {
    @Test
    void addTask() {
        InMemoryHistoryManager history = new InMemoryHistoryManager();
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task = new Task("Task checkEquals", "Task checkEquals description", taskManager.getId(),
                Duration.ofMinutes(30L), LocalDateTime.of(2025, 4, 19, 10, 30));
        final int taskId = taskManager.addNewTask(task);
        try {
            Task savedTask = taskManager.getTask(taskId);
            assertEquals(1, taskManager.getHistory().size(), "Ошибка при добаавлении задачи в историю");//добавлено в историю
        } catch (IOException e){

        }

    }
}