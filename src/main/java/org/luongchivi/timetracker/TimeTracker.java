package org.luongchivi.timetracker;

import org.luongchivi.timetracker.data.Category;
import org.luongchivi.timetracker.data.CurrentTask;
import org.luongchivi.timetracker.data.Task;
import org.luongchivi.timetracker.util.ArgUtil;
import org.luongchivi.timetracker.util.Args;
import org.luongchivi.timetracker.util.FileUtil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Map;


public class TimeTracker {
    public static void main(String[] args) throws IOException, URISyntaxException {
        ArgUtil argUtil = new ArgUtil();
        Args arguments = argUtil.parseArgs(args);

        // Get currentTasks from file
        FileUtil fileUtil = new FileUtil();
        CurrentTask currentTask = fileUtil.getSaveTasks();

        switch (arguments.getCommand().name()) {
            case "TASK_START" -> {
                Task task = new Task(arguments.getTaskName(), new Category(arguments.getCategoryName()));
                currentTask.startTask(task);
            }
            case "TASK_STOP" -> currentTask.completeTask(arguments.getTaskName());
            case "REPORT_TASK" -> {
                Map<String, Duration> tasksReport = currentTask.getTasksReport();
                for (Map.Entry<String, Duration> entry : tasksReport.entrySet()) {
                    System.out.println("Task: " + entry.getKey());
                    System.out.println("Duration in minutes: " + entry.getValue().toMinutes());
                }
            }
            case "REPORT_CATEGORIES" -> {
                Map<String, Duration> categoriesReport = currentTask.getCategoriesReport();
                for (Map.Entry<String, Duration> entry : categoriesReport.entrySet()) {
                    System.out.println("Category: " + entry.getKey());
                    System.out.println("Duration in minutes: " + entry.getValue().toMinutes());
                }
            }
        }
        fileUtil.saveTasksToFile(currentTask);
    }
}