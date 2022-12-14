package com.example.homework35.component.controller;




import com.example.homework35.record.FacultyRecord;
import com.example.homework35.record.StudentRecord;
import com.example.homework35.service.FacultyService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

;
@RestController
@RequestMapping("/faculties")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }
    @PostMapping
    public FacultyRecord create(@RequestBody @Valid FacultyRecord facultyRecord){
        return facultyService.create(facultyRecord);
    }

    @GetMapping("/{id}")
    public FacultyRecord read(@PathVariable long id){
        return facultyService.read(id);
    }

    @PutMapping("/{id}")
    public FacultyRecord update(@PathVariable long id,
                          @RequestBody @Valid FacultyRecord facultyRecord){
        return facultyService.update(id, facultyRecord);
    }

    @DeleteMapping("/{id}")
    public FacultyRecord delete(@PathVariable long id){
        return facultyService.delete(id);
    }

    @GetMapping(params = "colors")
    public Collection<FacultyRecord> findByColor(@RequestParam String color){
        return facultyService.findByColor(color);
    }

    @GetMapping(params = "colorOrName")
    public Collection<FacultyRecord> findByColorOrName(@RequestParam String colorOrName){
        return facultyService.findByColorOrName(colorOrName);
    }

    @GetMapping("/{id}/students")
    public Collection<StudentRecord> getStudentsByFaculty(@PathVariable long id){
        return facultyService.getStudentsByFaculty(id);
    }

}
