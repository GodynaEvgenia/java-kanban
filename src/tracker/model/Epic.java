package tracker.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subTasksList = new ArrayList<Integer>();
    private LocalDateTime endTime;

    public Epic(String name, String desc, int id) {
        super(name, desc, id, null, null);
        this.status = Statuses.NEW;
        this.subTasksList = new ArrayList<Integer>();
    }

    public ArrayList<Integer> getSubTasksList() {
        return subTasksList;
    }

    public void addSubTask(int subtaskId) {
        if (subtaskId != getId()) {
            subTasksList.add(subtaskId);
        }
    }

    @Override
    public String toString() {
        Long durationToMinutes = null;
        if (duration != null) {
            durationToMinutes = duration.toMinutes();
        }
        return id + ","
                + TasksType.EPIC + ","
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
        Epic epic = (Epic) o;
        return Objects.equals(name, epic.name) &&
                Objects.equals(desc, epic.desc) &&
                (id == epic.id);
    }

    @Override
    public int hashCode() {
        // вызываем вспомогательный метод и передаём в него нужные поля
        return Objects.hash(name, desc, id);
    }

    public Statuses getStatus(int epicId) {
        return this.status;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setStartTime(LocalDateTime endTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

}
