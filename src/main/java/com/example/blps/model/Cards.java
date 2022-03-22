package com.example.blps.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

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
    @Pattern(regexp="(^$|[0-9]{16})")
    @Column(name = "cardNumber")
    private String cardNumber;

    @NotBlank
    @Column(name = "cardDateEnd")
    private Date cardDateEnd;

    @Column(name = "cardCVC")
    private Integer cardCVC;

    @ManyToOne
    @JoinColumn(name = "users", referencedColumnName = "id")
    private Users user;
}
