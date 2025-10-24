// /src/main/java/com/example/attendence/repository/StudentRepository.java

package com.example.attendence.repository;

import com.example.attendence.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByBatchName(String batchName);
}