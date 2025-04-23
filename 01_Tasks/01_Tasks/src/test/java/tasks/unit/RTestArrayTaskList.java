package tasks.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tasks.model.ArrayTaskList;
import tasks.model.Task;

import java.util.Date;

public class RTestArrayTaskList {

    Task task;

    @BeforeEach
    public void setUp() {
        task = Mockito.mock(Task.class);
        Mockito.when(task.getTask()).thenReturn(new Task("Title 1", new Date(121, 04, 15, 12,30), new Date(123, 04, 15, 12,30), 3));
    }

    @Test
    public void Test1_TestAdd() {
        ArrayTaskList list = new ArrayTaskList();

        list.add(task.getTask());
        list.add(task.getTask());

        Assertions.assertEquals(2, list.size());
    }

    @Test
    public void Test2_TestRemove() {
        ArrayTaskList list = new ArrayTaskList();
        list.add(task.getTask());

        Assertions.assertEquals(1, list.size());

        list.remove(task.getTask());
        Assertions.assertEquals(0, list.size());
    }

}
