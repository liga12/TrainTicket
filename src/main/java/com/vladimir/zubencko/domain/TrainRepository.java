package com.vladimir.zubencko.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TrainRepository extends JpaRepository<Train, Integer> {
    @Query("select case when count (t) > 0  then true else false end from Train t where t.name = :name")
    Boolean existsByName(@Param("name") String name);
}
