package io.myzticbean.mcdevtools.scheduler;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.concurrent.CompletableFuture;

public class TaskScheduler {

    private final Plugin plugin;
    private final BukkitScheduler scheduler;

    public TaskScheduler(Plugin plugin) {
        this.plugin = plugin;
        this.scheduler = Bukkit.getScheduler();
    }

    public ScheduledTask runTask(Runnable task) {
        int taskId = scheduler.runTask(plugin, task).getTaskId();
        return new ScheduledTask(taskId, TaskType.SYNC);
    }

    public ScheduledTask runTaskAsync(Runnable task) {
        int taskId = scheduler.runTaskAsynchronously(plugin, task).getTaskId();
        return new ScheduledTask(taskId, TaskType.ASYNC);
    }

    public ScheduledTask runTaskLater(Runnable task, long delay) {
        int taskId = scheduler.runTaskLater(plugin, task, delay).getTaskId();
        return new ScheduledTask(taskId, TaskType.SYNC_DELAYED);
    }

    public CompletableFuture<Void> runTaskAsyncWithFuture(Runnable task) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        runTaskAsync(() -> {
            try {
                task.run();
                future.complete(null);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    public enum TaskType {
        SYNC, ASYNC, SYNC_DELAYED, ASYNC_DELAYED
    }

    @Getter
    public static class ScheduledTask {
        private final int taskId;
        private final TaskType type;

        private ScheduledTask(int taskId, TaskType type) {
            this.taskId = taskId;
            this.type = type;
        }
    }
}
