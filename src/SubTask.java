public class SubTask extends Task {
    int epicId;

    SubTask(String name, String desc, int id, int epicId) {
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
    public void changeStatus(Statuses newStatus) {
        status = newStatus;
    }

    @Override
    public String toString() {
        return "SubTask{"
                + "id=" + getId() + ", "
                + "name='" + name + "', "
                + "status=" + status
                + '}';
    }
}
