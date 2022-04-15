package com.example.blps.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "cards")
public class Cards {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Integer id;

    @NotBlank
    @Pattern(regexp = "(^$|[0-9]{16})")
    @Column(name = "cardNumber")
    private String cardNumber;

    @NotNull
    @Column(name = "cardDateEnd")
    private LocalDate cardDateEnd;

    @NotNull
    @Column(name = "cardCVC")
    private Integer cardCVC;

    @NotNull
    @Column(name = "money")
    private Integer money;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "users", referencedColumnName = "id")
    private Users user;
}
