package com.occupancy.api.facility;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacilityRepository extends JpaRepository<Facility,Long> {

    @Query("SELECT f FROM Facility f WHERE f.ownerId = ?1")
    List<Facility> findByOwnerId(Long ownerId);

}
