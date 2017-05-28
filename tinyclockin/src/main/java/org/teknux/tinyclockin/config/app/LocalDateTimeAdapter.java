package org.teknux.tinyclockin.config.app;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;


/**
 * JAXB adaptor for serializing/deserializing Java 8 {@link LocalDateTime} to ISO8601.
 *
 * @author Francois EYL
 */
public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

    @Override
    public LocalDateTime unmarshal(String dateString) throws Exception {
        Instant instant = Instant.parse(dateString);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return dateTime;
    }

    @Override
    public String marshal(LocalDateTime localDate) throws Exception {
        Instant instant = localDate.toInstant(ZoneOffset.UTC);
        return DateTimeFormatter.ISO_INSTANT.format(instant);
    }
}
