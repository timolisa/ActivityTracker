package com.timolisa.activitytracker.Repository;

import com.timolisa.activitytracker.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query(value = "SELECT * FROM tasks", nativeQuery = true)
    List<Task> findAllTasks();

    @Query(value = "SELECT * FROM tasks WHERE id = ?1", nativeQuery = true)
    Task findTaskById(long id);

    @Query(value = "SELECT * FROM tasks WHERE status = 'PENDING'", nativeQuery = true)
    List<Task> findAllPendingTasks();

    @Query(value = "SELECT * FROM tasks WHERE status = 'IN_PROGRESS'", nativeQuery = true)
    List<Task> findAllTasksInProgress();

    @Query(value = "SELECT * FROM tasks WHERE status = 'COMPLETED'", nativeQuery = true)
    List<Task> findAllCompletedTasks();
}
