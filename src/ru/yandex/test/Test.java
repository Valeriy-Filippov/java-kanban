package ru.yandex.test;

import ru.yandex.controller.TaskManager;
import ru.yandex.model.Epic;
import ru.yandex.model.SubTask;
import ru.yandex.model.Task;
import ru.yandex.model.TaskStatus;

public class Test {
    static TaskManager taskManager = new TaskManager();

    public static void main(String[] args) {

        Task task1 = new Task(taskManager.nextId(), "Задача1", "Описание задачи1");
        Task task2 = new Task(taskManager.nextId(), "Задача2", "Описание задачи2");
        Epic epic1 = new Epic(taskManager.nextId(), "Эпик1", "Описание эпика1");
        Epic epic2 = new Epic(taskManager.nextId(), "Эпик2", "Описание эпика2");
        SubTask subTask1 = new SubTask(
                taskManager.nextId(), "Подзадача1 Эпика1", "Описание подзадачи1", epic1);
        SubTask subTask2 = new SubTask(
                taskManager.nextId(), "Подзадача2 Эпика1", "Описание подзадачи2", epic1);
        SubTask subTask3 = new SubTask(
                taskManager.nextId(), "Подзадача1 Эпика2", "Описание подзадачи1", epic2);

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(epic1);
        taskManager.addTask(epic2);
        taskManager.addTask(subTask1);
        taskManager.addTask(subTask2);
        taskManager.addTask(subTask3);

        printTasksByClass(Task.class);
        printTasksByClass(Epic.class);
        printTasksByClass(SubTask.class);

        taskManager.updateTaskStatus(new Task(1, "", "", TaskStatus.DONE));
        taskManager.updateTaskStatus(new Task(2, "", "", TaskStatus.IN_PROGRESS));

        taskManager.updateTaskStatus(new SubTask(5, "", "", TaskStatus.DONE, epic1));
        taskManager.updateTaskStatus(new SubTask(6, "", "", TaskStatus.IN_PROGRESS, epic1));
        taskManager.updateTaskStatus(new SubTask(7, "", "", TaskStatus.DONE, epic2));

        System.out.println("=".repeat(100));
        System.out.println("Списки после обновления: ");
        printTasksByClass(Task.class);
        printTasksByClass(Epic.class);
        printTasksByClass(SubTask.class);

        taskManager.removeById(1);
        taskManager.removeById(3);

        System.out.println("=".repeat(100));
        System.out.println("Списки после удаления: ");
        printTasksByClass(Task.class);
        printTasksByClass(Epic.class);
        printTasksByClass(SubTask.class);

    }

    public static <T extends Task> void printTasksByClass(Class<T> cl) {
        System.out.println("Список " + cl.getSimpleName());
        taskManager.getTasksByClass(cl).forEach(t -> System.out.println("\t" + t));
    }

}
