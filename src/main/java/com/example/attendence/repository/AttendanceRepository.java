// /src/main/java/com/example/attendence/repository/AttendanceRepository.java

package com.example.attendence.repository;

import com.example.attendence.model.AttendanceRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;

public interface AttendanceRepository extends JpaRepository<AttendanceRecord, Long> {
    Page<AttendanceRecord> findByStudentIdAndAttendanceDateBetween(
            Long studentId, LocalDate startDate, LocalDate endDate, Pageable pageable);
}