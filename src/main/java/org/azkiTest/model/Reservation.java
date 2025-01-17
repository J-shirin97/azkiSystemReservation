package org.azkiTest.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "RESERVATIONS",
        uniqueConstraints = @UniqueConstraint(columnNames = {"USER_ID", "SLOT_ID"}))
public class Reservation {

    @Id
    @Column(name = "ID")
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SLOT_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_RESERVATION_SLOT"))
    private AvailableSlot slot;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_RESERVATION_USER"))
    private Users user;

    @Column(name = "RESERVED_AT", nullable = false)
    private LocalDateTime reservedAt;

}