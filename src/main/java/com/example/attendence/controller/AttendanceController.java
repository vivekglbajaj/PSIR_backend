// /src/main/java/com/example/attendence/controller/AttendanceController.java

package com.example.attendence.controller;

import com.example.attendence.model.*;
import com.example.attendence.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AttendanceController {

    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;

    /**
     * Manual Constructor Injection
     * Spring will automatically provide the implementations for the Repositories.
     */
    public AttendanceController(StudentRepository studentRepository, AttendanceRepository attendanceRepository) {
        this.studentRepository = studentRepository;
        this.attendanceRepository = attendanceRepository;
    }

    // --- 1. ADD STUDENT ---
    @PostMapping("/students")
    public Student addStudent(@RequestBody Student student) {
        return studentRepository.save(student);
    }

    // --- 2. GET STUDENTS FOR ATTENDANCE (Filtered by current batch) ---
    @GetMapping("/students/batch/{batchName}")
    public List<Student> getStudentsByBatch(@PathVariable String batchName) {
        return studentRepository.findByBatchName(batchName);
    }

    // --- 3. MARK ATTENDANCE ---
    @PostMapping("/attendance/mark")
    public List<AttendanceRecord> markAttendance(@RequestBody List<AttendanceRecord> records) {
        return attendanceRepository.saveAll(records);
    }

    // --- 4. MONTHLY ANALYSIS & PAGINATION ---
    @GetMapping("/attendance/analysis")
    public Page<AttendanceRecord> getMonthlyAnalysis(
            @RequestParam Long studentId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        // Pagination logic: page 0, size = 31 (max days in a month)
        Pageable pageable = PageRequest.of(0, 31, Sort.by("attendanceDate"));

        return attendanceRepository.findByStudentIdAndAttendanceDateBetween(
                studentId, startDate, endDate, pageable);
    }
}