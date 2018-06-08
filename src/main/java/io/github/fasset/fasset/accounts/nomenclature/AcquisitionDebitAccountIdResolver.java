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
package io.github.fasset.fasset.accounts.nomenclature;

import io.github.fasset.fasset.accounts.nomenclature.properties.AccountIdService;
import io.github.fasset.fasset.model.FixedAsset;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Resolves names of the accounts for posting acquisitions
 */
@Component("debitAccountIDResolver")
public class AcquisitionDebitAccountIdResolver extends AbstractAccountIdResolver implements AccountIdResolver {

    private static final Logger log = getLogger(AcquisitionDebitAccountIdResolver.class);

    @Autowired
    public AcquisitionDebitAccountIdResolver(@Qualifier("accountIdConfigurationPropertiesService") AccountIdService idConfigurationService) {
        super(idConfigurationService);
    }

    /**
     * <p>Returns the account number with respect to a fixed asset which is formed by concatenating 4 elements together</p>
     * <p>1. Service Outlet Id</p>
     * <p>2. Currency</p>
     * <p>3. General Ledger</p>
     * <p>4. Account's unique ID #</p>
     *
     * @param fixedAsset Item for which we need an account number
     * @return The account number to track the fixed asset as String
     */
    @Override
    public String accountNumber(FixedAsset fixedAsset) {

        log.debug("Resolving account # for fixed asset : {}", fixedAsset.getAssetDescription());

        checkNotNull(fixedAsset.getCategory(), String.format("The category for the fixed asset : %s is null", fixedAsset.getAssetDescription()));
        checkNotNull(fixedAsset.getPurchaseCost(), String.format("The Cost for the fixed asset : %s is null", fixedAsset.getAssetDescription()));

        return fixedAsset.getSolId() + currencyCode(fixedAsset) + glCode(fixedAsset) + glId(fixedAsset);
    }

    @Override
    public String accountName(FixedAsset fixedAsset) {

        checkNotNull(fixedAsset.getCategory(), "Sorry mate, but REALLY need that category specified");

        return fixedAsset.getCategory().toUpperCase();
    }

    private String glId(FixedAsset fixedAsset) {

        log.debug("Resolving general ledger nomenclature for fixedAsset : {}", fixedAsset.getAssetDescription());

        return idConfigurationService.debitAccountPlaceHolder(fixedAsset.getCategory());
    }

    private String glCode(FixedAsset fixedAsset) {

        log.debug("Resolving general ledger code # for fixedAsset : {}", fixedAsset.getAssetDescription());

        return idConfigurationService.debitGeneralLedgerCode(fixedAsset.getCategory());
    }

    /**
     * Resolves the name of the appropriate general ledger that out to be used in this case for the
     * fixed assets. The general ledger is taken to be one hierarchy higher than an account. This is
     * being done by the accountIDResolver as the general-ledger nomenclature has something to do with the nomenclature
     * of the account itself
     *
     * @param fixedAsset For which we need a general ledger
     * @return The ID of the general ledger
     */
    @Override
    public String generalLedgerName(FixedAsset fixedAsset) {

        checkNotNull(fixedAsset.getCategory(), "Sorry mate, but REALLY need that category specified");

        return fixedAsset.getCategory().toUpperCase();
    }

    /**
     * Resolve the name of a Contra account for a main account used for tracking the asset
     *
     * @param fixedAsset The asset for which we seek an account to track financially
     * @return The name of the contra account
     */
    @Override
    public String resolveContraAccountId(FixedAsset fixedAsset) {

        checkNotNull(fixedAsset.getCategory(), "Sorry mate, but REALLY need that category specified");

        return String.format("Accumulated Depreciation on %s", fixedAsset.getCategory()).toUpperCase();
    }
}