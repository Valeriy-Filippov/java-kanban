package ru.yandex.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SubTaskTest {

    @Test
    void testSetEpicIdOwnId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new SubTask(1, "", "", TaskStatus.NEW, 1);
        }, "Если id и epicId равны бросается исключение");
    }
}