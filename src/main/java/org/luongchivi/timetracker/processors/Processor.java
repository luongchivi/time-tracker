package org.luongchivi.timetracker.processors;

import org.luongchivi.timetracker.data.Task;

public interface Processor {
    public void process(Task task);
}
