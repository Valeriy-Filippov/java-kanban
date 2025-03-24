package ru.yandex.controller;

import ru.yandex.model.Epic;
import ru.yandex.model.SubTask;
import ru.yandex.model.Task;
import ru.yandex.model.TaskStatus;

import java.util.*;

public class TaskManager {

    private final Map<Integer, Task> taskById;
    private int counter;

    public TaskManager() {
        taskById = new HashMap<>();
    }

    public int nextId() {
        return ++counter;
    }

    public void addTask(Task task) {
        taskById.put(task.getId(), task);
    }

    public void updateTaskStatus(Task task) {
        taskById.get(task.getId()).setTaskStatus(task.getTaskStatus());
    }

    public void updateTaskStatus(Epic task) {
        throw new IllegalArgumentException();
    }

    public void updateTaskStatus(SubTask task) {
        SubTask updateSubTask = (SubTask) getTaskById(task.getId());
        updateSubTask.setTaskStatus(task.getTaskStatus());
        Epic updateEpic = updateSubTask.getOwner();
        if (task.getTaskStatus() == TaskStatus.IN_PROGRESS) {
            updateEpic.setTaskStatus(TaskStatus.IN_PROGRESS);
        } else if (task.getTaskStatus() == TaskStatus.DONE) {
            if (updateEpic.getSubTasks().stream().allMatch(s -> s.getTaskStatus() == TaskStatus.DONE)) {
                updateEpic.setTaskStatus(TaskStatus.DONE);
            } else {
                updateEpic.setTaskStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }


    public <T extends Task> List<T> getTasksByClass(Class<T> tClass) {
        return taskById.values().stream()
                .filter(task -> task.getClass().equals(tClass))
                .map(tClass::cast).toList();
    }

    public Task getTaskById(int id) {
        return taskById.getOrDefault(id, Task.EMPTY_TASK);
    }

    public <T extends Task> void removeTasksByClass(Class<T> tClass) {
        getTasksByClass(tClass).forEach(t -> taskById.remove(t.getId()));
    }

    public void removeById(int id) {
        if (taskById.containsKey(id)) {
            Task deleteTask = taskById.get(id);
            if (deleteTask.getClass() == Task.class) {
                taskById.remove(id);
                return;
            }
            if (deleteTask instanceof SubTask deleteSubTask) {
                deleteSubTask.getOwner().getSubTasks().remove(deleteTask);
                taskById.remove(id);
                return;
            }
            if (deleteTask instanceof Epic deleteEpic) {
                deleteEpic.getSubTasks().forEach(s -> taskById.remove(s.getId()));
                taskById.remove(id);
            }
        }
    }

    public Set<SubTask> getSubTasksByEpic(Epic epic) {
        if (getTaskById(epic.getId()).getId() == -1) {
            return Collections.emptySet();
        }
        return getTasksByClass(epic.getClass()).get(epic.getId()).getSubTasks();
    }

}
