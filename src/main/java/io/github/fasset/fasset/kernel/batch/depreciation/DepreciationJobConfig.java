package io.github.fasset.fasset.kernel.batch.depreciation;

import io.github.fasset.fasset.kernel.batch.upload.BatchNotifications;
import io.github.fasset.fasset.model.AccruedDepreciation;
import io.github.fasset.fasset.model.Depreciation;
import io.github.fasset.fasset.model.FixedAsset;
import io.github.fasset.fasset.model.FixedAssetDTO;
import io.github.fasset.fasset.model.NetBookValue;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DepreciationJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ItemReader<FixedAsset> fixedAssetItemReader;

    @Bean("depreciationJob")
    public Job importExcelJob(DepreciationJobListener depreciationJobListener) {
        return jobBuilderFactory.get("depreciationJob")
                .incrementer(new RunIdIncrementer())
                .listener(depreciationJobListener)
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    public DepreciationProcessor depreciationProcessor(){

        return new DepreciationProcessor();
    }

    @Bean
    public DepreciationWriter depreciationWriter(){

        return new DepreciationWriter();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<FixedAsset, List<Depreciation>> chunk(100)
                .reader(fixedAssetItemReader)
                .processor(depreciationProcessor())
                .writer(depreciationWriter())
                .build();
    }


}