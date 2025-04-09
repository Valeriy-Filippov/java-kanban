package ru.yandex.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.model.Epic;
import ru.yandex.model.SubTask;
import ru.yandex.model.Task;
import ru.yandex.model.TaskStatus;

class InMemoryTaskManagerTest {

    @Test
    public void testAddTaskIsId(){
        TaskManager manager = Managers.getDefault();
        Task task = new Task(3,"Имя2", "Описание2", TaskStatus.DONE);
        manager.addTask(task);

        Assertions.assertEquals(1,task.getId(),
                "При добавлении задачи с id менеджер устанавливает свой id");
    }

    @Test
    public void testAddAllType() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Имя", "Описание", TaskStatus.DONE);
        Epic epic = new Epic("Имя", "Описание");
        SubTask subTask = new SubTask("Имя", "Описание", TaskStatus.DONE, 2);

        manager.addTask(task);
        manager.addTask(epic);
        manager.addTask(subTask);

        Assertions.assertEquals(1, manager.getTaskList().size(), "Длинна задач после добавления = 1");
        Assertions.assertEquals(1, manager.getEpicList().size(), "Длинна задач после добавления = 1");
        Assertions.assertEquals(1, manager.getSubTaskList().size(), "Длинна задач после добавления = 1");

        Assertions.assertEquals(task, manager.getTaskById(1), "Задача должна находится по id");
        Assertions.assertEquals(epic, manager.getTaskById(2), "Задача должна находится по id");
        Assertions.assertEquals(subTask, manager.getTaskById(3), "Задача должна находится по id");
    }

    @Test
    public void testAddTaskNoChangeFields() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Имя", "Описание", TaskStatus.DONE);
        manager.addTask(task);

        Task actual = manager.getTaskById(1);
        Assertions.assertEquals("Имя", actual.getName(),
                "При добавлении поля не должны меняться");
        Assertions.assertEquals("Описание", actual.getDescription(),
                "При добавлении поля не должны меняться");
        Assertions.assertEquals(TaskStatus.DONE, actual.getTaskStatus(),
                "При добавлении поля не должны меняться");
    }

}