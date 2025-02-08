package com.example.demo.repository;

import com.example.demo.dao.LibroDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepository extends JpaRepository<LibroDAO, Long> {
}
