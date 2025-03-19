package test;

import org.junit.jupiter.api.Test;
import tracker.controller.InMemoryTaskManager;
import tracker.model.Epic;
import tracker.model.Task;





import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    @Test
    void addNewTask() {

        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description", taskManager.getId());
        final int epicId = taskManager.addNewEpic(epic);
        final Task savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Эпик не найден");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");

        /*проверьте, что объект Epic нельзя добавить в самого себя в виде подзадачи;*/
        epic.addSubTask(epic.getId());
        assertEquals(0, epic.getSubTasksList().size(), "Эпик нельзя добавить в список подзадаач");

    }
}