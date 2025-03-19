package tracker.controller;

import tracker.model.Task;

import java.util.List;

public interface HistoryManager {
    public  void addTask(Task task);
    List<Task> getHistory();
    void remove(int id);
}
