package pro.baeshilbaeshil.application.domain.event;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pro.baeshilbaeshil.application.common.BaseEntity;
import pro.baeshilbaeshil.application.common.exception.InvalidArgumentException;

import java.time.LocalDateTime;
import java.util.Objects;

import static pro.baeshilbaeshil.application.common.exception_type.EventExceptionType.*;

@Getter
@Entity
@Table(name = "events")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Event event = (Event) object;
        return Objects.equals(id, event.id) &&
                Objects.equals(productId, event.productId) &&
                Objects.equals(name, event.name) &&
                Objects.equals(description, event.description) &&
                Objects.equals(imageUrl, event.imageUrl) &&
                Objects.equals(beginTime, event.beginTime) &&
                Objects.equals(endTime, event.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId, name, description, imageUrl, beginTime, endTime);
    }
}
