package tracker.controller;

import tracker.model.Task;

import java.util.List;

public interface HistoryManager {
    public  void addTask(Task task);
    public List<Task> getHistory();
}
