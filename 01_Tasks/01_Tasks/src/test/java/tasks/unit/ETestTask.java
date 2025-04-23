package tasks.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.model.Task;

import java.util.Calendar;
import java.util.Date;

public class ETestTask {

    @Test
    public void Test1_TestConstructor() {
        String defaultTitle = "Title";
        Date defaultStartDate = new Date(121, 04, 15, 12,30);
        Date defaultEndDate = new Date(123, 04, 15, 12,30);
        int defaultInterval = 2;

        Assertions.assertDoesNotThrow(() -> {
            Task task;

            task = new Task(defaultTitle, defaultStartDate);
            Assertions.assertNotNull(task);
            Assertions.assertEquals(defaultTitle, task.getTitle());
            Assertions.assertEquals(defaultStartDate, task.getStartTime());
            Assertions.assertEquals(defaultStartDate, task.getEndTime());
            Assertions.assertEquals(0, task.getRepeatInterval());

            task = new Task(defaultTitle, defaultStartDate, defaultEndDate, defaultInterval);
            Assertions.assertNotNull(task);

            Assertions.assertEquals(defaultTitle, task.getTitle());
            Assertions.assertEquals(defaultStartDate, task.getStartTime());
            Assertions.assertEquals(defaultEndDate, task.getEndTime());
            Assertions.assertEquals(defaultInterval, task.getRepeatInterval());
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> new Task(defaultTitle, new Date(-1)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Task("", defaultStartDate, defaultEndDate, defaultInterval));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Task(defaultTitle, new Date(-1), defaultEndDate, defaultInterval));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Task(defaultTitle, defaultStartDate, new Date(-1), defaultInterval));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Task(defaultTitle, defaultStartDate, defaultEndDate, 0));
    }

    @Test
    public void Test2_TestNextTimeAfter() {
        // Setăm o dată de început după 2020
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, Calendar.JANUARY, 1, 12, 0, 0);
        Date start = calendar.getTime(); // 12:00:00

        calendar.set(2023, Calendar.JANUARY, 1, 12, 1, 0);
        Date end = calendar.getTime(); // 12:01:00

        int interval = 10; // 10 secunde

        Task task = new Task("Title", start, end, interval);
        task.setActive(true); // activăm task-ul

        // Verificăm că task-ul e valid
        Assertions.assertNotNull(task);

        // Test 1: current < start => returnează start
        calendar.set(2023, Calendar.JANUARY, 1, 11, 59, 59);
        Date currentBeforeStart = calendar.getTime();
        Assertions.assertEquals(start.toString(), task.nextTimeAfter(currentBeforeStart).toString());

        // Test 2: current este între 12:00:10 și 12:00:20 => ar trebui să returneze 12:00:10 (conform metodei actuale)
        calendar.set(2023, Calendar.JANUARY, 1, 12, 0, 15);
        Date currentMid = calendar.getTime();
        Date expected = new Date(start.getTime() + 10 * 1000); // 12:00:10
        Assertions.assertEquals(expected.toString(), task.nextTimeAfter(currentMid).toString());

        // Test 3: current > end => returnează null
        calendar.set(2023, Calendar.JANUARY, 1, 12, 2, 0);
        Date currentAfterEnd = calendar.getTime();
        Assertions.assertNull(task.nextTimeAfter(currentAfterEnd));
    }

}
