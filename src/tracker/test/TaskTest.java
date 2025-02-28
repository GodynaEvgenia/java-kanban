package tracker.test;

import org.junit.jupiter.api.Test;
import tracker.controller.InMemoryTaskManager;
import tracker.model.Task;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void checkEqualsById(){
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task = new Task("Test checkEqualsById", "Test checkEqualsById description", taskManager.getId());
        final int taskId = taskManager.addNewTask(task);
        final Task savedTask = taskManager.getTask(task.getId());

        assertEquals(task, savedTask, "Экземпляры класса Task не совпадают");
        assertNotNull(savedTask, " Таск не найден");

    }

}