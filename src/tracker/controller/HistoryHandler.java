package tracker.controller;

import com.sun.net.httpserver.HttpExchange;
import tracker.model.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler {
    TaskManager manager;

    public HistoryHandler(TaskManager manager) {
        this.manager = manager;
    }

    //@Override
    protected void processGet(HttpExchange httpExchange) throws IOException {
        List<Task> history = manager.getHistory();
        System.out.println(manager.getHistory());
        sendText(httpExchange, history.toString());
    }
}
