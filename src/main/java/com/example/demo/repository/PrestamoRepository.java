package com.example.demo.repository;

import com.example.demo.dao.PrestamoDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepository extends JpaRepository<PrestamoDAO, Long> {
    boolean existsByLibroId(Long libroId);
    boolean existsByUsuarioId(Long libroId);
}
