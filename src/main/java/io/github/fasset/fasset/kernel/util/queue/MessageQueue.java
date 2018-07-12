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
package io.github.fasset.fasset.kernel.util.queue;

import io.github.fasset.fasset.kernel.util.queue.util.OnCompletion;
import io.github.fasset.fasset.kernel.util.queue.util.OnError;

/**
 * This interface represents a way for sending messages in the application.
 * <br> Please note it is expected that the queue is expected to operate through use
 * of database persisted strings
 * <br> The class primarily uses the push method to add a typed message. For instance, for the
 * below implementation :
 * <br> {@code public void push(QueueMessage<FileUpload> queueMessage) }
 * <br> A client could add a file upload message as shown:
 * <p>
 * <br> <pre>
 *     {@code
 *     QueueMessage<FileUpload> fileUploadsQueue = new FileUploadsQueue(fileUploadService);
 * <p>
 *     fileUploadsQueue.push(() -> new FileUploadNotification(fileUpload.getFileName());
 *     }
 *     </pre>
 * <p>
 * <br>
 * <br> Should the client need to carry out an action to show completion lifecycle method,
 * the client could call the {@link OnCompletion#handleCompletion()} method as shown:
 * <br>
 * <br> <pre>
 *     {@code
 *     fileUploadsQueue.push(() -> new FileUploadNotification(fileUpload.getFileName()),
 *      () -> log.debug("The file {} has been uploaded", fileUpload.getFileName()));
 *     }
 *     </pre>
 * <p>
 * TODO : implement consumers for this Queue
 */
public interface MessageQueue<T> {

    /**
     * Adds a message to the queue
     *
     * @param queueMessage Item to be added to the queue
     */
    public abstract void push(QueueMessage<T> queueMessage);

    /**
     * Adds a message to the queue, and provides a method to allow the producer to handle error
     *
     * @param queueMessage Item to be added to the queue
     */
    void push(QueueMessage<T> queueMessage, OnError onError);

    /**
     * Adds a message to the queue and provides a method to allow the producer to handle 'task completion acts'
     *
     * @param queueMessage Item to be added to the queue
     * @param onCompletion This is a functional interface for handling task completion tasks
     */
    void push(QueueMessage<T> queueMessage, OnCompletion onCompletion);

    /**
     * Adds a message to the queue, and provides a method to allow the producer to handle error
     *
     * @param queueMessage Item to be added to the queue
     * @param onError      This is a functional interface for handling errors when they occur
     * @param onCompletion This is a functional interface for handling task completion tasks
     */
    void push(QueueMessage<T> queueMessage, OnError onError, OnCompletion onCompletion);
}
