/*
 *     This file is part of fassets
 *     Copyright (C) 2018 Edwin Njeru
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.fasset.fasset.kernel.batch.upload;

import io.github.fasset.fasset.kernel.excel.ExcelMapperService;
import io.github.fasset.fasset.model.AccruedDepreciation;
import io.github.fasset.fasset.model.FixedAsset;
import io.github.fasset.fasset.model.FixedAssetDTO;
import io.github.fasset.fasset.model.NetBookValue;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Configuration;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManagerFactory;

@Configuration
public class FileUploadBatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("excelMapperService")
    private ExcelMapperService excelMapperService;

    @Autowired
    @Qualifier("excelItemProcessor")
    private ExcelItemProcessor excelItemProcessor;

    @Autowired
    @Qualifier("excelItemWriter")
    private ExcelItemWriter excelItemWriter;


    @Value("#{jobParameters['fileName']}")
    public static final String FILE_PATH = null;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    @Qualifier("fixedAssetAccruedDepreciationProcessor")
    private FixedAssetAccruedDepreciationProcessor fixedAssetAccruedDepreciationProcessor;

    @Autowired
    @Qualifier("accruedDepreciationWriter")
    private AccruedDepreciationWriter accruedDepreciationWriter;

    @Autowired
    @Qualifier("netBookValueWriter")
    private NetBookValueWriter netBookValueWriter;

    @Autowired
    @Qualifier("fixedAssetNetBookValueProcessor")
    private FixedAssetNetBookValueProcessor fixedAssetNetBookValueProcessor;


    @Bean
    @JobScope
    public ExcelItemReader excelItemReader(@Value("#{jobParameters['fileName']}")String filePath){

        return new ExcelItemReader(filePath,excelMapperService);
    }

    @Bean
    public ItemReader<FixedAsset> fixedAssetItemReader() throws Exception {

        JpaPagingItemReader<FixedAsset> dataBaseReader = new JpaPagingItemReader<>();
        dataBaseReader.setEntityManagerFactory(entityManagerFactory);

        dataBaseReader.setQueryString("SELECT a FROM FixedAsset a");

        dataBaseReader.setTransacted(true);
        dataBaseReader.setPageSize(100);
        dataBaseReader.setSaveState(true);
        dataBaseReader.afterPropertiesSet();

        return dataBaseReader;
    }

    @Bean("importExcelJob")
    public Job importExcelJob(BatchNotifications listener) {
        return jobBuilderFactory.get("importExcelJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(readExcelFileStep()).on("COMPLETED")
                .to(accrueDepreciationStep()).on("COMPLETED")
                .to(netBookValueUpdateStep())
                .end()
                .build();
    }

    @Bean
    public Step readExcelFileStep() {
        return stepBuilderFactory.get("readExcelFileStep")
                .<FixedAssetDTO, FixedAsset> chunk(100)
                .reader(excelItemReader(FILE_PATH))
                .processor(excelItemProcessor)
                .writer(excelItemWriter)
                .build();
    }

    @Bean
    public Step accrueDepreciationStep() {

        Step step2 = null;

        try {
            step2 = stepBuilderFactory.get("accrueDepreciationStep")
                    .<FixedAsset,AccruedDepreciation> chunk(100)
                    .reader(fixedAssetItemReader())
                    .processor(fixedAssetAccruedDepreciationProcessor)
                    .writer(accruedDepreciationWriter)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return step2;
    }

    @Bean
    public Step netBookValueUpdateStep() {

        Step step3 = null;

        try {
            step3 = stepBuilderFactory.get("netBookValueUpdateStep")
                    .<FixedAsset, NetBookValue> chunk(100)
                    .reader(fixedAssetItemReader())
                    .processor(fixedAssetNetBookValueProcessor)
                    .writer(netBookValueWriter)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return step3;
    }
}
