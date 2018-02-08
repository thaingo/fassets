package io.github.fasset.fasset.kernel.batch.depreciation.report.sol;

import io.github.fasset.fasset.model.depreciation.MonthlySolDepreciation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class MonthlySolDepreciationProcessor implements ItemProcessor<String,MonthlySolDepreciation> {

    private static final Logger log = LoggerFactory.getLogger(MonthlySolDepreciationProcessor.class);

    private String year;

    private final MonthlySolDepreciationExecutor monthlySolDepreciationExecutor;

    @Autowired
    public MonthlySolDepreciationProcessor(@Qualifier("monthylSolDepreciationExecutor") MonthlySolDepreciationExecutor monthlySolDepreciationExecutor,String year) {
        this.year=year;
        this.monthlySolDepreciationExecutor = monthlySolDepreciationExecutor;
    }

    /**
     * Process the provided item, returning a potentially modified or new item for continued
     * processing.  If the returned result is null, it is assumed that processing of the item
     * should not continue.
     *
     * @param item to be processed
     * @return potentially modified or new item for continued processing, null if processing of the
     * provided item should not continue.
     * @throws Exception thrown if exception occurs during processing.
     */
    @Override
    public MonthlySolDepreciation process(String item) throws Exception {

        if(year==null){

            log.warn("The year value passed is null : {}",year);
        }

        return monthlySolDepreciationExecutor.getMonthlyDepreciation(item,Integer.parseInt(year));
    }
}