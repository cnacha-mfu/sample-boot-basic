package th.mfu;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateSerializer extends StdSerializer<LocalDate> {

    private static DateTimeFormatter FORMATTER =  DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public LocalDateSerializer() {
        this(null);
    }

    public LocalDateSerializer(Class<LocalDate> clazz) {
        super(clazz);
    }

    @Override
    public void serialize(LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(localDate.format(FORMATTER));
    }
}