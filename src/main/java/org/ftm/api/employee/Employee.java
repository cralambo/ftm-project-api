package org.ftm.api.employee;

import jakarta.persistence.*;
import lombok.*;
import org.ftm.api.department.Department;
import org.ftm.api.position.Position;
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
        name = "employees",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_employees_user_id",
                        columnNames = "user_id"
                )
        }
)
@EntityListeners(AuditingEntityListener.class)
public class Employee {
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
            name = "employee_number",
            length = 10,
            nullable = false,
            unique = true
    )
    private String employeeNumber;

    @Column(
            name = "acronym",
            nullable = false,
            length = 10,
            unique = true
    )
    private String acronym;

    @Column(
            name = "personal_email",
            length = 150
    )
    private String personalEmail;

    @Column(
            name = "professional_email",
            length = 150,
            nullable = false
    )
    private String professionalEmail;

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

    @Column(
            name = "hire_date",
            nullable = false
    )
    private LocalDate hireDate;

    @ManyToOne
    @JoinColumn(
            name = "position_id"
    )
    private Position position;

    @ManyToOne
    @JoinColumn(
            name = "department_id"
    )
    private Department department;

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
