package tracker.controller;

import tracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager {
    private HashMap<Integer, Node> hashMap;
    private Node head;
    private Node tail;

    public InMemoryHistoryManager() {
        hashMap = new HashMap<>();
    }

    class Node {
        public Task data;
        public Node next;
        public Node prev;

        public Node(Node prev, Task data, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    public void linkLast(Task element) {
        final Node oldTail = tail;
        final Node newNode = new Node(tail, element, null);
        tail = newNode;

        if (oldTail == null)
            head = newNode;
        else
            oldTail.next = newNode;
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> histAR = new ArrayList();
        Node node = head;
        while (node != null) {
            histAR.add((Task) node.data);
            node = node.next;
        }

        return histAR;
    }

    @Override
    public void addTask(Task task) {
        Node node = hashMap.get(task.getId());
        if (node != null) {
            removeNode(node);
        }

        linkLast(task);
        Node newNode = tail;
        hashMap.remove(task.getId());
        hashMap.put(task.getId(), newNode);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        Node node = hashMap.get(id);
        if (node != null) {
            removeNode(node);
            hashMap.remove(node);
        }
    }

    public void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
        node.prev = null;
        node.next = null;
    }
}
