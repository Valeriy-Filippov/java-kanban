package ru.yandex.model;

public class SubTask extends Task {

    private final Epic owner;

    public SubTask(int id, String name, String description, Epic owner) {
        super(id, name, description);
        this.owner = owner;
        owner.getSubTasks().add(this);
    }

    public SubTask(int id, String name, String description, TaskStatus taskStatus, Epic owner) {
        super(id, name, description, taskStatus);
        this.owner = owner;
    }

    public Epic getOwner() {
        return owner;
    }

}
