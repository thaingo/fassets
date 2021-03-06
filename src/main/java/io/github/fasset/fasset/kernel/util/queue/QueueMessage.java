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

import io.github.fasset.fasset.kernel.util.queue.files.FileUploadService;

/**
 * Message item being added to the queue. The architecture of this queueing framework assumes the message enqueued is dumb as to whether or not it has been processed. The client has to form its own
 * way of 'knowing' whether or not a message has been consumed. SO we take the dumb-framework-clever-consumers approach. <br> The specifics of what's in the message are expected to be set up by
 * 'producers' through functional implementation such as shown below:
 * <br> <pre>{@code fileUploadsQueue.push(() -> new FileUploadNotification(fileUpload.getFileName())}</pre>
 * <br> <br> In the above example the {@code FIleUploadQueue} is an implementation of the {@code MessageQueue<FileUpload>} interface that is supposed to enqueue messages about an uploaded file. The
 * message here is therefore of the type {@code FileUploadNotification}, which has details of a file that the system needs to know about. <br> Using the lambda will allow the producer to create and
 * simultaneously enqueue an object of any type. <br> The 'consumer' on the other hand has only to call the {@link io.github.fasset.fasset.kernel.util.queue.QueueMessage#message()} method to obtain
 * the message sent by the producer. <br> The consumer for the {@link io.github.fasset.fasset.kernel.util.queue.files.FileUploadService} consumption of the message might look as follows:
 * <br> <pre>{@code fileUploadService.recordFileUpload(queueMessage.message());}</pre>
 *
 * @author edwin.njeru
 * @version $Id: $Id
 */
@FunctionalInterface
public interface QueueMessage<T> {

    /**
     * This is the item being enqueued
     *
     * @return The message from the queue
     */
    T message();
}
