package pro.baeshilbaeshil.application.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pro.baeshilbaeshil.application.common.BaseEntity;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private Integer points = 0;

    @Builder
    private User(String email) {
        this.email = email;
    }

    public void addPoints(int pointsToAdd) {
        this.points += pointsToAdd;
    }
}
