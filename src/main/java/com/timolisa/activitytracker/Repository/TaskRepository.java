package com.timolisa.activitytracker.Repository;

import com.timolisa.activitytracker.Model.Task;
import com.timolisa.activitytracker.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query(value = "SELECT * FROM tasks", nativeQuery = true)
    List<Task> findAllTasks();

    @Query(value = "SELECT * FROM tasks WHERE id = ?1", nativeQuery = true)
    Optional<Task> findTaskById(long id);
    // JPQL is used to create queries against entities to store in a RDB.
    @Query(value = "SELECT t FROM Task t WHERE LOWER(t.title) LIKE LOWER(concat('%', :query, '%'))")
//    @Query("SELECT t FROM Task t WHERE LOWER(t.name) LIKE LOWER(concat('%', :query, '%')) OR LOWER(t.keywords) LIKE LOWER(concat('%', :query, '%'))")
    List<Task> search(@Param("query") String query);
    List<Task> findTaskByStatus(Status status);
    void deleteById(Long id);
}
