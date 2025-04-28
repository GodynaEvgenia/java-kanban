package test;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import tracker.controller.HttpTaskServer;
import tracker.controller.InMemoryTaskManager;
import tracker.controller.TaskManager;
import tracker.model.Epic;
import tracker.model.SubTask;
import tracker.model.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer ;

    @BeforeEach
    public void setUp() throws IOException{
        taskServer = new HttpTaskServer(manager);
        manager.removeAllTasks();
        manager.removeAllSubtasks();
        manager.removeAllEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testGetTask() throws IOException, InterruptedException {
        Task task = new Task("Test 1", "Testing task 1",1,
                Duration.ofMinutes(5L), LocalDateTime.now());

        manager.addNewTask(task);
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ArrayList<Task> tasksFromManager = manager.getTasks();
        assertEquals(200, response.statusCode());
        assertNotNull(tasksFromManager, "Задача не возвращается");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество подзадач");
    }

    @Test
    public void testGetSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Epic 1", 1);
        manager.addEpic(epic);
        SubTask subTask = new SubTask("Test 1", "Testing task 1",2, 1,
                Duration.ofMinutes(5L), LocalDateTime.now());

        manager.addNewSubTask(subTask);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ArrayList<SubTask> subtasksFromManager = manager.getSubtasks();
        assertEquals(200, response.statusCode());
        assertNotNull(subtasksFromManager, "Подзадача не возвращается");
        assertEquals(1, subtasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    public void testGetEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Epic 1", 1);
        manager.addEpic(epic);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/epics/1");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ArrayList<Epic> epicsFromManager = manager.getEpics();
        assertEquals(200, response.statusCode());
        assertNotNull(epicsFromManager, "Эпик не возвращается");
        assertEquals(1, epicsFromManager.size(), "Некорректное количество эпиков");
    }
}