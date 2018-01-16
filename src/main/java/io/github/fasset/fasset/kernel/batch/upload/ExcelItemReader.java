package io.github.fasset.fasset.kernel.batch.upload;

import io.github.fasset.fasset.kernel.excel.ExcelMapperService;
import io.github.fasset.fasset.model.FixedAssetDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.List;


/**
 * Reads data from a list generated by an excel row mapper
 *
 * @author edwin.njeru
 */
@Scope("job")
public class ExcelItemReader implements ItemReader<FixedAssetDTO> {

    private static final Logger log = LoggerFactory.getLogger(ExcelItemReader.class);

    private int nextItem;
    private List<FixedAssetDTO> fixedAssetDTOS;

    private String fileName;

    @Autowired
    @Qualifier("excelMapperService")
    private ExcelMapperService excelMapperService;

    public ExcelItemReader(String fileName, ExcelMapperService excelMapperService) {
        this.fileName = fileName;
        this.excelMapperService = excelMapperService;
    }

    // To be called before and after job
    public void resetNextItem(){
        log.debug("The nextItem index = {}, is being reset to 0", nextItem);

        nextItem = 0;
    }

    @PostConstruct
    public void init(){
        resetNextItem();
        fixedAssetDTOS = excelMapperService.fetchExcelData(fileName);
    }

    @Override
    public FixedAssetDTO read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        if(!fixedAssetDTOS.isEmpty()){

            while (nextItem < fixedAssetDTOS.size()){

                log.debug("Fetching items # {}",nextItem);

                return fixedAssetDTOS.get(nextItem++);
            }
        }

        log.debug("Collection is empty returning null");
        return null;
    }
}
