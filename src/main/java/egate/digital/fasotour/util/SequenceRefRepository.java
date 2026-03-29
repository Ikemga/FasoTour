package egate.digital.fasotour.util;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface SequenceRefRepository extends JpaRepository<SequenceRef, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<SequenceRef> findByType(String type);
}