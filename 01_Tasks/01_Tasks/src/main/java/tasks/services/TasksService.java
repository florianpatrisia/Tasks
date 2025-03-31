package tasks.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import tasks.model.ArrayTaskList;
import tasks.model.Task;
import tasks.model.TasksOperations;

import java.util.Date;

public class TasksService {

    private ArrayTaskList tasks;
    private static final Logger log = Logger.getLogger(TasksService.class.getName());

    public TasksService(ArrayTaskList tasks){
        this.tasks = tasks;
    }

    public ObservableList<Task> getObservableList(){
        return FXCollections.observableArrayList(tasks.getAll());
    }
    public String getIntervalInHours(Task task){
        int seconds = task.getRepeatInterval();
        int minutes = seconds / DateService.SECONDS_IN_MINUTE;
        int hours = minutes / DateService.MINUTES_IN_HOUR;
        minutes = minutes % DateService.MINUTES_IN_HOUR;
        return formTimeUnit(hours) + ":" + formTimeUnit(minutes);//hh:MM
    }
    public String formTimeUnit(int timeUnit){
        StringBuilder sb = new StringBuilder();
        if (timeUnit < 10) sb.append("0");
        if (timeUnit == 0) sb.append("0");
        else {
            sb.append(timeUnit);
        }
        return sb.toString();
    }

    public int parseFromStringToSeconds(String stringTime){//hh:MM
        String[] units = stringTime.split(":");
        int hours = Integer.parseInt(units[0]);
        int minutes = Integer.parseInt(units[1]);
        int result = (hours * DateService.MINUTES_IN_HOUR + minutes) * DateService.SECONDS_IN_MINUTE;
        return result;
    }

    public Iterable<Task> filterTasks(Date start, Date end){
        TasksOperations tasksOps = new TasksOperations(getObservableList());
        Iterable<Task> filtered = tasksOps.incoming(start,end);
        //Iterable<Task> filtered = tasks.incoming(start, end);

        return filtered;
    }
    public void saveTask(Task task) {
        tasks.remove(task);
        validateTask(task);
        tasks.add(task);
    }

    public ArrayTaskList getTasks(){
        return tasks;
    }

    public void clear(){
        tasks = new ArrayTaskList();
    }

    public void validateTask(Task task){
        Date start = task.getStartTime();
        Date end = task.getEndTime();
        int interval = task.getRepeatInterval();
        String title = task.getTitle();

        long LOWER_BOUND = 1577836800000L;     // 01.01.2020 00:00:00
        long UPPER_BOUND = 2524607999000L;     // 31.12.2050 23:59:59

        if (start.getTime() < LOWER_BOUND || end.getTime() < LOWER_BOUND) {
            log.error("Time must not be before 1 Jan 2020");
            throw new IllegalArgumentException("Time must not be before 1 Jan 2020");
        }

        if (start.getTime() > UPPER_BOUND || end.getTime() > UPPER_BOUND) {
            log.error("Time must not be after 31 Dec 2050");
            log.error("Start time: " + start.getTime() + " End time: " + end.getTime());
            throw new IllegalArgumentException("Time must not be after 31 Dec 2050");
        }
        if (start.getTime() < 0 || end.getTime() < 0) {
            log.error("Time cannot be negative");
            throw new IllegalArgumentException("Time cannot be negative");
        }
        if (interval < 1) {
            log.error("Interval should be > 1");
            throw new IllegalArgumentException("interval should me > 1");
        }
        if (title.isEmpty() || title.length() > 255) {
            log.error("Title is out of bounds");
            throw new IllegalArgumentException("Title is out of bounds");
        }
    }
}
