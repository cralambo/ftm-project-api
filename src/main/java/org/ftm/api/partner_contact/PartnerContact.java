package org.ftm.api.partner_contact;

import jakarta.persistence.*;
import lombok.*;
import org.ftm.api.partner.SupportPartner;
import org.ftm.api.user.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
        name = "partner_contacts",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_partner_contacts_user_id",
                        columnNames = "user_id"
                )
        }
)
@EntityListeners(AuditingEntityListener.class)
public class PartnerContact {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(
            name = "firstname",
            length = 150,
            nullable = false
    )
    private String firstname;

    @Column(
            name = "lastname",
            length = 150
    )
    private String lastname;

    @Column(
            name = "email",
            length = 150,
            nullable = false
    )
    private String email;

    @Column(
            name = "phone",
            length = 20,
            nullable = false
    )
    private String phone;

    @Column(
            name = "address",
            columnDefinition = "TEXT"
    )
    private String address;

    @ManyToOne
    @JoinColumn(name = "support_partner_id")
    private SupportPartner supportPartner;

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
