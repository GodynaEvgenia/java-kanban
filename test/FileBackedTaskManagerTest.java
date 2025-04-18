package test;

import org.junit.jupiter.api.Test;
import tracker.controller.FileBackedTaskManager;
import tracker.exceptions.ManagerSaveException;

import static org.junit.jupiter.api.Assertions.assertThrows;


class FileBackedTaskManagerTest {

    @Test
    void save() {
        assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager("incorrectname.csv");
            fileBackedTaskManager.save();
        }, "Ошибка при чтении файла");
    }

}