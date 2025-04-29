package tracker.controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import tracker.model.Task;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler {
    TaskManager manager;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public PrioritizedHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    protected void processGet(HttpExchange httpExchange) throws IOException {
        Gson gson = new Gson();
        String responseJson = new String();
        List<Task> prioritized = manager.getPrioritizedTasks();
        System.out.println(manager.getHistory());
        responseJson = gson.toJson(prioritized);
        sendText(httpExchange, prioritized.toString());
    }

}
