package io.github.fasset.fasset.kernel.batch;

import io.github.fasset.fasset.kernel.notifications.FileUploadNotification;
import io.github.fasset.fasset.kernel.subscriptions.AbstractSubscriber;
import io.github.fasset.fasset.kernel.subscriptions.Subscriber;
import io.github.fasset.fasset.kernel.subscriptions.Update;
import io.github.fasset.fasset.kernel.util.BatchJobExecutionException;
import org.slf4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.time.LocalDateTime;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * This object bootstraps an excel upload job after the file has been uploaded to the server
 */
@Component("excelUploadJob")
public class ExcelUploadJob extends AbstractSubscriber implements Subscriber{

    private final static Logger log = getLogger(ExcelUploadJob.class);

    private final JobLauncher jobLauncher;

    private final Job importExcelJob;

    @Autowired
    public ExcelUploadJob(JobLauncher jobLauncher, @Qualifier("importExcelJob") Job importExcelJob) {
        super(ExcelUploadJob.class.getName());
        this.jobLauncher = jobLauncher;
        this.importExcelJob = importExcelJob;
    }

    /**
     * Systematically processes connect that can be extracted from an excel file provided as argument
     *
     * @param filePath from which we are reading the business domain connect
     */
    public void uploadExcelFile(String filePath,String month) throws BatchJobExecutionException {

        log.info("Uploading excel file on the path : {}", filePath);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("fileName",filePath)
                .addString("month",month)
                .addString("time", LocalDateTime.now().toString())
                .toJobParameters();

        try {
            jobLauncher.run(importExcelJob,jobParameters);
        } catch (Throwable e) {

            String message = String.format("Exception encountered %s caused by %s,while launching job" +
                            "id %s, reading from excel file : %s at time %s",
                    e.getMessage(),e.getCause(),importExcelJob.getName(),jobParameters.getString("fileName"),jobParameters.getString("time"));

            throw new BatchJobExecutionException(message,e);

        }

    }

    /**
     * Listens for messages from the queue
     * @param message
     * @throws JMSException
     */
    //@JmsListener(destination = "fileUploads", containerFactory = "messageFactory")
    public void listenForMessages(FileUploadNotification message){

        String fileName = message.getFileName();
        String month = message.getMonth();

        log.debug("File : {} has been received on the server side and is about to be actioned",fileName);

        try {
            uploadExcelFile(fileName,month);
        } catch (BatchJobExecutionException e) {
            log.error("Exception encountered while uploading excel file from : {}",fileName);
        }
    }

    @Override
    protected void consumeUpdate(Update update) {

        FileUploadNotification message = (FileUploadNotification) update.getPayload();

        log.debug("File upload : {} has been received by the excelUploadJob for processing",message);

        listenForMessages(message);
    }
}
