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
package io.github.fasset.fasset.kernel.util.queue.files;

import io.github.fasset.fasset.kernel.batch.ExcelUploadJob;
import io.github.fasset.fasset.kernel.util.BatchJobExecutionException;
import io.github.fasset.fasset.kernel.util.ConcurrentList;
import io.github.fasset.fasset.kernel.util.ImmutableListCollector;
import io.github.fasset.fasset.kernel.util.queue.MQException;
import io.github.fasset.fasset.kernel.util.queue.MessageConsumer;
import io.github.fasset.fasset.kernel.util.queue.QueueMessage;
import io.github.fasset.fasset.model.files.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * This is a utility for checking if files have been uploaded, and if so, trigger actions using the appropriate file uploads batch configuration
 *
 * @author edwin.njeru
 * @version $Id: $Id
 */
@Component("fileUploadsChecker")
public class FileUploadsChecker implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(FileUploadsChecker.class);

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private final MessageConsumer<List<FileUpload>> fileUploadsConsumer;
    private final ExcelUploadJob excelUploadJob;


    //Todo Import and add ExcelUploadJob to execution

    /**
     * <p>Constructor for FileUploadsChecker.</p>
     *
     * @param fileUploadsConsumer a {@link io.github.fasset.fasset.kernel.util.queue.MessageConsumer} object.
     * @param excelUploadJob      a {@link io.github.fasset.fasset.kernel.batch.ExcelUploadJob} object.
     */
    @Autowired
    public FileUploadsChecker(MessageConsumer<List<FileUpload>> fileUploadsConsumer, @Qualifier("excelUploadJob") ExcelUploadJob excelUploadJob) {
        this.fileUploadsConsumer = fileUploadsConsumer;
        this.excelUploadJob = excelUploadJob;
    }

    private static void handleError(MQException e) {
        throw new FileMessageReadingException(e);
    }

    private static void handleCompletion() {
        log.trace("A file has been inquired successfully from the system");
    }

    /**
     * {@inheritDoc}
     * <p>
     * When an object implementing interface <code>Runnable</code> is used to create a thread, starting the thread causes the object's <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Scheduled(fixedRate = 5000)
    @Override
    public void run() {

        log.trace("Checking for new files in the system...");

        List<FileUpload> fileUploads = ConcurrentList.newList();

        lock.writeLock().lock();

        try {
            fileUploadsConsumer.checkMessages(FileUploadsChecker::handleError, FileUploadsChecker::handleCompletion)
                               .subscribe((Optional<QueueMessage<List<FileUpload>>> f) -> fileUploads.addAll(f.get().message().stream().peek(fileUpload -> {
                                                                                                                 fileUpload.setDeserialized(true);
                                                                                                             }).collect(ImmutableListCollector.toImmutableFastList())

                                                                                                            ));
        } finally {

            lock.writeLock().unlock();

        }

        fileUploads.forEach(f -> {
            try {
                excelUploadJob.uploadExcelFile(f.getFileName(), f.getMonth().toString());
            } catch (BatchJobExecutionException e) {
                e.printStackTrace();
            }
        });

    }

}
