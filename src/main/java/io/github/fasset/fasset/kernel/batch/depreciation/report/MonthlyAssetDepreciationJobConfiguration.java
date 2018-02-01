package io.github.fasset.fasset.kernel.batch.depreciation.report;

import io.github.fasset.fasset.kernel.batch.upload.BatchNotifications;
import io.github.fasset.fasset.model.Depreciation;
import io.github.fasset.fasset.model.FixedAsset;
import io.github.fasset.fasset.model.depreciation.MonthlyAssetDepreciation;
import io.github.fasset.fasset.service.MonthlyAssetDepreciationService;
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
import java.util.List;

@Configuration
public class MonthlyAssetDepreciationJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("monthlyAssetDepreciationService")
    MonthlyAssetDepreciationService monthlyAssetDepreciationService;

    @Autowired
    private ItemReader<FixedAsset> fixedAssetItemReader;

    @Value("#{jobParameters['year']}")
    public static final String year = null;

    @Autowired
    @Qualifier("monthlyAssetDepreciationExecutor")
    private MonthlyAssetDepreciationExecutor monthlyAssetDepreciationExecutor;

    @Autowired
    public MonthlyAssetDepreciationJobConfiguration(JobBuilderFactory jobBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
    }

    /*@Bean
    public ItemReader<Depreciation> depreciationItemReader() throws Exception {
        JpaPagingItemReader<Depreciation> dataBaseReader = new JpaPagingItemReader<>();
        dataBaseReader.setEntityManagerFactory(entityManagerFactory);

        dataBaseReader.setQueryString("SELECT a FROM Depreciation a");

        dataBaseReader.setTransacted(true);
        dataBaseReader.setPageSize(100);
        dataBaseReader.setSaveState(true);
        dataBaseReader.afterPropertiesSet();

        return dataBaseReader;

    }*/

    @Bean("monthlyAssetDepreciationJobListener")
    public MonthlyAssetDepreciationJobListener monthlyAssetDepreciationJobListener(){

        return new MonthlyAssetDepreciationJobListener();
    }

    @Bean("monthlyAssetDepreciation")
    @DependsOn("monthlyAssetDepreciationJobListener")
    public Job monthlyAssetDepreciation() {
        return jobBuilderFactory.get("monthlyAssetDepreciation")
                .incrementer(new RunIdIncrementer())
                .listener(monthlyAssetDepreciationJobListener())
                .flow(updateMonthlyAssetDepreciation())
                .end()
                .build();
    }

    @Bean
    @JobScope
    public MonthlyAssetDepreciationProcessor monthlyAssetDepreciationProcessor(){

        return new MonthlyAssetDepreciationProcessor(monthlyAssetDepreciationExecutor,year);
    }

    @Bean
    public MonthlyAssetDepreciationWriter monthlyAssetDepreciationWriter(){

        return new MonthlyAssetDepreciationWriter(monthlyAssetDepreciationService);
    }

    @Bean
    public QueryMonthlyDepreciationProcessor queryMonthlyDepreciationProcessor(){

        return new QueryMonthlyDepreciationProcessor();
    }

    @Bean
    public Step updateMonthlyAssetDepreciation() {
        Step updateMonthlyAssetDepreciation = null;
        try {
            updateMonthlyAssetDepreciation =  stepBuilderFactory.get("updateMonthlyAssetDepreciation")
                    .<FixedAsset, MonthlyAssetDepreciation> chunk(100)
                    .reader(fixedAssetItemReader)
                    .processor(monthlyAssetDepreciationProcessor())
                    .writer(monthlyAssetDepreciationWriter())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return updateMonthlyAssetDepreciation;
    }

    /*@Bean
    public Step updateMonthlyAssetDepreciation() {
        Step updateMonthlyAssetDepreciation = null;
        try {
            updateMonthlyAssetDepreciation =  stepBuilderFactory.get("updateMonthlyAssetDepreciation")
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
