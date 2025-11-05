package com.cineclub.cineclub.repository;

import com.cineclub.cineclub.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {
    
    Page<Movie> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);
    
    @Query("SELECT m FROM Movie m WHERE " +
           "(:titulo IS NULL OR LOWER(m.titulo) LIKE LOWER(CONCAT('%', :titulo, '%')))")
    Page<Movie> searchMovies(@Param("titulo") String titulo, Pageable pageable);
    
    Optional<Movie> findByTitulo(String titulo);
}

