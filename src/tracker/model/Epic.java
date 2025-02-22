package tracker.model;

import java.util.ArrayList;

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


    @Override
    public String toString() {
        return "tracker.model.Epic{"
                + "id=" + getId() + ", "
                + "name='" + name + "', "
                + "status=" + status + ", "
                + "subTasks=" + getSubTasksList()
                + '}';
    }
}
