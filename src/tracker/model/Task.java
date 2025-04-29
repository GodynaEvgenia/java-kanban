package tracker.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected String name;
    protected String desc;
    protected int id;
    protected Statuses status;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Task(String name, String desc, int id, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.desc = desc;
        this.id = id;
        this.status = Statuses.NEW;
        this.duration = duration;
        this.startTime = startTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int newId) {
        this.id = newId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setStatus(Statuses newStatus) {
        this.status = newStatus;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
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
                + ","
                + durationToMinutes + ","
                + startTime;
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

    public String getName() {
        return this.name;
    }

    public String getDesc() {
        return this.desc;
    }

    public Statuses getStatus() {
        return this.status;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public LocalDateTime getEndTime() {
        return this.startTime.plus(this.duration);
    }

    public Duration getDuration() {
        return this.duration;
    }
}
