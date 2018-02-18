package io.github.fasset.fasset.kernel.batch.depreciation.agent;

import io.github.fasset.fasset.kernel.batch.depreciation.colleague.Colleague;
import io.github.fasset.fasset.kernel.batch.depreciation.colleague.Update;
import io.github.fasset.fasset.kernel.batch.depreciation.model.DepreciationUpdate;
import io.github.fasset.fasset.kernel.messaging.DepreciationUpdateDispatcher;
import io.github.fasset.fasset.kernel.messaging.dto.AccruedDepreciationDto;
import io.github.fasset.fasset.kernel.util.DepreciationExecutionException;
import io.github.fasset.fasset.model.AccruedDepreciation;
import io.github.fasset.fasset.model.FixedAsset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component("accruedDepreciationAgent")
public class AccruedDepreciationAgentImpl extends Colleague<AccruedDepreciation> implements AccruedDepreciationAgent {

    private static final Logger log = LoggerFactory.getLogger(AccruedDepreciationAgentImpl.class);

    @Autowired
    public AccruedDepreciationAgentImpl(@Qualifier("depreciationUpdateDispatcher") DepreciationUpdateDispatcher depreciationUpdateDispatcher) {
        super(depreciationUpdateDispatcher);
    }

    @Override
    public AccruedDepreciation invoke(FixedAsset asset, YearMonth month) {

        // with fingers crossed : Hope by the time you are here, the fixedAsser netBookValue will have changed
        double depreciationAcc = asset.getPurchaseCost() - asset.getNetBookValue();

        AccruedDepreciation accruedDepreciation = createAccruedDepreciation(asset,month,depreciationAcc);

        send(new DepreciationUpdate.from(new AccruedDepreciationDto(accruedDepreciation)).getPayload().setDestination(accruedDepreciation.getClass()).setSentBy(this));

        return accruedDepreciation;
    }

    /**
     * Creates {@link AccruedDepreciation} instance relative to the parameter items and fixedAsset item given
     * @param asset FixedAsset item whose accruedDepreciation we are to derive
     * @param month YearMonth in which the accruedDepreciation is relevant
     * @param accrual The actual amount of depreciation as double-precision
     * @return AccruedDepreciation item to be returned to the caller for further processing and persistence
     */
    private AccruedDepreciation createAccruedDepreciation(FixedAsset asset, YearMonth month, double accrual) {
        AccruedDepreciation accruedDepreciation = new AccruedDepreciation();

        log.trace("Creating accruedDepreciation instance relative to the asset : {}, for the month : {}",asset,month);

        try {
            //double accrual = accruedDepreciationService.getAccruedDepreciationForAsset(asset,month) + depreciationAmount;
            accruedDepreciation
                    .setMonth(month)
                    .setCategory(asset.getCategory())
                    .setFixedAssetId(asset.getId())
                    .setCategory(asset.getCategory())
                    .setSolId(asset.getSolId())
                    .setAccruedDepreciation(accrual);
        } catch (Throwable e) {
            String message = String.format("Exception encountered while creating accruedDepreciation instance relative" +
                    " to the asset : %s for the month : %s",asset,month);
            throw new DepreciationExecutionException(message,e);
        }

        log.trace("AccruedDepreciation instance created : {}",accruedDepreciation);

        return accruedDepreciation;
    }

    /**
     * This method listens for message sent to a queue
     * containing the Object of type U and formulates appropriate
     * response
     *
     * @param updateMessage
     */
    @Override
    public void receive(Update<AccruedDepreciation> updateMessage) {
        // crickets
    }
}
