package tasks.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.model.ArrayTaskList;
import tasks.model.Task;

import java.util.Calendar;
import java.util.Date;

public class BbtTest {
    private TasksService service;

    @BeforeEach
    public void setUp() {
        ArrayTaskList taskList = new ArrayTaskList();
        service = new TasksService(taskList);
        service.clear();
        Task task = new Task("Test data", new Date(2024 - 1900, Calendar.APRIL, 1), new Date(2024 - 1900, Calendar.APRIL, 1), 1);
        service.saveTask(task);
    }

    // ECP Valid
    @Test
    public void saveTaskWithValidTitle() {
        Task task = new Task("Titlu valid", new Date(2025 - 1900, Calendar.JANUARY, 1), new Date(2025 - 1900, Calendar.JANUARY, 1), 1);
        Assertions.assertDoesNotThrow(() -> service.saveTask(task));
    }

    // ECP Invalid
    @Test
    public void saveTaskWithInvalidTitle() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Task task = new Task("", new Date(2024, Calendar.JANUARY, 1), new Date(2024, Calendar.JANUARY, 1), 1);
            service.saveTask(task);
        });
    }

    // BVA Valid
    @Test
    public void saveTaskWithValidDateBoundary() {
        Date lowerBound = new Date(1577836800000L); // 01.01.2020
        Task task = new Task("Valid date", lowerBound, lowerBound, 1);
        Assertions.assertDoesNotThrow(() -> service.saveTask(task));
    }

    // BVA Invalid
    @Test
    public void saveTaskWithInvalidDateBoundary() {
        Date invalidDate = new Date(1577836799000L); // 31.12.2019
        Task task = new Task("Invalid date", invalidDate, invalidDate, 1);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            service.saveTask(task);
        });
    }
}

