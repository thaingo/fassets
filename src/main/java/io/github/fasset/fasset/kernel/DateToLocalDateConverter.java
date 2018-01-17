package io.github.fasset.fasset.kernel;
import io.github.fasset.fasset.kernel.util.ConverterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

@Component("dateToLocalDateConverter")
public class DateToLocalDateConverter implements Converter<Date, LocalDate> {

    private static final Logger log = LoggerFactory.getLogger(DateToLocalDateConverter.class);

    @Override
    public LocalDate convert(Date date) {

        LocalDate converted = null;

        Date toConvert = nullDateReassignment(date);

        log.debug("Converting : {} to LocalDate",date);

        try {

            converted = new java.sql.Date( toConvert.getTime() ).toLocalDate();

        } catch (Throwable e) {

            throw new ConverterException(String.format("Exception thrown while converting %s to localDate", date), e);
        }

        log.debug("{} successfully converted to {}",date, converted);

        return converted;
    }

    private Date nullDateReassignment(Date date){

        if(date == null){

            Date nullDate = Date.from(Instant.now());

            log.warn("The date given is null reassigning to : {}",nullDate);

            return nullDate;

        }
        return date;
    }
}