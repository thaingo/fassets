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
package io.github.fasset.fasset.kernel.util;

/**
 * <br> Jsonable Interface <br> This interface is a small utility whose clients can convert their internals into a JSON string. <br> A simple call to {link this#toJson} will return a string with JSON
 * formatted string.
 *
 * @author edwin.njeru
 * @version $Id: $Id
 */
public interface Jsonable {

    /**
     * <p>toJson.</p>
     *
     * @return String representing the object notation of the client
     */
    String toJson();
}
