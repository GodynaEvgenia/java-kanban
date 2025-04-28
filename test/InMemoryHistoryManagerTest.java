package test;

import org.junit.jupiter.api.Test;
import tracker.controller.InMemoryHistoryManager;
import tracker.controller.InMemoryTaskManager;
import tracker.model.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryHistoryManagerTest {
    @Test
    void createInMemoryHistoryManager() {
        InMemoryHistoryManager history = new InMemoryHistoryManager();
        assertNotNull(history, " InMemoryHistoryManager не проинициализирован");
    }

    @Test
    void checkEquals() {
        InMemoryHistoryManager history = new InMemoryHistoryManager();
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task = new Task("Task checkEquals", "Task checkEquals description", taskManager.getId(),
                Duration.ofMinutes(30L), LocalDateTime.of(2025, 4, 19, 10, 30));
        final int taskId = taskManager.addNewTask(task);
        try{
            Task savedTask = taskManager.getTask(taskId);//добавлено в историю
            Boolean eq = task.equals(savedTask);

            assertTrue(task.equals(savedTask), "Не эквивалентны");
        } catch(IOException ex){
            ex.printStackTrace();
        }

    }
}