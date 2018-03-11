package io.github.fasset.fasset.repository;

import io.github.fasset.fasset.model.NetBookValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class NetBookValueRepositoryTest {


    @Qualifier("netBookValueRepository")
    @Autowired
    private NetBookValueRepository valueRepository;

    @Test
    public void netBookValueRepositoryWorks() throws Exception {

        assertNotNull(valueRepository.save(new NetBookValue()));
    }
}