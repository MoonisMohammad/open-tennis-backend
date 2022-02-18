package com.occupancy.api.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface DataRepository extends JpaRepository<Data,Long> {

    @Query(
            "SELECT d FROM " +
                    "Data d WHERE " +
                        "d.timeStamp <= :receivedDate and " +
                        "d.time >= :from and " +
                        "d.time <= :to and " +
                        "d.deviceId = :receivedDeviceId and " +
                        "d.referenceNumber = :receivedReferenceNumber and " +
                        "d.dayOfWeek = :dayOfWeek")
    List<Data> findAllWithDateAfter(@Param("receivedDate") LocalDateTime receivedDate,
                                    @Param("from") LocalTime from,
                                    @Param("to") LocalTime to,
                                    @Param("receivedDeviceId") Long receivedDeviceId,
                                    @Param("receivedReferenceNumber") int receivedReferenceNumber,
                                    @Param("dayOfWeek") DayOfWeek dayOfWeek);

    @Query(
            "SELECT SUM(d.count) FROM " +
                    "Data d WHERE " +
                        "d.timeStamp <= :receivedDate and " +
                        "d.deviceId = :receivedDeviceId and " +
                        "d.referenceNumber = :receivedReferenceNumber and " +
                        "d.dayOfWeek = :dayOfWeek")
    Long findTotalCountLast30Days(@Param("receivedDate") LocalDateTime receivedDate,
                                 @Param("receivedDeviceId") Long receivedDeviceId,
                                 @Param("receivedReferenceNumber") int receivedReferenceNumber,
                                 @Param("dayOfWeek") DayOfWeek dayOfWeek);

}
