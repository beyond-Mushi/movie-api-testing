package org.example.movieapi.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.example.movieapi.dto.MovieDto;
import org.example.movieapi.dto.MoviePageResponse;
import org.example.movieapi.entity.Movie;
import org.example.movieapi.exception.FileExistsException;
import org.example.movieapi.exception.MovieNotFoundException;
import org.example.movieapi.repository.MovieRepository;
import org.example.movieapi.service.FileService;
import org.example.movieapi.service.MovieService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final FileService fileService;

    @Value("${base.url}")
    private String baseUrl;
    @Value("${project.poster}")
    private String path;

    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {
        if (Files.exists(Paths.get(path+ File.separator+file.getOriginalFilename()))) {
            throw new FileExistsException("File already exists! Please insert another file!");
        }
        String uploadedFileName = fileService.uploadFile(path, file);
        movieDto.setPoster(uploadedFileName);
        Movie movie = new Movie(
                null,
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );
        Movie savedMovie = movieRepository.save(movie);
        String posterUrl = baseUrl+"/file/"+uploadedFileName;
        MovieDto response = new MovieDto(
                savedMovie.getMovieId(),
                savedMovie.getTitle(),
                savedMovie.getDirector(),
                savedMovie.getStudio(),
                savedMovie.getMovieCast(),
                savedMovie.getReleaseYear(),
                savedMovie.getPoster(),
                posterUrl
        );
        return response;
    }

    @Override
    public MovieDto getMovie(Integer movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(()->new MovieNotFoundException("Movie is not found"));
        String posterUrl = baseUrl + "file" + movie.getPoster();
        MovieDto movieDto = new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl
        );
        return movieDto;
    }

    @Override
    public List<MovieDto> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        List<MovieDto> movieDtos = new ArrayList<>();

         movies.stream().map(m -> {
             String posterUrl = baseUrl + "/file/" + m.getPoster();
             return new MovieDto(
                     m.getMovieId(),
                     m.getTitle(),
                     m.getDirector(),
                     m.getStudio(),
                     m.getMovieCast(),
                     m.getReleaseYear(),
                     m.getPoster(),
                     posterUrl
                );
             }
         ).forEach(movieDtos::add);
         return movieDtos;
    }

    @Override
    public MovieDto updateMovie(Integer id, MovieDto movieDto, MultipartFile file)
            throws IOException {
        Movie mv = movieRepository.findById(id)
                .orElseThrow(()->new MovieNotFoundException("Movie is not found!"));

        String fileName = mv.getPoster();
        if ( file != null) {
            Files.deleteIfExists(Paths.get(path+File.separator+fileName));
            fileName = fileService.uploadFile(path, file);
        }
        movieDto.setPoster(fileName);
        Movie movie = new Movie(
                mv.getMovieId(),
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );
        Movie updatedMovie = movieRepository.save(movie);
        String posterUrl = baseUrl+ "/file/" + fileName;
        MovieDto response = new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl
        );
        return response;
    }

    @Override
    public String deleteMovie(Integer movieId) throws IOException{
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(()->new MovieNotFoundException("Movie is not found!"));
        Integer id = movie.getMovieId();
        Files.deleteIfExists(Paths.get(path+File.separator+movie.getPoster()));
        movieRepository.delete(movie);
        return "Movie deleted with id : "+id;
    }

    @Override
    public MoviePageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Movie> moviePages = movieRepository.findAll(pageable);
        List<Movie> movies = moviePages.getContent();
        List<MovieDto> movieDtos = new ArrayList<>();

        movieDtos = movies.stream().map(m->new MovieDto(
                m.getMovieId(),
                m.getTitle(),
                m.getDirector(),
                m.getStudio(),
                m.getMovieCast(),
                m.getReleaseYear(),
                m.getPoster(),
                baseUrl+"/file/"+m.getPoster()
        )).collect(Collectors.toUnmodifiableList());

        return new MoviePageResponse(movieDtos, pageNumber,pageSize,
                                    moviePages.getTotalElements(),
                                    moviePages.getTotalPages(),
                                    moviePages.isLast());
    }

    @Override
    public MoviePageResponse getAllMoviesWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir) {
        Sort sort = dir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Movie> moviePages = movieRepository.findAll(pageable);
        List<Movie> movies = moviePages.getContent();

        List<MovieDto> movieDtos = movies.stream().map(m->new MovieDto(
                m.getMovieId(),
                m.getTitle(),
                m.getDirector(),
                m.getStudio(),
                m.getMovieCast(),
                m.getReleaseYear(),
                m.getPoster(),
                baseUrl+"/file/"+m.getPoster()
        )).collect(Collectors.toUnmodifiableList());

        return new MoviePageResponse(movieDtos, pageNumber,pageSize,
                moviePages.getTotalElements(),
                moviePages.getTotalPages(),
                moviePages.isLast());
    }
}
