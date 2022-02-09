package com.occupancy.api.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface DataRepository extends JpaRepository<Data,Long> {




}
