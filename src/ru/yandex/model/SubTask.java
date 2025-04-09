package ru.yandex.model;

public class SubTask extends Task {

    private int epicId;

    public SubTask(int id, String name, String description, TaskStatus taskStatus, int epicId) {
        super(id, name, description, taskStatus);
        setEpicId(epicId);
    }

    public SubTask(String name, String description, TaskStatus taskStatus, int epicId) {
        super(name, description, taskStatus);
        setEpicId(epicId);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        if (epicId == getId()) {
            throw new IllegalArgumentException("Подзадача не может быть своим же эпиком");
        }
        this.epicId = epicId;
    }

}
