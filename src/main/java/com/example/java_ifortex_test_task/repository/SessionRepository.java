package com.example.java_ifortex_test_task.repository;

import com.example.java_ifortex_test_task.entity.DeviceType;
import com.example.java_ifortex_test_task.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    /**
     * Method retrieves the first(earliest) desktop Session
     * @param deviceType ENUM type of users device (DeviceType.DESKTOP)
     * @return the session entity
     */
    @Query(value = "SELECT s.id, s.device_type - 1 AS device_type, s.ended_at_utc, s.started_at_utc, s.user_id " + //correct output code from code in database to a deviceType
                           "FROM sessions s " +
                           "WHERE s.device_type = :deviceType + 1 " + //correct input code from a deviceType to code in database
                           "ORDER BY s.started_at_utc " +
                           "LIMIT 1", nativeQuery = true)
    Session getFirstDesktopSession(@Param("deviceType") DeviceType deviceType);

    /**
     * Method retrieves a list of sessions that were ended before 2025 with active users
     * @param endDate input 2025 year in the LocalDateTime format
     * @return list of session entities
     */
    @Query(value = "SELECT s.id, s.device_type - 1 AS device_type, s.ended_at_utc, s.started_at_utc, s.user_id " + //correct output code from code in database to a deviceType
                   "FROM sessions s " +
                   "INNER JOIN users u " +
                   "ON s.user_id = u.id " +
                   "WHERE s.ended_at_utc IS NOT NULL AND s.ended_at_utc < :endDate " +
                   "AND u.deleted = false " +
                   "ORDER BY s.started_at_utc DESC", nativeQuery = true)
    List<Session> getSessionsFromActiveUsersEndedBefore2025(@Param("endDate") LocalDateTime endDate);
}