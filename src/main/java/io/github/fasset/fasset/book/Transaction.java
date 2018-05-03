/*
 *  Copyright 2018 Edwin Njeru
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.github.fasset.fasset.book;

import io.github.fasset.fasset.book.keeper.Account;
import io.github.fasset.fasset.book.keeper.Entry;
import io.github.fasset.fasset.book.keeper.EntryAttribute;
import io.github.fasset.fasset.book.keeper.balance.AccountSide;
import io.github.fasset.fasset.book.keeper.unit.money.Cash;
import io.github.fasset.fasset.book.keeper.util.ImmutableEntryException;
import io.github.fasset.fasset.book.keeper.util.MismatchedCurrencyException;
import io.github.fasset.fasset.book.keeper.util.UnableToPostException;

import java.util.Map;
import java.util.Set;

/**
 * A collection of {@link Entry} items being posted at the same time
 */
public interface Transaction {

    /**
     * @param accountSide     {@link AccountSide} in which the entry is for
     * @param amount          Monetary value of the entry
     * @param account         {@link Account} into which the {@link Entry} is posted
     * @param narration       {@link Entry} specifications of the {@link Entry}
     * @param entryAttributes Map containing additional info about the entry
     */
    public void addEntry(AccountSide accountSide, Cash amount, Account account, String narration, Map<EntryAttribute, String> entryAttributes)
        throws ImmutableEntryException, MismatchedCurrencyException;

    /**
     * Same method as {code Transaction.addEntry()} but with an empty map as description of the
     * entry. The {@code Entry} can therefore only be distinguished from its narration.
     * The add method adds entries to the transaction provided the transaction has not already
     * been posted
     *
     * @param accountSide to which the entry is being posted
     * @param amount      {@link Cash} amount being posted to the journal
     * @param account     {@link Account} into which the {@link Entry} is being added
     * @param narration   a brief narration of the entry
     */
    public void addEntry(AccountSide accountSide, Cash amount, Account account, String narration) throws ImmutableEntryException, MismatchedCurrencyException;

    /**
     * Adds the {@link Entry} items into the accounts involved in this {@link Transaction}
     */
    void post() throws UnableToPostException, ImmutableEntryException;

    /**
     * @return Collection of {@link Entry} items in the Transaction
     */
    Set<Entry> getEntries();
}