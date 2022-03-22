package com.example.blps.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotBlank
    @Column(name = "firstName")
    private String firstName;

    @NotBlank
    @Column(name = "lastName")
    private String lastName;

    //8 988 021 4995
    @Pattern(regexp = "(^$|[0-9]{11})")
    @Column(name = "phone")
    private String phoneNumber;

    @NotBlank
    @Column(name = "status", columnDefinition = "varchar(255) default 'Nothing'")
    private String status;

    @ManyToMany
    @JoinTable(
            name = "user_film",
            joinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "films_id", referencedColumnName = "id"))
    private Set<Films> userFilm = new HashSet<>();

}
