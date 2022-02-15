package com.occupancy.api.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface DataRepository extends JpaRepository<Data,Long> {

   // @Query(
   //            "SELECT d " +
   //                    "FROM Data d WHERE " +
   //                        "timeStamp > :receivedDate " +
   //                        "AND deviceId = :receivedDeviceId" +
   //                        "AND referenceNumber = :receivedReferenceNumber"
   //    )
    @Query(
            "SELECT d FROM " +
                    "Data d WHERE " +
                        "d.timeStamp <= :receivedDate and " +
                        "d.deviceId = :receivedDeviceId and " +
                        "referenceNumber = :receivedReferenceNumber"
    )
    List<Data> findAllWithDateAfter(@Param("receivedDate") LocalDateTime receivedDate,
                                    @Param("receivedDeviceId") Long receivedDeviceId,
                                    @Param("receivedReferenceNumber") Integer receivedReferenceNumber);
}
