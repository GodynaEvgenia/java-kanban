package tracker.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import tracker.model.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class EpicsHandler extends BaseHttpHandler {
    TaskManager manager;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public EpicsHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    protected void processGet(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        Gson gson = new Gson();
        String responseJson = new String();

        if (pathParts.length == 2) {
            ArrayList<Epic> epics = manager.getEpics();
            responseJson = gson.toJson(epics);
            sendText(httpExchange, responseJson);
        } else if (pathParts.length == 3) {
            try {
                Epic epic = manager.getEpic(Integer.valueOf(pathParts[2]));
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
    }

    @Override
    protected void processPost(HttpExchange httpExchange) throws IOException {
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
    }

    @Override
    protected void processDelete(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
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
    }

}
