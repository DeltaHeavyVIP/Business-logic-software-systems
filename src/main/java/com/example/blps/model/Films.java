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
@Table(name = "films")
public class Films {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotBlank
    @Column(name = "name")
    private String name;

    @NotBlank
    @Column(name = "description")
    private String description;

    @Column(name = "cost")
    private Integer cost;

//    @Lob
//    @Column(name = "picture", columnDefinition = "BLOB")
//    private byte[] filmPicture;

    @ManyToMany(mappedBy = "genreFilm", fetch = FetchType.LAZY)
    private Set<Users> filmGenre = new HashSet<>();

    @ManyToMany(mappedBy = "userFilm", fetch = FetchType.LAZY)
    private Set<Users> filmUser = new HashSet<>();
}
