package io.github.fasset.fasset.kernel.batch.depreciation.agent;

import io.github.fasset.fasset.kernel.batch.depreciation.CategoryConfigurationRegistry;
import io.github.fasset.fasset.kernel.batch.depreciation.DepreciationPreprocessor;
import io.github.fasset.fasset.kernel.batch.depreciation.colleague.Colleague;
import io.github.fasset.fasset.kernel.batch.depreciation.colleague.Update;
import io.github.fasset.fasset.kernel.batch.depreciation.model.DepreciationUpdate;
import io.github.fasset.fasset.kernel.messaging.DepreciationUpdateDispatcher;
import io.github.fasset.fasset.kernel.util.DepreciationExecutionException;
import io.github.fasset.fasset.model.CategoryConfiguration;
import io.github.fasset.fasset.model.Depreciation;
import io.github.fasset.fasset.model.FixedAsset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component("depreciationAgent")
public class DepreciationAgentImpl extends Colleague implements DepreciationAgent {

    private final Logger log = LoggerFactory.getLogger(DepreciationAgentImpl.class);

    private final CategoryConfigurationRegistry categoryConfigurationRegistry;
    private final DepreciationPreprocessor preprocessor;

    @Autowired
    public DepreciationAgentImpl(@Qualifier("depreciationUpdateDispatcher")DepreciationUpdateDispatcher depreciationUpdateDispatcher, CategoryConfigurationRegistry categoryConfigurationRegistry, DepreciationPreprocessor preprocessor) {
        super(depreciationUpdateDispatcher);
        this.categoryConfigurationRegistry = categoryConfigurationRegistry;
        this.preprocessor = preprocessor;
    }


    @Override
    public Depreciation invoke(FixedAsset asset, YearMonth month) {

        Depreciation depreciation;

        log.trace("Calculating depreciation for fixedAsset {}", asset);

        String categoryName = asset.getCategory();

        log.trace("Fetching categoryConfiguration instance from repository for designation : {}", categoryName);

        CategoryConfiguration configuration = categoryConfigurationRegistry.getCategoryConfiguration(categoryName);

        log.trace("Using categoryConfiguration instance : {}", configuration);

        double depreciationRate = configuration.getDepreciationRate();

        double deprecant = getDeprecant(asset, configuration);

        log.trace("Using deprecant : {}, and depreciation rate : {} for calculating depreciation", deprecant, depreciationRate);
        double depreciationAmount = calculate(deprecant, depreciationRate);

        depreciation = getDepreciation(preprocessor.setAsset(asset).setMonth(month).setDepreciationAmount(depreciationAmount).setProperties());

        double nbv = asset.getNetBookValue() - depreciation.getDepreciation();

        //send changes to queue for flushing through entityManager
        send(new DepreciationUpdate.from(asset.setNetBookValue(nbv)).setDestination(asset.getClass()));

        return depreciation;

    }

    /**
     * This method determines the amount to be used as deprecant relative to the CategoryConfiguration
     * item passed
     *
     * @param asset FixedAsset item being depereciated
     * @param configuration CategoryConfiguration relevant to the asset being depreciated
     * @return amount of the deprecant
     */
    private double getDeprecant(FixedAsset asset, CategoryConfiguration configuration) {

        log.trace("Determining the deprecant for Asset : {}, with category configuration : {}",
                asset,configuration);

        double deprecant = 0.00;

        try {
            if(configuration.getDeprecant().equalsIgnoreCase("purchaseCost")){

                deprecant = asset.getPurchaseCost();

                log.trace("Using purchase cost as deprecant : {}",deprecant);

            } else if(configuration.getDeprecant().equalsIgnoreCase("netBookValue")){

                deprecant = asset.getNetBookValue();

                log.trace("Using the netBookValue as deprecant : {}",deprecant);
            }
        } catch (Throwable e) {
            String message = String.format("Exception encountered while determining the deprecant applicable for the " +
                    "asset : %s, pursuant to the categoryConfiguration : %s",asset,configuration);
            throw new DepreciationExecutionException(message,e);
        }
        return deprecant;
    }

    /**
     * This calculates the depreciation amount per month
     *
     * @param deprecant the amount of the asset (cost or NBV) on which depreciation is calculated
     * @param depreciationRate the depreciation rate to use
     * @return amount of depreciation
     */
    //@Cacheable
    private double calculate(double deprecant, double depreciationRate){

        double depreciation;

        try {
            log.trace("Calculating depreciation amount using deprecant of : {}, and depreciation rate of : {}", deprecant,depreciationRate);

            depreciation = deprecant * depreciationRate/100 * 1/12;

            log.trace("Depreciation for deprecant : {} and depreciationRate : {} calculated as : {}",deprecant,depreciationRate,depreciation);
        } catch (Throwable e) {
            String message = String.format("Exception encountered while calculating depreciation amount for " +
                    "deprecant amount of : %s and depreciation rate of :%s",deprecant,depreciationRate);
            throw new DepreciationExecutionException(message,e);
        }

        return depreciation;
    }

    /**
     * Creates a depreciation item using data from the preprocessor
     *
     * @param preprocessor DepreciationPreprocessor for the depreciation amounts
     * @return Depreciation item in accordance with the preprocessor passed
     */
    private Depreciation getDepreciation(DepreciationPreprocessor preprocessor){

        log.trace("Creating depreciation instance relative to the fixedAsset item : {} for the month : {}",preprocessor.getAsset(),preprocessor.getMonth());
        Depreciation depreciation = new Depreciation();
        try {
            depreciation
                    .setDepreciationPeriod(preprocessor.getMonth())
                    .setFixedAssetId(preprocessor.getAsset().getId())
                    .setCategory(preprocessor.getAsset().getCategory())
                    .setSolId(preprocessor.getAsset().getSolId())
                    .setYear(preprocessor.getMonth().getYear())
                    .setMonth(preprocessor.getMonth().getMonthValue())
                    .setDepreciation(preprocessor.getDepreciationAmount());
        } catch (Throwable e) {
            String message = String.format("Exception encountered while creating depreciation instance relative to" +
                    " asset : %s, for the period : %s",preprocessor.getAsset(),preprocessor.getMonth());
            throw new DepreciationExecutionException(message,e);
        }

        log.trace("Returning depreciation instance : {}",depreciation);
        return depreciation;
    }


    /**
     * This method listens for message sent to a queue
     * containing the Object of type U and formulates appropriate
     * response
     *
     * @param updateMessage Update item  to be received and processed by the Colleague
     */
    @Override
    public void receive(Update updateMessage) {
        // crickets
    }
}