package com.pryabykh.entityservice.services;

import com.pryabykh.entityservice.dtos.request.PageSizeDto;
import com.pryabykh.entityservice.dtos.request.TeacherRequestDto;
import com.pryabykh.entityservice.dtos.response.TeacherResponseDto;
import com.pryabykh.entityservice.exceptions.EntityNotFoundException;
import com.pryabykh.entityservice.exceptions.PermissionDeniedException;
import com.pryabykh.entityservice.models.Classroom;
import com.pryabykh.entityservice.models.Teacher;
import com.pryabykh.entityservice.repositories.ClassroomRepository;
import com.pryabykh.entityservice.repositories.TeacherRepository;
import com.pryabykh.entityservice.userContext.UserContextHolder;
import com.pryabykh.entityservice.utils.TeacherDtoUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;
    private final ClassroomRepository classroomRepository;

    public TeacherServiceImpl(TeacherRepository teacherRepository, ClassroomRepository classroomRepository) {
        this.teacherRepository = teacherRepository;
        this.classroomRepository = classroomRepository;
    }

    @Override
    @Transactional
    public TeacherResponseDto create(TeacherRequestDto teacherDto) {
        Long userId = UserContextHolder.getContext().getUserId();
        Teacher teacherEntity = TeacherDtoUtils.convertToEntity(teacherDto);
        teacherEntity.setCreatorId(userId);
        Teacher savedTeacher = teacherRepository.save(teacherEntity);
        teacherDto.getClassrooms().forEach((classroomId) -> {
            Optional<Classroom> optionalClassroom = classroomRepository.findByIdAndCreatorId(classroomId, userId);
            if (optionalClassroom.isPresent()) {
                Teacher teacher = new Teacher();
                teacher.setId(savedTeacher.getId());
                Classroom classroom = optionalClassroom.get();
                classroom.setTeacher(teacher);
                classroomRepository.save(classroom);
            }
        });
        return TeacherDtoUtils.convertFromEntityWithoutClassroom(savedTeacher);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TeacherResponseDto> fetchAll(PageSizeDto pageSizeDto) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherResponseDto> fetchAllList() {
        Long userId = UserContextHolder.getContext().getUserId();
        return teacherRepository.findAllByCreatorId(userId)
                .stream()
                .map(TeacherDtoUtils::convertFromEntityWithoutClassroom)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TeacherResponseDto fetchById(Long id) {
        return null;
    }

    @Override
    @Transactional
    public TeacherResponseDto update(Long id, TeacherRequestDto teacherDto) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(id);
        if (optionalTeacher.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Teacher currentTeacher = optionalTeacher.get();
        Long userId = UserContextHolder.getContext().getUserId();
        if (!Objects.equals(currentTeacher.getCreatorId(), userId)) {
            throw new PermissionDeniedException();
        }
        Teacher newTeacher = TeacherDtoUtils.convertToEntity(teacherDto);
        currentTeacher.setFirstName(newTeacher.getFirstName());
        currentTeacher.setPatronymic(newTeacher.getPatronymic());
        currentTeacher.setLastName(newTeacher.getLastName());
        Teacher savedTeacher = teacherRepository.save(currentTeacher);
        List<Classroom> classroomsToDeleteTeacher = fetchClassroomsWhereTeacherSetToNull(currentTeacher, newTeacher);
        classroomsToDeleteTeacher.forEach(classroomIdOnly -> {
            Long classroomId = classroomIdOnly.getId();
            Optional<Classroom> optionalClassroom = classroomRepository.findByIdAndCreatorId(classroomId, userId);
            if (optionalClassroom.isPresent()) {
                Classroom classroom = optionalClassroom.get();
                classroom.setTeacher(null);
                classroomRepository.save(classroom);
            }
        });
        List<Classroom> classroomsToAddTeacher = fetchClassroomsWhereAddTeacher(currentTeacher, newTeacher);
        classroomsToAddTeacher.forEach(classroomIdOnly -> {
            Long classroomId = classroomIdOnly.getId();
            Optional<Classroom> optionalClassroom = classroomRepository.findByIdAndCreatorId(classroomId, userId);
            if (optionalClassroom.isPresent()) {
                Teacher teacher = new Teacher();
                teacher.setId(savedTeacher.getId());
                Classroom classroom = optionalClassroom.get();
                classroom.setTeacher(teacher);
                classroomRepository.save(classroom);
            }
        });
        return TeacherDtoUtils.convertFromEntityWithoutClassroom(savedTeacher);
    }

    @Override
    @Transactional
    public void delete(Long id) {

    }

    private List<Classroom> fetchClassroomsWhereTeacherSetToNull(Teacher currentTeacher, Teacher newTeacher) {
        return currentTeacher.getClassrooms()
                .stream()
                .filter(currentClassroom -> {
                    return newTeacher.getClassrooms()
                            .stream()
                            .noneMatch(newClassroom -> newClassroom.getId().equals(currentClassroom.getId()));
                }).collect(Collectors.toList());
    }

    private List<Classroom> fetchClassroomsWhereAddTeacher(Teacher currentTeacher, Teacher newTeacher) {
        return newTeacher.getClassrooms()
                .stream()
                .filter(newClassroom -> {
                    return currentTeacher.getClassrooms()
                            .stream()
                            .noneMatch(currentClassroom -> currentClassroom.getId().equals(newClassroom.getId()));
                }).collect(Collectors.toList());
    }
}
