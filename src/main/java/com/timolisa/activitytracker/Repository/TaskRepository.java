package com.timolisa.activitytracker.Repository;

import com.timolisa.activitytracker.Model.Task;
import com.timolisa.activitytracker.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query(value = "SELECT * FROM tasks", nativeQuery = true)
    List<Task> findAllTasks();

    @Query(value = "SELECT * FROM tasks WHERE id = ?1", nativeQuery = true)
    Optional<Task> findTaskById(long id);

    List<Task> findTaskByStatus(Status status);
}
