package tabletopsDAO;

import java.time.LocalDateTime;
import java.sql.Timestamp;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {


	@Override
	public Timestamp convertToDatabaseColumn(LocalDateTime locDateTime) {
		if(locDateTime != null){
			return Timestamp.valueOf(locDateTime);
		}
		return null;
	}

	@Override
	public LocalDateTime convertToEntityAttribute(Timestamp sqlTimestamp) {
		if(sqlTimestamp != null){
			return sqlTimestamp.toLocalDateTime();
		}
		return null;
	}

}
