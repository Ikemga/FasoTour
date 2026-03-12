package egate.digital.fasotour.model;


import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Duration;

@Converter(autoApply = false)
public class DurationConverter implements AttributeConverter<Duration, Long> {

    @Override
    public Long convertToDatabaseColumn(Duration duration) {
        return duration != null ? duration.toDays() : null;
    }

    @Override
    public Duration convertToEntityAttribute(Long minutes) {
        return minutes != null ? Duration.ofMinutes(minutes) : null;
    }
}
