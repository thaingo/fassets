package io.github.fasset.fasset.repository;

import io.github.fasset.fasset.model.NetBookValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

@Repository("netBookValueRepository")
public interface NetBookValueRepository extends JpaRepository<NetBookValue, Integer> {
}
