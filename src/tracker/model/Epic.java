package tracker.model;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subTasksList = new ArrayList<Integer>();

    public Epic(String name, String desc, int id) {
        super(name, desc, id);
        this.status = Statuses.NEW;
        this.subTasksList = new ArrayList<Integer>();
    }

    public ArrayList<Integer> getSubTasksList() {
        return subTasksList;
    }

    public void addSubTask(int subtaskId){
        if (subtaskId != getId()){
            subTasksList.add(subtaskId);
        }
    }

    @Override
    public String toString() {
        return "tracker.model.Epic{"
                + "id=" + getId() + ", "
                + "name='" + name + "', "
                + "status=" + status + ", "
                + "subTasks=" + getSubTasksList()
                + '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return Objects.equals(name, epic.name) &&
                Objects.equals(desc, epic.desc) &&
                (id == epic.id) ;
    }

    @Override
    public int hashCode() {
        // вызываем вспомогательный метод и передаём в него нужные поля
        return Objects.hash(name, desc, id);
    }
}
