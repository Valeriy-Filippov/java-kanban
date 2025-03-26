package ru.yandex.model;

import java.util.HashSet;
import java.util.Set;

public class Epic extends Task {

    private final Set<SubTask> subTasks;

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
        subTasks = new HashSet<>();
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
        subTasks.add(subTask);
    }
}
