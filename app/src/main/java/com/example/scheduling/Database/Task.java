package com.example.scheduling.Database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "tasks", primaryKeys = {"uid", "day"})
public class Task  {
    @NonNull
    @ColumnInfo(name = "uid")
    public String id;

    @NonNull
    @ColumnInfo(name = "day")
    public Long day;

    @ColumnInfo(name = "status")
    public String status;

    @ColumnInfo(name = "type")
    public String type;

    @ColumnInfo(name = "name")
    public String Name;

    @ColumnInfo(name = "description")
    public String Description;

    @ColumnInfo(name = "day_from")
    public Long day_from;

    @ColumnInfo(name = "day_to")
    public Long day_to;

    @ColumnInfo(name = "time_from")
    public Integer time_from;

    @ColumnInfo(name = "time_to")
    public Integer time_to;

    @ColumnInfo(name = "repeat")
    public Integer repeat;

    @ColumnInfo(name = "repeat_unit")
    public Integer repeat_unit;

    public Task clone(Task task) {
        Task nTask = new Task();
        nTask.id = task.id;
        nTask.day = task.day;
        nTask.type = task.type;
        nTask.status = task.status;
        nTask.Name = task.Name;
        nTask.Description = task.Description;
        nTask.day_from = task.day_from;
        nTask.day_to = task.day_to;
        nTask.time_from = task.time_from;
        nTask.time_to = task.time_to;
        nTask.repeat = task.repeat;
        nTask.repeat_unit = task.repeat_unit;
        return nTask;
    }
}
