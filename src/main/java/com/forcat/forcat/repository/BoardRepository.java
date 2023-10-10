package com.forcat.forcat.repository;

import com.forcat.forcat.entity.Board;
import com.forcat.forcat.repository.Search.SearchBoardRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, SearchBoardRepository {
    @EntityGraph (attributePaths = {"imageSet"})
    @Query ("select b from Board b where b.bno =:bno")
    Optional<Board> findByIdWithImages (@Param ("bno") Long bno);
}
