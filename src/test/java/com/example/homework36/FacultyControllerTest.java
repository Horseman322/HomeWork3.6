package com.example.homework36;

import com.example.homework36.component.RecordMapper;
import com.example.homework36.entity.Faculty;
import com.example.homework36.record.FacultyRecord;
import com.example.homework36.repository.FacultyRepository;
import com.example.homework36.service.FacultyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = FacultyControllerTest.class)
@ExtendWith(MockitoExtension.class)
public class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private FacultyService facultyService;

    @SpyBean
    private RecordMapper recordMapper;

    @Test
    public void createTest() throws Exception{
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setColor("Красный");
        faculty.setName("Гриффиндор");

        when(facultyRepository.save(any())).thenReturn(faculty);

        FacultyRecord facultyRecord = new FacultyRecord();
        facultyRecord.setColor("Красный");
        facultyRecord.setName("Гриффиндор");

        mockMvc.perform(MockMvcRequestBuilders.post("/faculty")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(facultyRecord)))
                .andExpect(result -> {
                    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
                    FacultyRecord facultyRecordResult = objectMapper.readValue(mockHttpServletResponse.getContentAsString(StandardCharsets.UTF_8), FacultyRecord.class);
                   assertThat(mockHttpServletResponse.getStatus()).isEqualTo(HttpStatus.CREATED.value());
                   assertThat(facultyRecordResult).isNotNull();
                   assertThat(facultyRecordResult).usingRecursiveComparison().ignoringFields("id").isEqualTo(facultyRecord);
                   assertThat(facultyRecordResult.getId()).isEqualTo(faculty.getId());
                });
    }

}
