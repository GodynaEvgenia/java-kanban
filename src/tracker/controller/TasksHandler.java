package tracker.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import tracker.exceptions.TaskManagerException;
import tracker.model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

public class TasksHandler extends BaseHttpHandler {
    TaskManager manager;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public TasksHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void processGet(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        Gson gson = new Gson();
        Optional<Integer> taskId = Optional.empty();
        String responseJson = new String();
        Task task;
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
    }

    @Override
    public void processPost(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        //String[] pathParts = path.split("/");
        //Gson gson = new Gson();
        //Optional<Integer> taskId = Optional.empty();
        //String responseJson = new String();
        Task task;
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
    }

    @Override
    public void processDelete(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        Optional<Integer> taskId = Optional.empty();
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
    }
    /*public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        Gson gson = new Gson();
        Optional<Integer> taskId = Optional.empty();
        String responseJson = new String();
        Task task;
        switch (method) {

            case "DELETE":

            default:
                try (OutputStream os = httpExchange.getResponseBody()) {
                    httpExchange.sendResponseHeaders(500, 0);
                    os.write("Внутренняя ошибка".getBytes(DEFAULT_CHARSET));
                }
                httpExchange.close();
        }
    }
}*/
}
