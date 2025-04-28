package tracker.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import tracker.exceptions.TaskManagerException;
import tracker.model.Epic;
import tracker.model.SubTask;
import tracker.model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class HttpTaskServer {
    static final int PORT = 8081;
    static TaskManager manager;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static HttpServer httpServer;

    public HttpTaskServer(TaskManager manager) throws IOException {
        this.manager = manager;
    }

    public static void main(String[] args) throws IOException {
        manager = new InMemoryTaskManager();
        start();
    }

    public static void stop() {
        httpServer.stop(1);
    }

    public static void start() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.createContext("/subtasks", new SubtasksHandler());
        httpServer.createContext("/epics", new EpicsHandler());
        httpServer.createContext("/history", new HistoryHandler());
        httpServer.createContext("/prioritized", new PrioritizedHandler());
        httpServer.start();
    }

    static class TasksHandler extends BaseHttpHandler implements HttpHandler {

        //@Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            String path = httpExchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");
            Gson gson = new Gson();
            Optional<Integer> taskId = Optional.empty();
            String responseJson = new String();
            Task task;
            switch (method) {
                case "POST":
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    JsonElement jsonElement = JsonParser.parseString(body);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    Integer id = null;
                    if (jsonObject.has("id")) {
                        id = jsonObject.get("id").getAsInt();
                    }
                    String name = jsonObject.get("name").getAsString();
                    String desc = jsonObject.get("description").getAsString();
                    Duration duration = Duration.ofMinutes(Long.valueOf(jsonObject.get("duration").getAsInt()));
                    String startTimeStr = jsonObject.get("startTime").getAsString();
                    LocalDateTime startTime = LocalDateTime.parse(startTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

                    try {
                        if (id == null) {
                            task = new Task(name, desc, manager.getId(), duration, startTime);
                            id = manager.addNewTask(task);
                        } else {
                            task = new Task(name, desc, id, duration, startTime);
                            manager.updateTask(task);
                        }
                        sendText(httpExchange, task.toString());
                    } catch (TaskManagerException e) {
                        sendHasInterraction(httpExchange, "Задача пересекается с существующими");
                    } catch (IOException e) {
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            httpExchange.sendResponseHeaders(500, 0);
                            os.write("Внутренняя ошибка".getBytes(DEFAULT_CHARSET));
                        }
                        httpExchange.close();
                    }
                    httpExchange.close();
                case "GET":
                    if (pathParts.length == 3) {
                        taskId = Optional.of(Integer.valueOf(pathParts[2]));
                    }
                    if (!taskId.isEmpty()) {
                        try {
                            task = manager.getTask(Integer.valueOf(pathParts[2]));
                            String taskJson = gson.toJson(task);
                            sendText(httpExchange, taskJson);
                        } catch (NoSuchElementException e) {
                            sendNotFound(httpExchange, "Задача не найдена");
                        } catch (IOException e) {
                            try (OutputStream os = httpExchange.getResponseBody()) {
                                httpExchange.sendResponseHeaders(500, 0);
                                os.write("Внутренняя ошибка".getBytes(DEFAULT_CHARSET));
                            }
                            httpExchange.close();
                        }
                    } else {
                        ArrayList<Task> tasks = manager.getTasks();
                        responseJson = gson.toJson(tasks);
                        sendText(httpExchange, responseJson);
                    }
                case "DELETE":
                    if (pathParts.length == 3) {
                        taskId = Optional.of(Integer.valueOf(pathParts[2]));
                    }
                    if (!taskId.isEmpty()) {
                        try {
                            manager.removeTaskById(Integer.valueOf(pathParts[2]));
                            sendText(httpExchange, "Задача удалена");
                        } catch (IOException e) {
                            try (OutputStream os = httpExchange.getResponseBody()) {
                                httpExchange.sendResponseHeaders(500, 0);
                                os.write("Внутренняя ошибка".getBytes(DEFAULT_CHARSET));
                            }
                            httpExchange.close();
                        }
                    }
                default:
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        httpExchange.sendResponseHeaders(500, 0);
                        os.write("Внутренняя ошибка".getBytes(DEFAULT_CHARSET));
                    }
                    httpExchange.close();
            }
        }
    }

    static class SubtasksHandler extends BaseHttpHandler implements HttpHandler {

        //@Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            String path = httpExchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");
            Gson gson = new Gson();
            Optional<Integer> subTaskId = Optional.empty();
            String responseJson = new String();
            SubTask subTask;
            switch (method) {
                case "POST":
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    JsonElement jsonElement = JsonParser.parseString(body);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    Integer id = null;
                    if (jsonObject.has("id")) {
                        id = jsonObject.get("id").getAsInt();
                    }
                    String name = jsonObject.get("name").getAsString();
                    String desc = jsonObject.get("description").getAsString();
                    Duration duration = Duration.ofMinutes(Long.valueOf(jsonObject.get("duration").getAsInt()));
                    String startTimeStr = jsonObject.get("startTime").getAsString();
                    Integer epicId = jsonObject.get("epicId").getAsInt();
                    LocalDateTime startTime = LocalDateTime.parse(startTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

                    try {
                        if (id == null) {
                            subTask = new SubTask(name, desc, manager.getId(), epicId, duration, startTime);
                            id = manager.addNewSubTask(subTask);
                        } else {
                            subTask = new SubTask(name, desc, id, epicId, duration, startTime);
                            manager.updateSubTask(subTask);
                        }
                        sendText(httpExchange, subTask.toString());
                    } catch (TaskManagerException e) {
                        sendHasInterraction(httpExchange, "Подадача пересекается с существующими");
                    } catch (IOException e) {
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            httpExchange.sendResponseHeaders(500, 0);
                            os.write("Внутренняя ошибка".getBytes(DEFAULT_CHARSET));
                        }
                        httpExchange.close();
                    }
                    httpExchange.close();
                case "GET":
                    if (pathParts.length == 3) {
                        subTaskId = Optional.of(Integer.valueOf(pathParts[2]));
                    }
                    if (!subTaskId.isEmpty()) {
                        try {
                            subTask = manager.getSubTask(Integer.valueOf(pathParts[2]));
                            String taskJson = gson.toJson(subTask);
                            sendText(httpExchange, taskJson);
                        } catch (NoSuchElementException e) {
                            sendNotFound(httpExchange, "Подадача не найдена");
                        } catch (IOException e) {
                            try (OutputStream os = httpExchange.getResponseBody()) {
                                httpExchange.sendResponseHeaders(500, 0);
                                os.write("Внутренняя ошибка".getBytes(DEFAULT_CHARSET));
                            }
                            httpExchange.close();
                        }
                    } else {
                        ArrayList<SubTask> subtasks = manager.getSubtasks();
                        responseJson = gson.toJson(subtasks);
                        sendText(httpExchange, responseJson);
                    }
                case "DELETE":
                    if (pathParts.length == 3) {
                        subTaskId = Optional.of(Integer.valueOf(pathParts[2]));
                    }
                    if (!subTaskId.isEmpty()) {
                        try {
                            manager.removeSubTaskById(Integer.valueOf(pathParts[2]));
                            sendText(httpExchange, "Подзадача удалена");
                        } catch (IOException e) {
                            try (OutputStream os = httpExchange.getResponseBody()) {
                                httpExchange.sendResponseHeaders(500, 0);
                                os.write("Внутренняя ошибка".getBytes(DEFAULT_CHARSET));
                            }
                            httpExchange.close();
                        }
                    }
                default:
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        httpExchange.sendResponseHeaders(500, 0);
                        os.write("Внутренняя ошибка".getBytes(DEFAULT_CHARSET));
                    }
                    httpExchange.close();
            }
        }
    }

    static class EpicsHandler extends BaseHttpHandler implements HttpHandler {
        //@Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            String path = httpExchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");
            Gson gson = new Gson();
            Optional<Integer> subTaskId = Optional.empty();
            String responseJson = new String();
            SubTask subTask;
            switch (method) {
                case "POST":
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    JsonElement jsonElement = JsonParser.parseString(body);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    String name = jsonObject.get("name").getAsString();
                    String desc = jsonObject.get("description").getAsString();

                    try {
                        Epic epic = new Epic(name, desc, manager.getId());
                        manager.addNewEpic(epic);
                        sendText(httpExchange, epic.toString());
                    } catch (IOException e) {
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            httpExchange.sendResponseHeaders(500, 0);
                            os.write("Внутренняя ошибка".getBytes(DEFAULT_CHARSET));
                        }
                        httpExchange.close();
                    }
                    httpExchange.close();
                case "GET":
                    if (pathParts.length == 2) {
                        ArrayList<Epic> epics = manager.getEpics();
                        responseJson = gson.toJson(epics);
                        sendText(httpExchange, responseJson);
                    } else if (pathParts.length == 3) {
                        try {
                            Epic epic = manager.getEpic(Integer.valueOf(pathParts[2]));
                            String epicJson = gson.toJson(epic);
                            sendText(httpExchange, epic.toString());
                        } catch (NoSuchElementException e) {
                            sendNotFound(httpExchange, "Эпик не найден");
                        } catch (IOException e) {
                            try (OutputStream os = httpExchange.getResponseBody()) {
                                httpExchange.sendResponseHeaders(500, 0);
                                os.write("Внутренняя ошибка".getBytes(DEFAULT_CHARSET));
                            }
                            httpExchange.close();
                        }
                    } else {
                        try {
                            Epic epic = manager.getEpic(Integer.valueOf(pathParts[3]));
                            ArrayList<Integer> epicSubtasks = epic.getSubTasksList();
                            sendText(httpExchange, epicSubtasks.toString());
                        } catch (NoSuchElementException e) {
                            sendNotFound(httpExchange, "Эпик не найден");
                        } catch (IOException e) {
                            try (OutputStream os = httpExchange.getResponseBody()) {
                                httpExchange.sendResponseHeaders(500, 0);
                                os.write("Внутренняя ошибка".getBytes(DEFAULT_CHARSET));
                            }
                            httpExchange.close();
                        }
                    }
                case "DELETE":
                    try {
                        manager.removeEpicById(Integer.valueOf(pathParts[2]));
                        sendText(httpExchange, "Эпик удален");
                    } catch (IOException e) {
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            httpExchange.sendResponseHeaders(500, 0);
                            os.write("Внутренняя ошибка".getBytes(DEFAULT_CHARSET));
                        }
                        httpExchange.close();
                    }
                default:
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        httpExchange.sendResponseHeaders(500, 0);
                        os.write("Внутренняя ошибка".getBytes(DEFAULT_CHARSET));
                    }
                    httpExchange.close();
            }
        }
    }

    static class HistoryHandler extends BaseHttpHandler implements HttpHandler {
        //@Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println("hist handler");
            String method = httpExchange.getRequestMethod();
            Gson gson = new Gson();
            String responseJson = new String();
            switch (method) {
                case "GET":
                    List<Task> history = manager.getHistory();
                    System.out.println(manager.getHistory());
                    sendText(httpExchange, history.toString());
                default:
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        httpExchange.sendResponseHeaders(500, 0);
                        os.write("Внутренняя ошибка".getBytes(DEFAULT_CHARSET));
                    }
                    httpExchange.close();
            }
        }
    }


    static class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
        //@Override
        public void handle(HttpExchange httpExchange) throws IOException {

            String method = httpExchange.getRequestMethod();
            Gson gson = new Gson();
            String responseJson = new String();
            switch (method) {
                case "GET":
                    List<Task> prioritized = manager.getPrioritizedTasks();
                    System.out.println(manager.getHistory());
                    responseJson = gson.toJson(prioritized);
                    sendText(httpExchange, prioritized.toString());
                default:
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        httpExchange.sendResponseHeaders(500, 0);
                        os.write("Внутренняя ошибка".getBytes(DEFAULT_CHARSET));
                    }
                    httpExchange.close();
            }
        }
    }

}
