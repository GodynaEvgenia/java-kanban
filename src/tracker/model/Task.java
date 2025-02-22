package tracker.model;

public class Task {
    protected String name;
    protected String desc;
    protected int id;
    protected Statuses status;

    public Task(String name, String desc, int id) {
        this.name = name;
        this.desc = desc;
        this.id = id;
        this.status = Statuses.NEW;
    }

    public int getId() {
        return id;
    }

    public void setId(int newId) {
        this.id = newId;
    }

    public void setStatus(Statuses newStatus) {
        status = newStatus;
    }

    @Override
    public String toString() {
        return "tracker.model.Task{"
                + "name='" + name + "', "
                + "status=" + status
                + '}';
    }
}
