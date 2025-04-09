package ru.yandex.controller;

import ru.yandex.model.Epic;
import ru.yandex.model.SubTask;
import ru.yandex.model.Task;
import ru.yandex.model.TaskStatus;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private final Map<Integer, Task> taskById;
    private final Map<Integer, Epic> epicById;
    private final Map<Integer, SubTask> subTaskById;
    private final HistoryManager historyManager;

    private int counter = 1;

    public InMemoryTaskManager() {
        taskById = new HashMap<>();
        epicById = new HashMap<>();
        subTaskById = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public List<Task> getTaskList() {
        return List.copyOf(taskById.values());
    }

    @Override
    public List<Epic> getEpicList() {
        return List.copyOf(epicById.values());
    }

    @Override
    public List<SubTask> getSubTaskList() {
        return List.copyOf(subTaskById.values());
    }

    @Override
    public void deleteAllTask() {
        taskById.clear();
    }

    @Override
    public void deleteAllEpic() {
        epicById.clear();
        subTaskById.clear();
    }

    @Override
    public void deleteAllSubTask() {
        epicById.values().forEach(epic -> {
            epic.clearSubTask();
            refreshEpicTaskStatus(epic);
        });
        subTaskById.clear();
    }

    @Override
    public Task getTaskById(int id) {
        Task result = null;
        if (taskById.containsKey(id)) {
            result = taskById.get(id);
        } else if (epicById.containsKey(id)) {
            result = epicById.get(id);
        } else if (subTaskById.containsKey(id)) {
            result = subTaskById.get(id);
        }
        historyManager.add(result);
        return result;
    }

    @Override
    public void addTask(Task task) {
        int id = nextId();
        task.setId(id);
        taskById.put(id, task);
    }


    @Override
    public void addTask(Epic epic) {
        int id = nextId();
        epic.setId(id);
        epicById.put(id, epic);
    }

    @Override
    public void addTask(SubTask subTask) {
        if (epicById.containsKey(subTask.getEpicId())) {
            int id = nextId();
            subTask.setId(id);
            epicById.get(subTask.getEpicId()).addSubTask(subTask);
            refreshEpicTaskStatus(epicById.get(subTask.getEpicId()));
            subTaskById.put(id, subTask);
        }
    }


    @Override
    public void updateTask(Task task) {
        if (taskById.containsKey(task.getId())) {
            taskById.put(task.getId(), task);
        }
    }

    @Override
    public void updateTask(Epic epic) {
        if (epicById.containsKey(epic.getId())) {
            Epic epicUpdate = epicById.get(epic.getId());
            epicUpdate.setName(epic.getName());
            epicUpdate.setDescription(epic.getDescription());
        }
    }

    @Override
    public void updateTask(SubTask subTask) {
        if (subTaskById.containsKey(subTask.getId())
                && subTask.getEpicId() == subTaskById.get(subTask.getId()).getEpicId()) {
            epicById.get(subTask.getEpicId()).addSubTask(subTask);
            refreshEpicTaskStatus(epicById.get(subTask.getEpicId()));
            subTaskById.put(subTask.getId(), subTask);
        }
    }

    @Override
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

    @Override
    public Set<SubTask> getSubTaskByEpic(Epic epic) {
        if (epicById.containsValue(epic)) {
            return epicById.get(epic.getId()).getSubTasks();
        }
        return Collections.emptySet();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
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
