package ru.yandex.model;

import java.util.HashSet;
import java.util.Set;

public class Epic extends Task {

    private final Set<SubTask> subTasks;

    public Epic(int id, String name, String description) {
        super(id, name, description);
        subTasks = new HashSet<>();
    }

    public Set<SubTask> getSubTasks() {
        return subTasks;
    }

}
