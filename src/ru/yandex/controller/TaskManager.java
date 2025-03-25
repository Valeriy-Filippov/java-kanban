package ru.yandex.controller;

import ru.yandex.model.Epic;
import ru.yandex.model.SubTask;
import ru.yandex.model.Task;
import ru.yandex.model.TaskStatus;

import java.util.*;

public class TaskManager {

    private final Map<Integer, Task> taskById;
    private final Map<Integer, Epic> epicById;
    private final Map<Integer, SubTask> subTaskById;

    private int counter = 1;

    public TaskManager() {
        taskById = new HashMap<>();
        epicById = new HashMap<>();
        subTaskById = new HashMap<>();
    }

    public List<Task> getTaskList() {
        return List.copyOf(taskById.values());
    }

    public List<Epic> getEpicList() {
        return List.copyOf(epicById.values());
    }

    public List<SubTask> getSubTaskList() {
        return List.copyOf(subTaskById.values());
    }

    public void deleteAllTask() {
        taskById.clear();
    }

    public void deleteAllEpic() {
        epicById.clear();
        subTaskById.clear();
    }

    public void deleteAllSubTask() {
        epicById.values().forEach(epic -> {
            epic.clearSubTask();
            refreshEpicTaskStatus(epic);
        });
        subTaskById.clear();
    }

    public Task getTaskById(int id) {
        if (taskById.containsKey(id)) {
            return taskById.get(id);
        }
        if (epicById.containsKey(id)) {
            return epicById.get(id);
        }
        if (subTaskById.containsKey(id)) {
            return subTaskById.get(id);
        }
        return null;
    }

    public void addTask(Task task) {
        int id = nextId();
        task.setId(id);
        taskById.put(id, task);
    }

    public void addTask(Epic epic) {
        int id = nextId();
        epic.setId(id);
        epicById.put(id, epic);
    }

    public void addTask(SubTask subTask) {
        if (epicById.containsKey(subTask.getEpicId())) {
            int id = nextId();
            subTask.setId(id);
            epicById.get(subTask.getEpicId()).addSubTask(subTask);
            refreshEpicTaskStatus(epicById.get(subTask.getEpicId()));
            subTaskById.put(id, subTask);
        }
    }

    public void updateTask(Task task) {
        if (taskById.containsKey(task.getId())) {
            taskById.put(task.getId(), task);
        }
    }

    public void updateTask(Epic epic) {
        if (epicById.containsKey(epic.getId())) {
            Epic epicUpdate = epicById.get(epic.getId());
            epicUpdate.setName(epic.getName());
            epicUpdate.setDescription(epic.getDescription());
        }
    }

    public void updateTask(SubTask subTask) {
        if (subTaskById.containsKey(subTask.getId())
                && subTask.getEpicId() == subTaskById.get(subTask.getId()).getEpicId()) {
            epicById.get(subTask.getEpicId()).addSubTask(subTask);
            refreshEpicTaskStatus(epicById.get(subTask.getEpicId()));
            subTaskById.put(subTask.getId(), subTask);
        }
    }

    public boolean deleteById(int id) {
        if (taskById.containsKey(id)) {
            taskById.remove(id);
            return true;
        }

        if (epicById.containsKey(id)) {
            epicById.get(id).getSubTasks().forEach(s -> subTaskById.remove(s.getId()));
            epicById.remove(id);
            return true;
        }
        if (subTaskById.containsKey(id)) {
            SubTask subTask = subTaskById.get(id);
            Epic epic;
            if (epicById.containsKey(subTask.getEpicId())) {
                epic = epicById.get(subTask.getEpicId());
                epic.deleteSubTusk(subTask);
                refreshEpicTaskStatus(epic);
            }
            subTaskById.remove(id);
            return true;
        }
        return false;
    }

    public Set<SubTask> getSubTaskByEpic(Epic epic) {
        if (epicById.containsValue(epic)) {
            return epicById.get(epic.getId()).getSubTasks();
        }
        return Collections.emptySet();
    }

    private int nextId() {
        return counter++;
    }

    private void refreshEpicTaskStatus(Epic epic) {
        int subTasksSize = epic.getSubTasks().size();

        if (subTasksSize == 0) {
            epic.setTaskStatus(TaskStatus.NEW);
            return;
        }

        int countInProgress = 0;
        int countDone = 0;

        for (SubTask s : epic.getSubTasks()) {
            if (s.getTaskStatus() == TaskStatus.IN_PROGRESS) {
                countInProgress++;
                continue;
            }
            if (s.getTaskStatus() == TaskStatus.DONE) {
                countDone++;
            }
        }

        if (countDone == subTasksSize) {
            epic.setTaskStatus(TaskStatus.DONE);
            return;
        }

        if (countDone + countInProgress > 0) {
            epic.setTaskStatus(TaskStatus.IN_PROGRESS);
            return;
        }
        epic.setTaskStatus(TaskStatus.NEW);
    }
}
