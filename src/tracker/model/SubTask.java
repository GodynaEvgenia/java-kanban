package tracker.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String desc, int id, int epicId, Duration duration, LocalDateTime startTime) {
        super(name, desc, id, duration, startTime);
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
        Long durationToMinutes = null;
        if (duration != null) {
            durationToMinutes = duration.toMinutes();
        }
        return id + ","
                + getClass().getSimpleName().toUpperCase() + ","
                + name + ","
                + status + ","
                + desc + ","
                + getEpicId() + ","
                + durationToMinutes + ","
                + startTime;
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
