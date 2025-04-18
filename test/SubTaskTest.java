package test;

import org.junit.jupiter.api.Test;
import tracker.controller.InMemoryTaskManager;
import tracker.model.Epic;
import tracker.model.SubTask;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubTaskTest {
    @Test
    void addNewTask() {

        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Test epic", "Test epic description", taskManager.getId());
        SubTask subTask = new SubTask("Test checkEqualsById", "Test checkEqualsById description", taskManager.getId(), epic.getId(),
                Duration.ofMinutes(30L), LocalDateTime.of(2025, 4, 19, 10, 30));
        final int subTaskId = taskManager.addNewSubTask(subTask);
        final SubTask savedSubTask = taskManager.getSubTask(subTaskId);
        assertEquals(subTask, savedSubTask, "Экземпляры класса SubTask не совпадают");

        /*проверьте, что объект Subtask нельзя сделать своим же эпиком;*/
        int epicId = subTask.getEpicId();
        subTask.setEpicId(subTask.getId());
        assertEquals(epicId, subTask.getEpicId(), "Subtask нельзя сделать своим же эпиком");

    }
}