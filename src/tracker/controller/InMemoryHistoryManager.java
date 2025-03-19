package tracker.controller;

import tracker.model.Task;

import java.util.*;

import tracker.model.Node;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> history;
    public HandMadeLinkedList<Task> handMadeLinkedList;
    private HashMap<Integer, Node> hashMap;

    public InMemoryHistoryManager() {
        history = new ArrayList<>(10);
        hashMap = new HashMap<>();
        handMadeLinkedList = new HandMadeLinkedList<>();
    }

    class HandMadeLinkedList<T> /*extends LinkedList */ {
        private Node<T> head;
        private Node<T> tail;
        private int size = 0;

        public void linkLast(T element) {

            final Node<T> oldTail = tail;
            final Node<T> newNode = new Node<>(tail, element, null);
            tail = newNode;

            if (oldTail == null)
                head = newNode;
            else
                oldTail.next = newNode;
            size++;
        }

        public ArrayList<Task> getTasks() {
            ArrayList<Task> histAR = new ArrayList();
            Node node = handMadeLinkedList.head;
            while (node != null) {
                histAR.add((Task) node.data);
                node = node.next;
            }

            return histAR;
        }

    }

    @Override
    public void addTask(Task task) {

        Node node = hashMap.get(task.getId());
        if (node != null) {
            removeNode(node);
        }

        handMadeLinkedList.linkLast(task);
        Node newNode = handMadeLinkedList.tail;
        hashMap.remove(task.getId());
        hashMap.put(task.getId(), newNode);
    }

    @Override
    public List<Task> getHistory() {
        return handMadeLinkedList.getTasks();
    }

    @Override
    public void remove(int id) {

    }

    public void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            handMadeLinkedList.head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            handMadeLinkedList.tail = node.prev;
        }
        node.prev = null;
        node.next = null;
        handMadeLinkedList.size--;
    }
}
