public class Task {
    String name;
    String desc;
    int id;
    Statuses status;

    Task(String name, String desc, int id) {
        this.name = name;
        this.desc = desc;
        this.id = id;
        this.status = Statuses.NEW;
    }

    public int getId() {
        return id;
    }

    public void changeStatus(Statuses newStatus) {
        status = newStatus;
    }

    @Override
    public String toString() {
        return "Task{"
                + "name='" + name + "', "
                + "status=" + status
                + '}';
    }
}
