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

package io.github.fasset.fasset.book.keeper.util;


/**
 * This exception is thrown when the {@link io.github.fasset.fasset.book.Transaction} client
 * tries to post a transaction when the sum of the {@link io.github.fasset.fasset.book.keeper.Entry} items does not
 * evaluate to zero, or when the caller of {@link io.github.fasset.fasset.book.Transaction#post()}
 * does not first ensure that {@link io.github.fasset.fasset.book.keeper.unit.money.Cash} amounts of
 * {@link io.github.fasset.fasset.book.keeper.balance.AccountSide#CREDIT}
 * {@link io.github.fasset.fasset.book.keeper.Entry} items
 * are not equivalent to the {@link io.github.fasset.fasset.book.keeper.unit.money.Cash} amounts of
 * {@link io.github.fasset.fasset.book.keeper.balance.AccountSide#CREDIT}
 * {@link io.github.fasset.fasset.book.keeper.Entry} items
 *
 * @author edwin.njeru
 */
public class UnableToPostException extends Exception {

    private static final long serialVersionUID = 2585148563615187597L;

    /**
     * Constructs a new throwable with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     * The {@link #fillInStackTrace()} method is called to initialize
     * the stack trace data in the newly created throwable.
     */
    public UnableToPostException() {
        super();
    }

    /**
     * Constructs a new throwable with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     * The {@link #fillInStackTrace()} method is called to initialize
     * the stack trace data in the newly created throwable.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public UnableToPostException(String message) {
        super(message);
    }
}
