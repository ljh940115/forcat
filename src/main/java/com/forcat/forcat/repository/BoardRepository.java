package com.forcat.forcat.repository;

import com.forcat.forcat.entity.Board;
import com.forcat.forcat.repository.Search.SearchBoardRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

//JpaRepository를 상속받아 DB 작업
public interface BoardRepository extends JpaRepository<Board, Long>, SearchBoardRepository {
/*    @EntityGraph(attributePaths = {"imageSet"})
    @Query("select b from Board b where b.bno =:bno")
    Optional<Board> findByIdWithImages(Long bno);*/

    @Query(value = "select now()", nativeQuery = true)
    String getTime();
}
