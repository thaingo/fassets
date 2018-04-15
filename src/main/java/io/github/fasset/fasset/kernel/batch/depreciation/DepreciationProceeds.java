/*
 *     This file is part of fassets
 *     Copyright (C) 2018 Edwin Njeru
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.fasset.fasset.kernel.batch.depreciation;

import io.github.fasset.fasset.model.AccruedDepreciation;
import io.github.fasset.fasset.model.Depreciation;
import io.github.fasset.fasset.model.NetBookValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class DepreciationProceeds {

    private static final Logger log = LoggerFactory.getLogger(DepreciationProceeds.class);

    private Depreciation depreciation;

    private NetBookValue netBookValue;

    private AccruedDepreciation accruedDepreciation;

    public DepreciationProceeds() {
    }

    public Depreciation getDepreciation() {
        return depreciation;
    }

    public DepreciationProceeds setDepreciation(Depreciation depreciation) {
        log.debug("Proceeds#1 : Setting depreciation as {}", depreciation);
        this.depreciation = depreciation;
        return this;
    }

    public NetBookValue getNetBookValue() {
        return netBookValue;
    }

    public DepreciationProceeds setNetBookValue(NetBookValue netBookValue) {
        log.debug("Proceeds#2 : Setting netBookValue as {}", netBookValue);
        this.netBookValue = netBookValue;
        return this;
    }

    public AccruedDepreciation getAccruedDepreciation() {
        return accruedDepreciation;
    }

    public DepreciationProceeds setAccruedDepreciation(AccruedDepreciation accruedDepreciation) {
        log.debug("Proceeds#3 : Setting accruedDepreciation as {}", accruedDepreciation);
        this.accruedDepreciation = accruedDepreciation;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DepreciationProceeds that = (DepreciationProceeds) o;
        return Objects.equals(depreciation, that.depreciation) &&
                Objects.equals(netBookValue, that.netBookValue) &&
                Objects.equals(accruedDepreciation, that.accruedDepreciation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(depreciation, netBookValue, accruedDepreciation);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DepreciationProceeds{");
        sb.append("depreciation=").append(depreciation);
        sb.append(", netBookValue=").append(netBookValue);
        sb.append(", accruedDepreciation=").append(accruedDepreciation);
        sb.append('}');
        return sb.toString();
    }
}
