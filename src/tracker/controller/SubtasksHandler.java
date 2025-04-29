package tracker.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import tracker.exceptions.TaskManagerException;
import tracker.model.SubTask;

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

public class SubtasksHandler extends BaseHttpHandler {
    TaskManager manager;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public SubtasksHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    protected void processGet(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        Gson gson = new Gson();
        Optional<Integer> subTaskId = Optional.empty();
        String responseJson = new String();
        SubTask subTask;

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
    }

    @Override
    protected void processPost(HttpExchange httpExchange) throws IOException {
        SubTask subTask;

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
    }

    @Override
    protected void processDelete(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        Optional<Integer> subTaskId = Optional.empty();
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
    }
}
