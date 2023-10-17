package com.forcat.forcat.repository;

import com.forcat.forcat.entity.Content;
import com.forcat.forcat.repository.Search.SearchContentRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ContentRepository extends JpaRepository<Content, Long>, SearchContentRepository {

    @EntityGraph (attributePaths = {"imageSet"})
    @Query ("select c from Content c where c.cno =:cno")
    Optional<Content> findByIdWithContentImages (@Param ("cno") Long cno);
} // ContentRepository
