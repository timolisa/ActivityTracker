package com.timolisa.activitytracker.repository;

import com.timolisa.activitytracker.entity.Task;
import com.timolisa.activitytracker.enums.Status;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query(value = "SELECT * FROM tasks WHERE id = ?1", nativeQuery = true)
    Optional<Task> findTaskById(long id);

    @Query(value = "SELECT * FROM tasks WHERE user_id = :userId ORDER BY id ASC ", nativeQuery = true)
    List<Task> findTasksByUser(@Param("userId") Long userId);

    @Query(value = "SELECT * FROM tasks WHERE id = :id AND user_id = :userId", nativeQuery = true)
    Optional<Task> findTaskForUser(@Param("id") Long id, @Param("userId") Long userId);

    // JPQL is used to create queries against entities to store in a RDB.
    @Query(value = "SELECT t FROM Task t WHERE t.user.id = :userId AND LOWER(t.title) LIKE LOWER(concat('%', :query, '%')) ORDER BY t.id ASC")
    List<Task> search(@Param("userId") Long userId, @Param("query") String query);

    @Query(value = "SELECT t FROM Task t WHERE t.user.id = :userId AND t.status = :status")
    List<Task> findTasksByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Status status);

    void deleteById(@NonNull Long id);
}
