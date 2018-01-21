package io.github.fasset.fasset.service.impl;

import io.github.fasset.fasset.kernel.util.DataRetrievalFromServiceException;
import io.github.fasset.fasset.model.FixedAsset;
import io.github.fasset.fasset.model.brief.CategoryBrief;
import io.github.fasset.fasset.model.brief.ServiceOutletBrief;
import io.github.fasset.fasset.repository.FixedAssetRepository;
import io.github.fasset.fasset.kernel.util.ImmutableListCollector;
import io.github.fasset.fasset.service.FixedAssetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("fixedAssetService")
@Transactional
public class FixedAssetServiceImpl implements FixedAssetService {

    private static final Logger log = LoggerFactory.getLogger(FixedAssetServiceImpl.class);

    @Autowired
    @Qualifier("fixedAssetRepository")
    private FixedAssetRepository fixedAssetRepository;

    /**
     * Saves all {@link FixedAsset} items passed in a list
     *
     * @param fixedAssets
     */
    @Override
    public void saveAllFixedAssets(List<? extends FixedAsset> fixedAssets) {

        fixedAssetRepository.saveAll(fixedAssets);
    }

    /**
     * Fetches a List of all existing items in the {@link FixedAssetRepository}
     *
     * @return
     */
    @Override
    public List<FixedAsset> fetchAllExistingAssets() {

        return fixedAssetRepository.findAll()
                .parallelStream()
                .sorted()
                .collect(ImmutableListCollector.toImmutableList());
    }

    /**
     * Extracts the fixed asset when the id is known
     *
     * @param id
     * @return
     */
    @Override
    @Cacheable
    public FixedAsset fetchAssetGivenId(int id) {

        return fixedAssetRepository.findById(id).get();
    }

    /**
     * By querying the {@link FixedAssetRepository} this method
     * is able to create a {@link CategoryBrief} for the category given in the parameter
     *
     * @param category for which we are preparing a brief
     * @return {@link CategoryBrief}
     */
    @Override
    public CategoryBrief getCategoryBrief(String category) {
        CategoryBrief brief = new CategoryBrief();

        log.info("Preparing a brief for category : {}",category);

        try {
            brief.setDesignation(category);
            brief.setPurchaseCost(fixedAssetRepository.getTotalCategoryPurchaseCost(category));
            brief.setNetBookValue(fixedAssetRepository.getTotalCategoryNetBookValue(category));
            brief.setPoll(fixedAssetRepository.getTotalCategoryCount(category));
            brief.setAccruedDepreciation(brief.getPurchaseCost() - brief.getNetBookValue());
        } catch (Throwable e) {
            String message = String.format("Exception encountered while creating a categoryBrief for category : %s",category);
            throw new DataRetrievalFromServiceException(message,e);
        }

        log.debug("Brief for category returned : {}",brief);

        return brief;
    }

    /**
     * By querying the {@link FixedAssetRepository} this method
     * is able to create a {@link ServiceOutletBrief} for the SOL queried in the parameter
     *
     * @param solId for which we are preparing a brief
     * @return {@link CategoryBrief}
     */
    @Override
    public ServiceOutletBrief getServiceOutletBrief(String solId) {
        ServiceOutletBrief brief = new ServiceOutletBrief();

        log.info("Preparing a brief for solId : {}",solId);

        try {
            brief.setDesignation(solId);
            brief.setPurchaseCost(fixedAssetRepository.getTotalSolPurchaseCost(solId));
            brief.setNetBookValue(fixedAssetRepository.getTotalSolNetBookValue(solId));
            brief.setAccruedDepreciation(brief.getPurchaseCost() - brief.getNetBookValue());
            brief.setPoll(fixedAssetRepository.getTotalSolCount(solId));
        } catch (Throwable e) {
            String message = String.format("Exception encountered while creating a serviceOutletBrief for solId : %s",solId);
            throw new DataRetrievalFromServiceException(message,e);
        }

        log.debug("Brief for service outlet returned : {}",brief);

        return brief;
    }
}
