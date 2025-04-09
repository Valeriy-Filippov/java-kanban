package ru.yandex.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ManagersTest {

    @Test
    void testGetDefault() {
        Assertions.assertNotNull(Managers.getDefault(), "Менеджер должен быть проинициализирован");
    }

    @Test
    void testGetDefaultHistory() {
        Assertions.assertNotNull(Managers.getDefaultHistory(), "Менеджер должен быть проинициализирован");
    }
}