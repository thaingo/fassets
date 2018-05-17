/**
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
package io.github.fasset.fasset.managers;

import io.github.fasset.fasset.book.keeper.Account;
import io.github.fasset.fasset.book.keeper.util.UnEnteredDetailsException;
import io.github.fasset.fasset.model.FixedAsset;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static io.github.fasset.fasset.book.keeper.AccountAttribute.ACCOUNT_SCHEME;
import static io.github.fasset.fasset.book.keeper.AccountAttribute.ACCOUNT_SUB_TYPE;
import static io.github.fasset.fasset.book.keeper.AccountAttribute.ACCOUNT_TYPE;
import static io.github.fasset.fasset.book.keeper.AccountAttribute.CATEGORY;
import static io.github.fasset.fasset.book.keeper.AccountAttribute.SERVICE_OUTLET;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class DefaultAccountResolverTest {

    private DefaultAccountResolver defaultAccountResolver;

    private final static FixedAsset radio = new FixedAsset("Radio", Money.of(200,"KES"), "Electronics", "001", LocalDate.of(2018,2,5), "abc01", Money.of(9.5,"KES"));
    private final static FixedAsset lenovo = new FixedAsset("Lenovo IDP110", Money.of(5600,"KES"), "Computers", "987",LocalDate.of(2018,2,13), "abc02", Money.of(13.42,"KES"));
    private final static FixedAsset chair = new FixedAsset("Chair", Money.of(156,"KES"), "FURNITURE", "010",LocalDate.of(2018,1,13),"abc03", Money.of(19.24,"KES"));

    @Before
    public void setUp() throws Exception {

        AccountIDResolver accountIDResolver = Mockito.mock(AccountIDResolver.class);

        defaultAccountResolver = new DefaultAccountResolver(accountIDResolver);

        when(accountIDResolver.resolveCategoryId(lenovo)).thenReturn("Computers");
        when(accountIDResolver.resolveContraAccountId(lenovo)).thenReturn("Accumulated Depreciation on Computers");
        when(accountIDResolver.resolveGeneralLedgerId(lenovo)).thenReturn("1025648");

        when(accountIDResolver.resolveCategoryId(radio)).thenReturn("Electronics");
        when(accountIDResolver.resolveContraAccountId(radio)).thenReturn("Accumulated Depreciation on Electronics");
        when(accountIDResolver.resolveGeneralLedgerId(radio)).thenReturn("1025649");

        when(accountIDResolver.resolveCategoryId(chair)).thenReturn("Furniture");
        when(accountIDResolver.resolveContraAccountId(chair)).thenReturn("Accumulated Depreciation on Furniture");
        when(accountIDResolver.resolveGeneralLedgerId(chair)).thenReturn("1025650");


    }

    @Test
    public void getAcquisitionDebitAccount() throws UnEnteredDetailsException {

        Account computers = defaultAccountResolver.getAcquisitionDebitAccount(lenovo);
        Account sundryCreditors = defaultAccountResolver.getAcquisitionCreditAccount(lenovo);

        Account electronics = defaultAccountResolver.getAcquisitionDebitAccount(radio);
        Account sundryCreditors1 = defaultAccountResolver.getAcquisitionCreditAccount(radio);

        Account furniture = defaultAccountResolver.getAcquisitionDebitAccount(chair);
        Account sundryCreditors2 = defaultAccountResolver.getAcquisitionCreditAccount(chair);


        assertNotEquals(computers, sundryCreditors);

        /*This sameness test is going to interfere with the undefined a/c hieracrchy problem*/
        assertNotEquals(sundryCreditors, sundryCreditors1);
        assertNotEquals(sundryCreditors, sundryCreditors2);
        assertNotEquals(sundryCreditors1, sundryCreditors2);

        assertEquals("Asset", computers.getAttribute(ACCOUNT_TYPE));
        assertEquals("Fixed Assets", computers.getAttribute(ACCOUNT_SCHEME));
        assertEquals(lenovo.getCategory(), computers.getAttribute(CATEGORY));
        assertEquals("Non Current Asset", computers.getAttribute(ACCOUNT_SUB_TYPE));
        assertEquals(lenovo.getSolId(), computers.getAttribute(SERVICE_OUTLET));
    }

    @Test
    public void getAcquisitionCreditAccount() throws UnEnteredDetailsException {

        Account computers = defaultAccountResolver.getAcquisitionDebitAccount(lenovo);
        Account sundryCreditors = defaultAccountResolver.getAcquisitionCreditAccount(lenovo);

        assertNotEquals(sundryCreditors, computers);
        assertEquals("Liability", sundryCreditors.getAttribute(ACCOUNT_TYPE));
        assertEquals("Sundry Creditors", sundryCreditors.getAttribute(ACCOUNT_SCHEME));
        assertEquals(lenovo.getCategory(), sundryCreditors.getAttribute(CATEGORY));
        assertEquals("Current Liability", sundryCreditors.getAttribute(ACCOUNT_SUB_TYPE));
        assertEquals(lenovo.getSolId(), sundryCreditors.getAttribute(SERVICE_OUTLET));
    }
}