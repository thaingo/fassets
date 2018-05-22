/**
 *
 */
package io.github.fasset.fasset.accounts;

import java.util.List;

import com.google.common.collect.ImmutableList;
import io.github.fasset.fasset.accounts.depreciation.DepreciationAlgorithm;
import io.github.fasset.fasset.accounts.depreciation.DepreciationPeriod;
import io.github.fasset.fasset.book.keeper.unit.money.Cash;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.github.fasset.fasset.book.keeper.AccountingEntry;
import io.github.fasset.fasset.kernel.util.ImmutableListCollector;
import io.github.fasset.fasset.model.FixedAsset;

import static io.github.fasset.fasset.book.keeper.balance.AccountSide.DEBIT;
import static io.github.fasset.fasset.book.keeper.unit.time.SimpleDate.ofLocal;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * This resolver generates accounting entries relates to depreciation of a fixed asset where we generally credit the
 * contra account which is the accumulated depreciation account, and credit the respective depreciation account.
 *
 * @author edwin.njeru
 */
@Component("batchDepreciationEntryResolver")
public class DefaultDepreciationEntryResolver implements DepreciationEntryResolver {

    private static final Logger log = getLogger(DefaultDepreciationEntryResolver.class);

    private AccountResolver accountResolver;

    private DepreciationAlgorithm depreciationAlgorithm;

    @Autowired
    public DefaultDepreciationEntryResolver(@Qualifier("depreciationAccountResolver") AccountResolver accountResolver, DepreciationAlgorithm depreciationAlgorithm) {
        this.accountResolver = accountResolver;
        this.depreciationAlgorithm = depreciationAlgorithm;
    }

    /**
     * Generates {@code AccountingEntry} items based on {@code FixedAssets} items passed in the parameter.
     * The method will generate both {@code DEBIT} and {@code CREDIT} side entries and will abstract from
     * client the logic of obtaining depreciation rates and values from configurations in the application
     *
     * @param fixedAssets Items to be depreciated
     * @param period when the depreciation Entries are effective
     * @return {@code AccountingEntries} to post depreciation
     */
    public List<AccountingEntry> resolveEntries(List<FixedAsset> fixedAssets, DepreciationPeriod period) {

        log.debug("Resolving depreciation entries for {} fixedAssets", fixedAssets.size());

        List<AccountingEntry> entries = fixedAssets.stream().flatMap(asset -> getEntrySet(asset, period).stream()).collect(ImmutableListCollector.toImmutableFastList());

        log.debug("{} accounting entries generated", entries.size());

        return entries;
    }

    private List<AccountingEntry> getEntrySet(FixedAsset asset, DepreciationPeriod period) {

        return ImmutableList.of(debitEntry(asset, period), creditEntry(asset, period));
    }

    private AccountingEntry debitEntry(FixedAsset asset, DepreciationPeriod period) {

        AccountingEntry debit =
            new AccountingEntry(
                ofLocal(asset.getPurchaseDate()),
                accountResolver.resolveDebitAccount(asset),
                narration(asset), DEBIT, depreciation(asset, period));

        updateAttributes(debit, asset);

        return debit;
    }

    private void updateAttributes(AccountingEntry debit, FixedAsset asset) {

        debit.addAttribute("DEPRECIATION_ALGORITHM", depreciationAlgorithm.name());
    }

    private Cash depreciation(FixedAsset asset, DepreciationPeriod period) {

        return depreciationAlgorithm.getDepreciation(asset, period);
    }

    private String narration(FixedAsset asset) {

        return String.format("depreciation for %s", asset.getAssetDescription());
    }

    private AccountingEntry creditEntry(FixedAsset asset,  DepreciationPeriod period) {
        AccountingEntry credit = new AccountingEntry();
        return credit;
    }

}
