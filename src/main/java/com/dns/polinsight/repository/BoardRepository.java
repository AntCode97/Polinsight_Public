package com.dns.polinsight.repository;

import com.dns.polinsight.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
