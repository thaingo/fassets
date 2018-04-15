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

package io.github.fasset.fasset.service;

import io.github.fasset.fasset.model.brief.CategoryBrief;

import java.util.List;

public interface CategoryBriefService {

    /**
     *
     * @return {@link List<CategoryBrief>} fetched from the repository
     */
    List<CategoryBrief> fetchAllCategoryBriefs();

    /**
     *
     * @param id of the CategoryBrief
     * @return {@link CategoryBrief} of the id given as param
     */
    CategoryBrief fetchCategoryBriefGivenId(int id);

    /**
     *
     * @param categoryBriefs items to be saved to repository
     */
    void saveAllCategoryBriefItems(List<? extends CategoryBrief> categoryBriefs);
}
