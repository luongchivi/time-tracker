package org.luongchivi.timetracker.data;

import org.luongchivi.timetracker.Logger;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CurrentTask {
    private Map<String, Task> currentTasks = new HashMap<>();

    public CurrentTask(Map<String, Task> currentTasks) {
        this.currentTasks = currentTasks;
    }

    public void startTask(Task task) {
        if (currentTasks.putIfAbsent(task.getTaskName(), task) != null) {
            Logger.log("Task already exists, skipping");
        }
    }

    public void completeTask(String taskName) {
        Task existingTask = currentTasks.get(taskName);
        if (existingTask == null) {
            Logger.log("No tasks found");
        } else {
            existingTask.setEndTime(LocalDateTime.now());
            existingTask.setStatus(TaskStatus.COMPLETE);
        }
    }

    public Map<String, Duration> getTasksReport() {
        return currentTasks
                .values()
                .stream()
                .filter(task -> task.getEndTime() != null && !task.getStatus().equals(TaskStatus.IN_PROGRESS))
                .collect(Collectors.toMap(task -> task.getTaskName(), task -> task.getTaskDuration()));
    }

    public Map<String, Duration> getCategoriesReport() {
        Map<String, Duration> categoriesReport = new HashMap<>();
        currentTasks
                .values()
                .stream()
                .filter(task -> task.getEndTime() != null)
                .forEach(task -> {
                    String category = task.getCategory().getName();
                    Duration categoryDuration = categoriesReport.getOrDefault(category, Duration.ZERO);
                    categoriesReport.put(category, categoryDuration.plus(task.getTaskDuration()));
                });

        return categoriesReport;
    }

    public Map<String, Task> getCurrentTasks() {
        return currentTasks;
    }

    public void setCurrentTasks(Map<String, Task> currentTasks) {
        this.currentTasks = currentTasks;
    }

    @Override
    public String toString() {
        return "CurrentTask{" +
                "currentTasks=" + currentTasks +
                '}';
    }
}
