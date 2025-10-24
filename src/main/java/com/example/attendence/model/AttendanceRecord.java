// /src/main/java/com/example/attendence/model/AttendanceRecord.java

package com.example.attendence.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class AttendanceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate attendanceDate;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private String batchName;
    private String status; // Present, Absent

    // Default constructor is required by JPA
    public AttendanceRecord() {
    }

    // Getters
    public Long getId() {
        return id;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public Student getStudent() {
        return student;
    }

    public String getBatchName() {
        return batchName;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}