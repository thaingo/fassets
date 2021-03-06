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
package io.github.fasset.fasset.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Objects;

/**
 * This object simply configures the default of the program's default currency
 *
 * @author edwin.njeru
 * @version $Id: $Id
 */
@ConfigurationProperties(prefix = "money")
public class MoneyProperties {

    private String defaultCurrency = "KES";

    /**
     * <p>Constructor for MoneyProperties.</p>
     */
    public MoneyProperties() {
    }

    /**
     * <p>Getter for the field <code>defaultCurrency</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDefaultCurrency() {
        return defaultCurrency;
    }

    /**
     * <p>Setter for the field <code>defaultCurrency</code>.</p>
     *
     * @param defaultCurrency a {@link java.lang.String} object.
     * @return a {@link io.github.fasset.fasset.config.MoneyProperties} object.
     */
    public MoneyProperties setDefaultCurrency(String defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MoneyProperties that = (MoneyProperties) o;
        return Objects.equals(defaultCurrency, that.defaultCurrency);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(defaultCurrency);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "MoneyProperties{" + "defaultCurrency='" + defaultCurrency + '\'' + '}';
    }
}
