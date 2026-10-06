package org.ftm.api.board_member;

import jakarta.persistence.*;
import lombok.*;
import org.ftm.api.user.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
        name = "board_members",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_board_members_user_id",
                        columnNames = "user_id"
                )
        }
)
@EntityListeners(AuditingEntityListener.class)
public class BoardMember {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(
            name = "firstname",
            nullable = false,
            length = 150
    )
    private String firstname;

    @Column(
            name = "lastname",
            length = 150
    )
    private String lastname;

    @Column(
            name = "personal_email",
            length = 150
    )
    private String personalEmail;

    @Column(
            name = "professional_email",
            length = 150
    )
    private String professionalEmail;

    @Column(
            name = "phone",
            length = 20
    )
    private String phone;

    @Column(
            name = "address",
            columnDefinition = "TEXT"
    )
    private String address;

    @Column(
            name = "position",
            length = 150
    )
    private String position;

    @Column(
            name = "appointment_date",
            nullable = false
    )
    private LocalDate appointmentDate;

    @Column(
            name = "end_date"
    )
    private LocalDate endDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true
    )
    private User user;

    @CreatedDate
    @Column(
            name = "created_date",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(
            name = "last_modified_date",
            insertable = false
    )
    private LocalDateTime lastModifiedDate;
}
