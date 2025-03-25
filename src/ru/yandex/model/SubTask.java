package ru.yandex.model;

public class SubTask extends Task {

    private int epicId;

    public SubTask(Epic epic) {
        epic.addSubTask(this);
    }

    public int getEpicId() {
        return epicId;
    }

    protected void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
