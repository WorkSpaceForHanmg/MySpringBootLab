package com.rookies3.myspringbootlab.repository;

import com.rookies3.myspringbootlab.entity.Publisher;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    Optional<Publisher> findByName(String name);

    boolean existsByName(String name);

    // Fetch join으로 도서 리스트까지 한 번에 로딩
    @EntityGraph(attributePaths = "books")
    Optional<Publisher> findByIdWithBooks(Long id);
}
