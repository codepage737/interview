package com.shahrabi.interview.repository.main;

import com.shahrabi.interview.domain.main.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, UUID>, JpaSpecificationExecutor<Borrow> {
    Optional<Borrow> findFirstByBookIdAndReturnDateIsNullOrderByBorrowDateDesc(UUID bookId);
}
