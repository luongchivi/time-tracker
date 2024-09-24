package org.luongchivi.timetracker.util;

import org.luongchivi.timetracker.data.Category;
import org.luongchivi.timetracker.data.CurrentTask;
import org.luongchivi.timetracker.data.Task;
import org.luongchivi.timetracker.data.TaskStatus;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FileUtil {

    public static final String PATH = "task-info.csv";

    public CurrentTask getSaveTasks() throws IOException, URISyntaxException {
        Path path = Paths.get(PATH);
        if (Files.notExists(path)) {
            Files.createFile(path);
        }
        Map<String, Task> taskMap = Files.lines(path)
                //.peek(System.out::println)
                .map(line -> line.split(","))
                .filter(tokenArray -> tokenArray.length == 5)
                .map(tokenArray -> new Task(
                        tokenArray[0],
                        new Category(tokenArray[1]),
                        tokenArray[2] == null || "null".equals(tokenArray[2]) || tokenArray[2].isBlank() ? null : LocalDateTime.parse(tokenArray[2]),
                        tokenArray[3] == null || "null".equals(tokenArray[3]) || tokenArray[3].isBlank() ? null : LocalDateTime.parse(tokenArray[3]),
                        TaskStatus.valueOf(tokenArray[4])
                ))
                //.collect(Collectors.toMap(Task::getTaskName, Function.identity()));
                .collect(Collectors.toMap(task -> task.getTaskName(), task -> task));
        return new CurrentTask(taskMap);
    }

    public void saveTasksToFile(CurrentTask tasks) throws IOException {
        Path path = Paths.get(PATH);
        if (Files.notExists(path)) {
            Files.createFile(path);
        }
        List<String> lines = tasks.getCurrentTasks()
                .values()
                .stream()
                .map(task -> task.getCsvFormat())
                .toList();
        Files.write(path, lines);
    }
}
