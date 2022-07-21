package com.pryabykh.entityservice.controllers;

import com.pryabykh.entityservice.dtos.request.PageSizeDto;
import com.pryabykh.entityservice.dtos.request.TeacherRequestDto;
import com.pryabykh.entityservice.dtos.response.SubjectResponseDto;
import com.pryabykh.entityservice.dtos.response.TeacherResponseDto;
import com.pryabykh.entityservice.exceptions.EntityNotFoundException;
import com.pryabykh.entityservice.exceptions.PermissionDeniedException;
import com.pryabykh.entityservice.services.TeacherService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;

@RestController
@RequestMapping("/v1/teachers")
public class TeacherController {
    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping("")
    ResponseEntity<TeacherResponseDto> create(@RequestBody TeacherRequestDto teacherRequestDto) {
        return ResponseEntity.ok(teacherService.create(teacherRequestDto));
    }

    @PostMapping("/all")
    ResponseEntity<Page<TeacherResponseDto>> fetchAll(@RequestBody PageSizeDto pageSizeDto) {
        return ResponseEntity.ok(teacherService.fetchAll(pageSizeDto));
    }

    @GetMapping("/list")
    ResponseEntity<List<TeacherResponseDto>> fetchAll() {
        return ResponseEntity.ok(teacherService.fetchAllList());
    }

    @GetMapping("/{id}")
    ResponseEntity<TeacherResponseDto> fetchById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(teacherService.fetchById(id));
    }

    @PutMapping("/{id}")
    ResponseEntity<TeacherResponseDto> update(@PathVariable("id") Long id,
                                              @RequestBody TeacherRequestDto teacherRequestDto) {
        return ResponseEntity.ok(teacherService.update(id, teacherRequestDto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable("id") Long id) {
        teacherService.delete(id);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleConstraintViolationException() {
        return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({PermissionDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handlePermissionDeniedException() {
        return new ResponseEntity<>("Permission denied", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleEntityNotFoundException() {
        return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleException(Throwable throwable) {
        throwable.printStackTrace();
        return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
