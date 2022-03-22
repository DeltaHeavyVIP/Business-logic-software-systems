package com.example.blps.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "genres")
public class Genres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotBlank
    @Column(name = "genre")
    private String genre;

    @ManyToMany
    @JoinTable(
            name = "film_genre",
            joinColumns = @JoinColumn(name = "genres_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "films_id", referencedColumnName = "id"))
    private Set<Films> genreFilm = new HashSet<>();
}
