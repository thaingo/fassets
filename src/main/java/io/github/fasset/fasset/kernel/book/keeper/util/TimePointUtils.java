/*
 * fassets - Project for light-weight tracking of fixed assets
 * Copyright © 2018 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.github.fasset.fasset.kernel.book.keeper.util;

import io.github.ghacupha.time.point.SimpleDate;
import io.github.ghacupha.time.point.TimePoint;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Provides static utilities for ease of implementations of the TimePoint and ReadableTime interfaces
 *
 * @author edwin.njeru
 * @version $Id: $Id
 */
public class TimePointUtils {

    /**
     * Reads dateString and return equivalent {@link io.github.ghacupha.time.point.TimePoint} object
     *
     * @param dateString  String from which we are extracting the date
     * @param datePattern Date pattern to be used in parsing the date string argument
     * @return Equivalent TimePoint for the string
     */
    public static TimePoint parseString(String dateString, String datePattern) {

        return new SimpleDate(LocalDate.parse(dateString, DateTimeFormatter.ofPattern(datePattern)));
    }
}
