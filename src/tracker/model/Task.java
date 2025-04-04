package tracker.model;

import java.util.Objects;

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
        return id + ","
                + getClass().getSimpleName().toUpperCase() + ","
                +  name + ","
                +  status +","
                + desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) &&
                Objects.equals(desc, task.desc) &&
                (id == task.id) &&
                (status == task.status);
    }

    @Override
    public int hashCode() {
        // вызываем вспомогательный метод и передаём в него нужные поля
        return Objects.hash(name, desc, id, status);
    }
}
