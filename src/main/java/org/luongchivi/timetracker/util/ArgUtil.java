package org.luongchivi.timetracker.util;

import org.luongchivi.timetracker.Logger;
import org.luongchivi.timetracker.data.Category;

public class ArgUtil {
    public Args parseArgs(String[] args) {
        if (!validate(args)) {
            throw new RuntimeException("invalid arguments");
        }
        Args argsObj = new Args();
        String cmdString = args[0];

        Commands command = switch (cmdString) {
            case "start" -> Commands.TASK_START;
            case "stop" -> Commands.TASK_STOP;
            case "report" -> "task".equals(args[1]) ? Commands.REPORT_TASK
                    : "category".equals(args[1]) ? Commands.REPORT_CATEGORIES : null;
            default -> throw new RuntimeException("Invalid input arguments: " + cmdString);
        };

        argsObj.setCommand(command);

        if (Commands.TASK_START.equals(command) || Commands.TASK_STOP.equals(command)) {
            argsObj.setTaskName(args[1]);
            argsObj.setCategoryName(args.length == 3 ? args[2] : Category.NONE);
        }

        return argsObj;
    }

    public boolean validate(String[] args) {
        if (args.length < 2) {
            Logger.log("Error! Not enough arguments!");
            return false;
        }
        return true;
    }
}
