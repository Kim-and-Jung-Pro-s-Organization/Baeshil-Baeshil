package pro.baeshilbaeshil.application.domain.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e WHERE e.beginTime <= :date AND e.endTime > :date")
    List<Event> findActiveEvents(@Param("date") LocalDateTime date);

    @Query(value = """
            select /*+ index(e time_index) */ *
            from events e
            where e.begin_time <= :date
              and e.end_time > :date
            """,
            nativeQuery = true)
    List<Event> findActiveEventsByIndexRangeScan(@Param("date") LocalDateTime date);

    @Query(value = """
            select *
            from events e
            where e.end_time >= :date
            order by e.begin_time
            """,
            nativeQuery = true)
    List<Event> findValidEventsSortedByBeginTime(@Param("date") LocalDateTime now);
}
