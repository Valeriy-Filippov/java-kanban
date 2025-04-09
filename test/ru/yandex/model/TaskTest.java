package ru.yandex.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    void assertEqualsTask(){
        Task task = new Task("Имя1", "Описание1", TaskStatus.NEW);
        Task task2 = new Task("Имя2", "Описание2", TaskStatus.NEW);

        task.setId(1);
        task2.setId(1);

        Assertions.assertEquals(task, task2,"Разные задачи с одинаковым id должны быть равны");
    }

    @Test
    void assertEqualsTasksAndEpic(){
        Task task = new Task("Имя1", "Описание1", TaskStatus.NEW);
        Epic task2 = new Epic("Имя2", "Описание2");

        task.setId(1);
        task2.setId(1);

        Assertions.assertEquals(task, task2, "Разные задачи с одинаковым id должны быть равны");
    }

}