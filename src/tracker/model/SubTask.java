package tracker.model;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String desc, int id, int epicId) {
        super(name, desc, id);
        this.epicId = epicId;
        //this.status = status;
    }

    public int getEpicId() {
        return epicId;
    }

    public Statuses getStatus() {
        return status;
    }

    @Override
    public void setStatus(Statuses newStatus) {
        status = newStatus;
    }

    @Override
    public String toString() {
        return "tracker.model.SubTask{"
                + "id=" + getId() + ", "
                + "name='" + name + "', "
                + "status=" + status
                + '}';
    }
}
