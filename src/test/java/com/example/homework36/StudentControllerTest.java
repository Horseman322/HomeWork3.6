package com.example.homework36;

import com.example.homework36.entity.Faculty;
import com.example.homework36.entity.Student;
import com.example.homework36.record.FacultyRecord;
import com.example.homework36.record.StudentRecord;
import com.example.homework36.repository.FacultyRepository;
import com.example.homework36.repository.StudentRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private FacultyRepository facultyRepository;

    @BeforeEach
    public void beforeEach(){
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setColor("Красный");
        faculty.setName("Гриффиндор");

        Student harry = new Student();
        harry.setId(1L);
        harry.setAge(22);
        harry.setName("Гарри Поттер");
        harry.setFaculty(faculty);

        when(facultyRepository.findById(any())).thenReturn(Optional.of(faculty));
        when(studentRepository.save(harry)).thenReturn(harry);
    }


    @Test
    public void createdTest(){
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setColor("Красный");
        faculty.setName("Гриффиндор");

        Student harry = new Student();
        harry.setId(1L);
        harry.setAge(22);
        harry.setName("Гарри Поттер");
        harry.setFaculty(faculty);

        when(facultyRepository.findById(any())).thenReturn(Optional.of(faculty));
        when(studentRepository.save(any())).thenReturn(harry);

        FacultyRecord facultyRecord = new FacultyRecord();
        facultyRecord.setColor("Красный");
        facultyRecord.setName("Гриффиндор");

        StudentRecord studentRecord = new StudentRecord();
        studentRecord.setAge(22);
        studentRecord.setName("Гарри Поттер");
        studentRecord.setFaculty(facultyRecord);

        ResponseEntity<StudentRecord> recordResponseEntity = testRestTemplate.postForEntity("http://localhost" + port + "student", studentRecord, StudentRecord.class);
        assertThat(recordResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(recordResponseEntity.getBody()).isNotNull();
        assertThat(recordResponseEntity.getBody()).usingRecursiveComparison().ignoringFields("id", "faculty.id").isEqualTo(studentRecord);
        assertThat(recordResponseEntity.getBody().getId()).isEqualTo(harry.getId());
        assertThat(recordResponseEntity.getBody().getFaculty().getId()).isEqualTo(faculty.getId());
    }
    @Test
    public void putTest(){
        Faculty faculty1 = new Faculty();
        faculty1.setId(1L);
        faculty1.setColor("Красный");
        faculty1.setName("Гриффиндор");

        Faculty faculty2 = new Faculty();
        faculty2.setId(1L);
        faculty2.setColor("Зеленый");
        faculty2.setName("Слизерин");

        Student harry = new Student();
        harry.setId(1L);
        harry.setAge(22);
        harry.setName("Гарри Поттер");
        harry.setFaculty(faculty1);

        when(facultyRepository.findById(eq(faculty1.getId()))).thenReturn(Optional.of(faculty1));
        when(facultyRepository.findById(eq(faculty1.getId()))).thenReturn(Optional.of(faculty2));
        when(studentRepository.findById(eq(harry.getId()))).thenReturn(Optional.of(harry));
        when(studentRepository.save(any())).thenReturn(harry);

        FacultyRecord facultyRecord1 = new FacultyRecord();
        facultyRecord1.setId(faculty1.getId());
        facultyRecord1.setColor(faculty1.getColor());
        facultyRecord1.setName(faculty1.getName());

        StudentRecord studentRecord = new StudentRecord();
        studentRecord.setId(harry.getId());
        studentRecord.setAge(harry.getAge());
        studentRecord.setName(harry.getName());
        studentRecord.setFaculty(facultyRecord1);

        ResponseEntity<StudentRecord> getForEntityResponse = testRestTemplate.getForEntity("http://localhost" + port + "/students/" +harry.getId(), StudentRecord.class);
        assertThat(getForEntityResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getForEntityResponse.getBody()).isNotNull();
        assertThat(getForEntityResponse.getBody()).usingRecursiveComparison().isEqualTo(studentRecord);
        assertThat(getForEntityResponse.getBody().getId()).isEqualTo(harry.getId());
        assertThat(getForEntityResponse.getBody().getFaculty().getId()).isEqualTo(faculty1.getId());

        FacultyRecord facultyRecord2 = new FacultyRecord();
        facultyRecord2.setId(faculty2.getId());
        facultyRecord2.setColor(faculty2.getColor());
        facultyRecord2.setName(faculty2.getName());

        studentRecord.setFaculty(facultyRecord2);

        ResponseEntity<StudentRecord> recordResponseEntity = testRestTemplate.postForEntity("http://localhost" + port + "/students", studentRecord, StudentRecord.class);
        assertThat(recordResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(recordResponseEntity.getBody()).isNotNull();
        assertThat(recordResponseEntity.getBody()).usingRecursiveComparison().isEqualTo(studentRecord);
        assertThat(recordResponseEntity.getBody().getId()).isEqualTo(harry.getId());
        assertThat(recordResponseEntity.getBody().getFaculty().getId()).isEqualTo(faculty2.getId());
    }
}
