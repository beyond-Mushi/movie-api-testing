package org.example.movieapi.dto;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MovieDto {

    private Integer movieId;
    @NotBlank(message = "Provide a title!")
    private String title;
    @NotBlank(message = "Provide a director name!")
    private String director;
    @NotBlank(message = "Provide a studio name!")
    private String studio;
    private Set<String> movieCast;
    private Integer releaseYear;
    @NotBlank(message = "Provide a poster name!")
    private String poster;
    @NotBlank(message = "Provide a poster url!")
    private String posterUrl;
}
