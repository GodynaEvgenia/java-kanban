import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Integer> subTasksList = new ArrayList<Integer>();

    Epic(String name, String desc, int id) {
        super(name, desc, id);
        this.status = Statuses.NEW;
        this.subTasksList = new ArrayList<Integer>();

    }

    public ArrayList<Integer> getSubTasksList() {
        return subTasksList;
    }

    @Override
    public String toString() {
        return "Epic{"
                + "id=" + getId() + ", "
                + "name='" + name + "', "
                + "status=" + status + ", "
                + "subTasks=" + getSubTasksList()
                + '}';
    }
}
