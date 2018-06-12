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
package io.github.fasset.fasset.accounts.nomenclature;

import io.github.fasset.fasset.accounts.nomenclature.properties.AccountIdService;
import io.github.fasset.fasset.accounts.nomenclature.properties.FileAccountIdService;
import io.github.fasset.fasset.accounts.nomenclature.properties.policy.AccountIdPolicyVersion1;
import io.github.fasset.fasset.model.FixedAsset;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static io.github.fasset.fasset.accounts.definition.TransactionType.ACQUISITION;
import static io.github.fasset.fasset.book.keeper.balance.AccountSide.DEBIT;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class AcquisitionDebitAccountIdResolverTest {

    private AccountIdResolver accountIdResolver;

    private final static FixedAsset radio = new FixedAsset("Radio", Money.of(200,"KES"), "ELECTRONIC EQUIPMENT", "001", LocalDate.of(2018,2,5), "abc01", Money.of(9.5,"KES"));
    private final static FixedAsset lenovo = new FixedAsset("Lenovo IDP110", Money.of(5600,"KES"), "COMPUTERS", "987",LocalDate.of(2018,2,13), "abc02", Money.of(13.42,"KES"));
    private final static FixedAsset chair = new FixedAsset("Chair", Money.of(156,"KES"), "FURNITURE & FITTINGS", "010",LocalDate.of(2018,1,13),"abc03", Money.of(19.24,"KES"));
    private final static FixedAsset zemana = new FixedAsset("Zemana Antilogger", Money.of(180,"KES"), "COMPUTER SOFTWARE", "986",LocalDate.of(2017,12,15),"abc48", Money.of(50.32,"KES"));
    private final static FixedAsset kca = new FixedAsset("KCA 265G", Money.of(950,"KES"), "MOTOR VEHICLES", "996",LocalDate.of(2018,1,5),"abc23", Money.of(500.12,"KES"));
    private final static FixedAsset officePartitioning = new FixedAsset("Office Partitioning Works", Money.of(1500,"KES"), "OFFICE RENOVATION", "978",LocalDate.of(2018,1,12),"abc56", Money.of(50.13,
        "KES"));


    @Before
    public void setUp() throws Exception {

        AccountIdService accountIdService = Mockito.mock(AccountIdService.class);

        when(accountIdService.accountName(ACQUISITION, DEBIT, radio)).thenReturn("ELECTRONICS");
        when(accountIdService.accountName(ACQUISITION, DEBIT, lenovo)).thenReturn("COMPUTERS");
        when(accountIdService.accountName(ACQUISITION, DEBIT, chair)).thenReturn("FURNITURE");
        when(accountIdService.accountName(ACQUISITION, DEBIT, zemana)).thenReturn("COMPUTER SOFTWARE");
        when(accountIdService.accountName(ACQUISITION, DEBIT, kca)).thenReturn("MOTOR VEHICLES");
        when(accountIdService.accountName(ACQUISITION, DEBIT, officePartitioning)).thenReturn("OFFICE RENOVATION");

        when(accountIdService.accountPlaceHolder(ACQUISITION, DEBIT, radio)).thenReturn("010");
        when(accountIdService.accountPlaceHolder(ACQUISITION, DEBIT, lenovo)).thenReturn("011");
        when(accountIdService.accountPlaceHolder(ACQUISITION, DEBIT, chair)).thenReturn("012");
        when(accountIdService.accountPlaceHolder(ACQUISITION, DEBIT, zemana)).thenReturn("013");
        when(accountIdService.accountPlaceHolder(ACQUISITION, DEBIT, kca)).thenReturn("014");
        when(accountIdService.accountPlaceHolder(ACQUISITION, DEBIT, officePartitioning)).thenReturn("015");

        when(accountIdService.generalLedgerCode(ACQUISITION, DEBIT, radio)).thenReturn("00156");
        when(accountIdService.generalLedgerCode(ACQUISITION, DEBIT, lenovo)).thenReturn("00152");
        when(accountIdService.generalLedgerCode(ACQUISITION, DEBIT, chair)).thenReturn("00153");
        when(accountIdService.generalLedgerCode(ACQUISITION, DEBIT, zemana)).thenReturn("00155");
        when(accountIdService.generalLedgerCode(ACQUISITION, DEBIT, kca)).thenReturn("00151");
        when(accountIdService.generalLedgerCode(ACQUISITION, DEBIT, officePartitioning)).thenReturn("00154");

        when(accountIdService.currencyCode("KES")).thenReturn("00");

        accountIdResolver = new AcquisitionDebitAccountIdResolver(accountIdService);
    }

    @Test
    public void resolveName() {

        assertEquals("ELECTRONICS", accountIdResolver.accountName(radio));
        assertEquals("COMPUTERS", accountIdResolver.accountName(lenovo));
        assertEquals("FURNITURE", accountIdResolver.accountName(chair));
        assertEquals("COMPUTER SOFTWARE", accountIdResolver.accountName(zemana));
        assertEquals("MOTOR VEHICLES", accountIdResolver.accountName(kca));
        assertEquals("OFFICE RENOVATION", accountIdResolver.accountName(officePartitioning));

    }

    @Test
    public void resolveContraAccountId() {

        assertEquals("ACCUMULATED DEPRECIATION ON COMPUTERS", accountIdResolver.resolveContraAccountId(lenovo));
        assertEquals("ACCUMULATED DEPRECIATION ON FURNITURE", accountIdResolver.resolveContraAccountId(chair));
        assertEquals("ACCUMULATED DEPRECIATION ON ELECTRONICS", accountIdResolver.resolveContraAccountId(radio));
    }

    @Test
    public void resolveCategoryId() {
        assertEquals("ELECTRONICS", accountIdResolver.resolveCategoryId(radio));
        assertEquals("COMPUTERS", accountIdResolver.resolveCategoryId(lenovo));
        assertEquals("FURNITURE", accountIdResolver.resolveCategoryId(chair));
        assertEquals("COMPUTER SOFTWARE", accountIdResolver.resolveCategoryId(zemana));
        assertEquals("MOTOR VEHICLES", accountIdResolver.resolveCategoryId(kca));
        assertEquals("OFFICE RENOVATION", accountIdResolver.resolveCategoryId(officePartitioning));
    }

    @Test
    public void resolveNumber() {

        assertEquals("0010000154015", accountIdResolver.accountNumber(radio));
        assertEquals("0100000153014", accountIdResolver.accountNumber(chair));
        assertEquals("9870000152013", accountIdResolver.accountNumber(lenovo));
    }

    @Test
    public void resolveGeneralLedgerId() {

        assertEquals("ELECTRONICS", accountIdResolver.generalLedgerName(radio));
        assertEquals("FURNITURE", accountIdResolver.generalLedgerName(chair));
        assertEquals("COMPUTERS", accountIdResolver.generalLedgerName(lenovo));
    }
}