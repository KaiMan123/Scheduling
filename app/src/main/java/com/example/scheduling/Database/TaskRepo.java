package com.example.scheduling.Database;

import android.app.Application;

import com.example.scheduling.utils.Time;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TaskRepo {
    private TaskDao mtaskDB;
    public TaskRepo(Application application) {
        TaskDB db = TaskDB.getDatabase(application);
        mtaskDB = db.taskDao();;
        Calendar calendar = Time.getTime();
        mtaskDB.checkPass(calendar.getTimeInMillis(), "passed", new String[]{"finished", "start"});
    }

    public List<List<Long>> getAllTasks() {
        List<List<Long>> mAllTask = new ArrayList<>();
        mAllTask.add(mtaskDB.getOne("short", "long"));
        mAllTask.add(mtaskDB.getOne("long", "short"));
        mAllTask.add(mtaskDB.getBoth("short", "long"));
//        mtaskDB.cleanDB();
        return mAllTask;
    }

    public List<Task> getByIds(String[] uuids) {
        return mtaskDB.getByIds(uuids);
    }

    public void deleteByIds(String[] uuids) {
        TaskDB.databaseWriteExecutor.execute(() -> {
            mtaskDB.deleteByIds(uuids);
        });
    }

    public List<Task> getByDay(Long date) {
        return mtaskDB.getByDay(date);
    }

    public void insertAll(List<Task> tasks) {
        TaskDB.databaseWriteExecutor.execute(() -> {
            mtaskDB.insertAll(tasks);
        });
    }

    public void insert(Task task) {
        if(task.repeat == -1){
            TaskDB.databaseWriteExecutor.execute(() -> {
                mtaskDB.insert(task);
            });
        }else{
            List<Task> tTask = generateTaskDays(task);
            TaskDB.databaseWriteExecutor.execute(() -> {
                mtaskDB.insertAll(tTask);
            });
//            System.out.println("++++++++++++++++++++++++++++++++++++++++++");
//            for(Task t : tTask){
//                Calendar nc = Time.getDate();
//                nc.setTimeInMillis(t.day);
//                System.out.println(nc.getTime());
//            }
//            System.out.println("++++++++++++++++++++++++++++++++++++++++++");
        }
    }

    public void delete(Task task) {
        TaskDB.databaseWriteExecutor.execute(() -> {
            mtaskDB.delete(task);
        });
    }

    private List<Task> generateTaskDays(Task task) {
        List<Task> task_days = new ArrayList<>();

        if(task.repeat == -1){
            task_days.add(task);
        }else{
            int unit;
            switch (task.repeat_unit){
                case 0:
                    unit = Calendar.DAY_OF_MONTH;
                    break;
                case 1:
                    unit = Calendar.WEEK_OF_MONTH;
                    break;
                case 2:
                    unit = Calendar.MONTH;
                    break;
                case 3:
                    unit = Calendar.YEAR;
                    break;
                default:
                    unit = Calendar.DAY_OF_MONTH;
                    break;
            }
            Calendar cs = Time.getDate();
            cs.setTimeInMillis(task.day_from);
            Calendar ce = Time.getDate();
            if(task.type == "short"){
                ce.setTimeInMillis(task.day_from);
                ce.add(Calendar.YEAR, 10);
            }else{
                ce.setTimeInMillis(task.day_to);
            }
            while(cs.getTimeInMillis() <= ce.getTimeInMillis()){
                Task nTask = task.clone(task);
                nTask.day = cs.getTimeInMillis();
                task_days.add(nTask);
                cs.add(unit, task.repeat);
            }
        }
        return task_days;
    }
}