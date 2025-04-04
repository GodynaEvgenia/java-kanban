package tracker.model;

import java.util.Objects;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String desc, int id, int epicId) {
        super(name, desc, id);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int newEpicId) {
        if (newEpicId != id) {
            epicId = newEpicId;
        }
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
        return id + ","
                + getClass().getSimpleName().toUpperCase() + ","
                +  name + ","
                +  status + ","
                + desc + ","
                + getEpicId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubTask subTask = (SubTask) o;
        return Objects.equals(name, subTask.name) &&
                Objects.equals(desc, subTask.desc) &&
                (id == subTask.id);
    }

    @Override
    public int hashCode() {
        // вызываем вспомогательный метод и передаём в него нужные поля
        return Objects.hash(name, desc, id);
    }
}
