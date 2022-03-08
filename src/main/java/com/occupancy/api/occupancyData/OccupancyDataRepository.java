package com.occupancy.api.occupancyData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface OccupancyDataRepository extends JpaRepository<OccupancyData,Long> {

    @Query(
            "SELECT d FROM " +
                    "OccupancyData d WHERE " +
                        "d.timeStamp >= :receivedDate and " +
                        "d.time >= :from and " +
                        "d.time <= :to and " +
                        "d.deviceId = :receivedDeviceId and " +
                        "d.referenceNumber = :receivedReferenceNumber")
    List<OccupancyData> findAllWithDateAfter(@Param("receivedDate") LocalDateTime receivedDate,
                                             @Param("from") LocalTime from,
                                             @Param("to") LocalTime to,
                                             @Param("receivedDeviceId") Long receivedDeviceId,
                                             @Param("receivedReferenceNumber") int receivedReferenceNumber);
    @Query(
            "SELECT d FROM " +
                    "OccupancyData d WHERE " +
                        "d.timeStamp <= :to and " +
                        "d.timeStamp >= :from and " +
                        "d.deviceId = :receivedDeviceId and " +
                        "d.referenceNumber = :receivedReferenceNumber")
    List<OccupancyData> getBetween(@Param("from") LocalDateTime from,
                          @Param("to") LocalDateTime to,
                          @Param("receivedDeviceId") Long receivedDeviceId,
                          @Param("receivedReferenceNumber") int receivedReferenceNumber);
}
