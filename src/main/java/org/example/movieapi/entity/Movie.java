package org.example.movieapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer movieId;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "Provide a title!")
    private String title;

    @Column(nullable = false)
    @NotBlank(message = "Provide a director name!")
    private String director;

    @Column(nullable = false)
    @NotBlank(message = "Provide a studio name!")
    private String studio;

    @ElementCollection
    @CollectionTable(name = "movie_cast")
    private Set<String> movieCast;

    @Column(nullable = false)
    @NotBlank(message = "Provide a release year!")
    private Integer releaseYear;

    @Column(nullable = false)
    @NotBlank(message = "Provide a poster name!")
    private String poster;
}
