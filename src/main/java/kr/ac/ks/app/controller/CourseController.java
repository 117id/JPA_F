package kr.ac.ks.app.controller;

import kr.ac.ks.app.domain.Course;
import kr.ac.ks.app.domain.Lesson;
import kr.ac.ks.app.domain.Student;
import kr.ac.ks.app.repository.CourseRepository;
import kr.ac.ks.app.repository.LessonRepository;
import kr.ac.ks.app.repository.StudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CourseController {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;

    public CourseController(StudentRepository studentRepository, CourseRepository courseRepository, LessonRepository lessonRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
    }

    @GetMapping("/course")
    public String showCourseForm(Model model) {
        List<Student> students = studentRepository.findAll();
        List<Lesson> lessons = lessonRepository.findAll();
        model.addAttribute("students", students);
        model.addAttribute("lessons", lessons);
        return "courses/courseForm";
    }

    @PostMapping("/course")
    public String createCourse(@RequestParam("studentId") Long studentId,
                               @RequestParam("lessonId") Long lessonId
                               // 하나더 입력을 받는다 설정 추가.
    ) {
        Student student = studentRepository.findById(studentId).get();
        Lesson lesson = lessonRepository.findById(lessonId).get();
        Course course = Course.createCourse(student,lesson);
        Course savedCourse = courseRepository.save(course);
        return "redirect:/courses";
    }

    @GetMapping("/courses")
    public String courseList(Model model) {
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        return "courses/courseList";
    }

    @GetMapping("/courses/update/{id}")
    public String getCourseUpdate(@PathVariable("id") Long id, Model model) {
        Course course = courseRepository.findById(id).get();
        List<Student> students = studentRepository.findAll();
        List<Lesson> lessons = lessonRepository.findAll();
        model.addAttribute("course", course);
        model.addAttribute("students", students);
        model.addAttribute("lessons", lessons);
        return "/courses/courseUpdate";
    }

    @PostMapping("/courses/update/{id}")
    public String postCourseUpdate(@PathVariable("id") Long id
            , @RequestParam("studentId") Long studentId
            , @RequestParam("lessonId") Long lessonId) {
        Course course = courseRepository.findById(id).get();
        Student student = studentRepository.findById(studentId).get();
        Lesson lesson = lessonRepository.findById(lessonId).get();
        course.setStudent(student);
        course.setLesson(lesson);
        courseRepository.save(course);
        return "redirect:/courses";
    }

    @GetMapping("/courses/delete/{id}")
    public String deleteCourse(@PathVariable("id") Long id) {
        Course course = courseRepository.findById(id).get();
        courseRepository.delete(course);
        return "redirect:/courses";
    }
//    @GetMapping("/test")
//    public String test(){
//        Course course1 = courseRepository.save(new Course());
//        Course course2 = courseRepository.save(new Course());
//        Student student = new Student();
//        student.getCourses().add(course1);
//
//        course1.setStudent((student)); // db에는 반영이 되어있음.
//        // 단일화가 필요하게됨. 전체 동기화를 시켜준다. 외래키를 가지고 있는 쪽에서  전체 데이터를 일관되게 맞춰준다.
//        //
//        student.getCourses().add(course2);
//
//        // 외래키에는 관심이 없음.
//        studentRepository.save(student);
//        return "courses/courseList";
//    }
}
