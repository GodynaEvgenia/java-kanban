package tracker.test;

import org.junit.jupiter.api.Test;
import tracker.controller.InMemoryHistoryManager;
import tracker.controller.InMemoryTaskManager;
import tracker.model.Task;

import static org.junit.jupiter.api.Assertions.*;

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
        Task task = new Task("Task checkEquals", "Task checkEquals description", taskManager.getId());
        final int taskId = taskManager.addNewTask(task);

        Task savedTask = taskManager.getTask(taskId);//добавлено в историю
        Boolean eq = task.equals(savedTask);

        assertTrue(task.equals(savedTask), "Не эквивалентны");
    }
}