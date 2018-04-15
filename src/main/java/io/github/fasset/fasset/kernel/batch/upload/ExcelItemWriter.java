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

package io.github.fasset.fasset.kernel.batch.upload;


import io.github.fasset.fasset.service.FixedAssetService;
import io.github.fasset.fasset.model.FixedAsset;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("excelItemWriter")
public class ExcelItemWriter implements ItemWriter<FixedAsset> {

    @Autowired
    @Qualifier("fixedAssetService")
    private FixedAssetService fixedAssetService;


    @Override
    public void write(List<? extends FixedAsset> list) throws Exception {

        fixedAssetService.saveAllFixedAssets(list);
    }
}
