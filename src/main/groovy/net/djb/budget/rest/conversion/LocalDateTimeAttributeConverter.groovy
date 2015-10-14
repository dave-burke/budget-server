package net.djb.budget.rest.conversion;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {
	
	@Override
	public Timestamp convertToDatabaseColumn(LocalDateTime dateTime) {
		return dateTime == null ? null : Timestamp.valueOf(dateTime);
	}

	@Override
	public LocalDateTime convertToEntityAttribute(Timestamp timestamp) {
		return timestamp == null ? null : timestamp.toLocalDateTime();
	}

}

