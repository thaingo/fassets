package io.github.fasset.fasset.kernel.batch.depreciation.report.asset;

import io.github.fasset.fasset.kernel.batch.depreciation.report.asset.MonthlyAssetDepreciationExecutor;
import io.github.fasset.fasset.kernel.batch.depreciation.report.asset.MonthlyAssetDepreciationJobListener;
import io.github.fasset.fasset.kernel.batch.depreciation.report.asset.MonthlyAssetDepreciationProcessor;
import io.github.fasset.fasset.kernel.batch.depreciation.report.asset.MonthlyAssetDepreciationWriter;
import io.github.fasset.fasset.kernel.batch.depreciation.report.sol.MonthlySolDepreciationExecutor;
import io.github.fasset.fasset.kernel.batch.depreciation.report.sol.MonthlySolDepreciationProcessor;
import io.github.fasset.fasset.kernel.batch.depreciation.report.sol.MonthlySolDepreciationWriter;
import io.github.fasset.fasset.model.FixedAsset;
import io.github.fasset.fasset.model.depreciation.MonthlyAssetDepreciation;
import io.github.fasset.fasset.model.depreciation.MonthlySolDepreciation;
import io.github.fasset.fasset.service.MonthlyAssetDepreciationService;
import io.github.fasset.fasset.service.MonthlySolDepreciationService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.persistence.EntityManagerFactory;

@Configuration
public class MonthlyAssetDepreciationJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;



    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("monthlyAssetDepreciationService")
    MonthlyAssetDepreciationService monthlyAssetDepreciationService;

    @Autowired
    private ItemReader<FixedAsset> fixedAssetItemReader;

    @Value("#{jobParameters['year']}")
    public static final String YEAR = null;

    @Autowired
    @Qualifier("monthlyAssetDepreciationExecutor")
    private MonthlyAssetDepreciationExecutor monthlyAssetDepreciationExecutor;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private MonthlySolDepreciationService monthlySolDepreciationService;
    @Autowired
    private MonthlySolDepreciationExecutor monthlySolDepreciationExecutor;

    @Autowired
    private ItemReader monthlySolDepreciationReader;

    @Autowired
    public MonthlyAssetDepreciationJobConfiguration(JobBuilderFactory jobBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
    }


    @Bean("monthlyAssetDepreciationJobListener")
    public MonthlyAssetDepreciationJobListener monthlyAssetDepreciationJobListener(){

        return new MonthlyAssetDepreciationJobListener();
    }

    /*@Bean("monthlyAssetDepreciationJob")
    @DependsOn("monthlyAssetDepreciationJobListener")
    public Job monthlyAssetDepreciationJob() {
        return jobBuilderFactory.get("monthlyAssetDepreciationJob")
                .incrementer(new RunIdIncrementer())
                .listener(monthlyAssetDepreciationJobListener())
                .start(updateMonthlyAssetDepreciation())
                .next(createMonthlySolDepreciationItems())
                .next(createMonthlyCategoryDepreciationItems())
                .build();
    }*/

    @Bean("monthlyAssetDepreciationJob")
    @DependsOn("monthlyAssetDepreciationJobListener")
    public Job monthlyAssetDepreciationJob() {
        return jobBuilderFactory.get("monthlyAssetDepreciationJob")
                .incrementer(new RunIdIncrementer())
                .listener(monthlyAssetDepreciationJobListener())
                .preventRestart()
                .flow(updateMonthlyAssetDepreciation())
                .end()
                .build();
    }

    @Bean
    @JobScope
    public MonthlyAssetDepreciationProcessor monthlyAssetDepreciationProcessor(@Value("#{jobParameters['year']}") String year){

        return new MonthlyAssetDepreciationProcessor(monthlyAssetDepreciationExecutor,year);
    }

    @Bean
    public MonthlyAssetDepreciationWriter monthlyAssetDepreciationWriter(){

        return new MonthlyAssetDepreciationWriter(monthlyAssetDepreciationService);
    }

    @Bean
    public Step updateMonthlyAssetDepreciation() {
        Step updateMonthlyAssetDepreciation = null;
        try {
            updateMonthlyAssetDepreciation =  stepBuilderFactory.get("updateMonthlyAssetDepreciation")
                    .<FixedAsset, MonthlyAssetDepreciation> chunk(100)
                    .reader(fixedAssetItemReader)
                    .processor(monthlyAssetDepreciationProcessor(YEAR))
                    .writer(monthlyAssetDepreciationWriter())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return updateMonthlyAssetDepreciation;
    }

    @Bean
    public MonthlySolDepreciationWriter monthlySolDepreciationWriter(){

        return new MonthlySolDepreciationWriter(monthlySolDepreciationService);
    }

    @Bean
    @JobScope
    public MonthlySolDepreciationProcessor monthlySolDepreciationProcessor(@Value("#{jobParameters['year']}") String year){

        return new MonthlySolDepreciationProcessor(monthlySolDepreciationExecutor,year);
    }


    /*@Bean("createMonthlySolDepreciationItems")
    public Step createMonthlySolDepreciationItems() {

        Step createMonthlySolDepreciationItems = null;

        try {
            createMonthlySolDepreciationItems = stepBuilderFactory
                    .get("createMonthlySolDepreciationItems")
                    .<String,MonthlySolDepreciation>chunk(5)
                    .reader(monthlySolDepreciationReader)
                    .writer(monthlySolDepreciationWriter())
                    .processor(monthlySolDepreciationProcessor(YEAR))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return createMonthlySolDepreciationItems;
    }*/

    /*@Bean
    public Step updateMonthlyAssetDepreciation() {
        Step updateMonthlyAssetDepreciation = null;
        try {
            updateMonthlyAssetDepreciation =  stepBuilderFactory.getCategoryConfiguration("updateMonthlyAssetDepreciation")
                    .<Depreciation, MonthlyAssetDepreciation> chunk(100)
                    .reader(depreciationItemReader())
                    .processor(monthlyAssetDepreciationProcessor())
                    .writer(monthlyAssetDepreciationWriter())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return updateMonthlyAssetDepreciation;
    }*/
}