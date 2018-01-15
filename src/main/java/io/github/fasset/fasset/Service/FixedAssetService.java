package io.github.fasset.fasset.Service;

import io.github.fasset.fasset.model.FixedAsset;

import java.util.List;

public interface FixedAssetService {

    /**
     * Saves all {@link FixedAsset} items passed in a list
     * @param fixedAssets
     */
    void saveAllFixedAssets(List<? extends FixedAsset> fixedAssets);

    /**
     * Fetches a List of all existing items in the {@link io.github.fasset.fasset.repository.FixedAssetRepository}
     * @return
     */
    List<FixedAsset> fetchAllExistingAssets();
}
