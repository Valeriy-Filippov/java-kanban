package ru.yandex.controller;

import ru.yandex.model.Epic;
import ru.yandex.model.SubTask;
import ru.yandex.model.Task;

import java.util.List;
import java.util.Set;

public interface TaskManager {

    List<Task> getTaskList();

    List<Epic> getEpicList();

    List<SubTask> getSubTaskList();

    void deleteAllTask();

    void deleteAllEpic();

    void deleteAllSubTask();

    Task getTaskById(int id);

    void addTask(Task task);

    void addTask(Epic epic);

    void addTask(SubTask subTask);

    void updateTask(Task task);

    void updateTask(Epic epic);

    void updateTask(SubTask subTask);

    boolean deleteById(int id);

    Set<SubTask> getSubTaskByEpic(Epic epic);
}
