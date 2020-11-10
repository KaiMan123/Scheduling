package com.example.scheduling.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM tasks")
    List<Task> getAll();

    @Query("SELECT Day FROM tasks WHERE type = (:type1) EXCEPT SELECT Day FROM tasks WHERE type = (:type2)")
    List<Long> getOne(String type1, String type2);

    @Query("SELECT Day FROM tasks WHERE type = (:type1) INTERSECT SELECT Day FROM tasks WHERE type = (:type2)")
    List<Long> getBoth(String type1, String type2);

    @Query("SELECT * FROM tasks WHERE uid IN (:uuids)")
    List<Task> getByIds(String[] uuids);

    @Query("DELETE FROM tasks WHERE uid IN (:uuids)")
    void deleteByIds(String[] uuids);

    @Query("SELECT * FROM tasks WHERE day = (:date)")
    List<Task> getByDay(Long date);

    @Query("UPDATE tasks SET status = (:status) WHERE day < (:date) AND status NOT IN (:exception)")
    void checkPass(Long date, String status, String[] exception);

    @Insert
    void insertAll(List<Task> tasks);

    @Insert
    void insert(Task task);

    @Delete
    void delete(Task task);

    @Query("DELETE FROM tasks")
    void cleanDB();
}