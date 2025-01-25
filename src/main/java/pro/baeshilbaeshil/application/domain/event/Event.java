package pro.baeshilbaeshil.application.domain.event;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pro.baeshilbaeshil.application.common.BaseEntity;
import pro.baeshilbaeshil.application.common.exception.InvalidArgumentException;

import java.time.LocalDateTime;

import static pro.baeshilbaeshil.application.common.exception_type.EventExceptionType.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Event extends BaseEntity {

    private static final int NAME_MAX_LENGTH = 20;
    private static final int DESCRIPTION_MAX_LENGTH = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private String name;
    private String description;
    private String imageUrl;

    private LocalDateTime beginTime;
    private LocalDateTime endTime;

    @Builder
    private Event(
            Long productId,
            String name,
            String description,
            String imageUrl,
            LocalDateTime beginTime,
            LocalDateTime endTime) {
        validate(name, description, beginTime, endTime);
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    private void validate(
            String name,
            String description,
            LocalDateTime beginTime,
            LocalDateTime endTime) {
        if (name == null || name.isEmpty() || name.length() > NAME_MAX_LENGTH) {
            throw new InvalidArgumentException(INVALID_EVENT_NAME);
        }
        if (description == null || description.isEmpty() || description.length() > DESCRIPTION_MAX_LENGTH) {
            throw new InvalidArgumentException(INVALID_EVENT_DESCRIPTION);
        }
        if (beginTime == null || endTime == null || beginTime.isAfter(endTime)) {
            throw new InvalidArgumentException(INVALID_EVENT_TIME);
        }
    }
}
