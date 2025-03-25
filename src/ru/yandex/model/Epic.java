package ru.yandex.model;

import java.util.HashSet;
import java.util.Set;

public class Epic extends Task {

    private final Set<SubTask> subTasks;

    public Epic() {
        this.subTasks = new HashSet<>();
    }

    @Override
    public void setId(int id) {
        super.setId(id);
        subTasks.forEach(s -> s.setEpicId(id));
    }

    public Set<SubTask> getSubTasks() {
        return subTasks;
    }

    public void deleteSubTusk(SubTask subTask) {
        subTasks.removeIf(s -> s.getId() == subTask.getId());
    }

    public void clearSubTask() {
        subTasks.clear();
    }

    public void addSubTask(SubTask subTask) {
        deleteSubTusk(subTask);
        subTask.setEpicId(getId());
        subTasks.add(subTask);
    }
}
