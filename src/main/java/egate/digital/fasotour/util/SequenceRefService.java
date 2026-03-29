package egate.digital.fasotour.util;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class SequenceRefService {

    private final SequenceRefRepository repo;

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Transactional
    public String nextRef(String type) {

        SequenceRef seq = repo.findByType(type)
                .orElseGet(() -> createSequence(type));

        // incrément sécurisé (row locked)
        seq.setCurrentValue(seq.getCurrentValue() + 1);

        return buildRef(type, seq.getCurrentValue());
    }

    private SequenceRef createSequence(String type) {
        try {
            return repo.save(new SequenceRef(null, type, 0L));
        } catch (Exception e) {
            // si concurrent a déjà créé → on recharge avec lock
            return repo.findByType(type)
                    .orElseThrow(() -> new RuntimeException("Erreur création séquence"));
        }
    }

    private String buildRef(String type, Long value) {
        String date = LocalDate.now().format(FORMAT);
        return String.format("%s-%s-%04d", type, date, value);
    }
}