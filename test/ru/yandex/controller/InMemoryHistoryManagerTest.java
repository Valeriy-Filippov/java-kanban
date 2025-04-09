package ru.yandex.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.model.Task;
import ru.yandex.model.TaskStatus;

import java.util.ArrayList;
import java.util.List;

class InMemoryHistoryManagerTest {

    private HistoryManager historyManager;

    private Task task1;
    private Task task2;
    private Task task3;
    private Task task4;
    private Task task5;
    private Task task6;
    private Task task7;
    private Task task8;
    private Task task9;
    private Task task10;
    private Task task11;

    @Test
    void testGetHistoryOverSize() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);
        historyManager.add(task5);
        historyManager.add(task6);
        historyManager.add(task7);
        historyManager.add(task8);
        historyManager.add(task9);
        historyManager.add(task10);
        historyManager.add(task11);
        List<Task> history = historyManager.getHistory();

        List<Task> expHistory = new ArrayList<>(List.of(
                task2,
                task3,
                task4,
                task5,
                task6,
                task7,
                task8,
                task9,
                task10,
                task11
        ));

        Assertions.assertEquals(10, history.size(),"Максимальный размер истории равен 10");
        Assertions.assertEquals(expHistory, history, "При переполнении истории задачи должны сдвигаться");
    }

    @Test
    void testAdd() {
        historyManager.add(task1);
        historyManager.add(task2);
        List<Task> history = historyManager.getHistory();
        Assertions.assertNotNull(history, "После добавления задачи, история не должна быть пустой.");
        Assertions.assertEquals(2, history.size(), "После добавления 2 задач, размер должен быть равен 2.");
    }

    @BeforeEach
    public void init(){
        historyManager = Managers.getDefaultHistory();

        task1 = new Task(1, "Имя", "Описание", TaskStatus.NEW);
        task2 = new Task(2, "Имя", "Описание", TaskStatus.NEW);
        task3 = new Task(3, "Имя", "Описание", TaskStatus.NEW);
        task4 = new Task(4, "Имя", "Описание", TaskStatus.NEW);
        task5 = new Task(5, "Имя", "Описание", TaskStatus.NEW);
        task6 = new Task(6, "Имя", "Описание", TaskStatus.NEW);
        task7 = new Task(7, "Имя", "Описание", TaskStatus.NEW);
        task8 = new Task(8, "Имя", "Описание", TaskStatus.NEW);
        task9 = new Task(9, "Имя", "Описание", TaskStatus.NEW);
        task10 = new Task(10, "Имя", "Описание", TaskStatus.NEW);
        task11 = new Task(11, "Имя", "Описание", TaskStatus.NEW);
    }
}