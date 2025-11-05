package com.cineclub.cineclub.specification;

import com.cineclub.cineclub.dto.MovieFilterDto;
import com.cineclub.cineclub.model.Movie;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MovieSpecifications {

    public static Specification<Movie> buildFromDto(MovieFilterDto filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtro == null) {
                return cb.conjunction();
            }

            if (filtro.getTitulo() != null && !filtro.getTitulo().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("titulo")), "%" + filtro.getTitulo().toLowerCase() + "%"));
            }

            if (filtro.getSinopsis() != null && !filtro.getSinopsis().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("sinopsis")), "%" + filtro.getSinopsis().toLowerCase() + "%"));
            }

            if (filtro.getDuracionMinima() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("duracionMinutos"), filtro.getDuracionMinima()));
            }

            if (filtro.getDuracionMaxima() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("duracionMinutos"), filtro.getDuracionMaxima()));
            }

            if (filtro.getClasificacionEdad() != null && !filtro.getClasificacionEdad().isEmpty()) {
                predicates.add(cb.equal(root.get("clasificacionEdad"), filtro.getClasificacionEdad()));
            }

            if (filtro.getFechaDesde() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("fechaEstreno"), filtro.getFechaDesde()));
            }

            if (filtro.getFechaHasta() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("fechaEstreno"), filtro.getFechaHasta()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}


