package pro.baeshilbaeshil.application.domain.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "select * from events e " +
            "where e.begin_time <= :date and e.end_time > :date",
            nativeQuery = true)
    List<Event> findActiveEvents(@Param("date") LocalDateTime date);
}
