import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleWebServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        List<Course> masterCourseList = DataManager.loadCourses();
        StudentController studentController = new StudentController(masterCourseList);
        ProfessorController professorController = new ProfessorController(masterCourseList);

        server.createContext("/", exchange -> {
            String path = exchange.getRequestURI().getPath();
            if (path.equals("/") || path.equals("/index.html")) serveHtmlFile(exchange, "index.html");
            else if (path.equals("/student.html")) serveHtmlFile(exchange, "student.html");
            else if (path.equals("/professor.html")) serveHtmlFile(exchange, "professor.html");
            else if (path.equals("/register.html")) serveHtmlFile(exchange, "register.html");
            else serveHtmlFile(exchange, "404");
        });

        server.createContext("/api/loginStudent", exchange -> {
            Map<String, String> params = parseExchange(exchange);
            Student found = DataManager.loadStudents().stream()
                    .filter(s -> s.getId().equals(params.get("id"))).findFirst().orElse(null);
            sendResponse(exchange, found != null ? "{\"success\": true, \"name\": \"" + found.getName() + "\"}" : "{\"success\": false}");
        });

        server.createContext("/api/loginProfessor", exchange -> {
            Map<String, String> params = parseExchange(exchange);
            Professor found = DataManager.loadProfessors().stream()
                    .filter(p -> p.getId().equals(params.get("id"))).findFirst().orElse(null);
            sendResponse(exchange, found != null ? "{\"success\": true, \"name\": \"" + found.getName() + "\"}" : "{\"success\": false}");
        });

        server.createContext("/api/createStudent", exchange -> {
            Map<String, String> params = parseExchange(exchange);
            Student newStudent = new Student.StudentBuilder(params.get("name"), params.get("id")).setMajor(params.get("major")).build();
            List<Student> students = DataManager.loadStudents();
            students.add(newStudent);
            DataManager.saveStudents(students);
            sendResponse(exchange, "Student Saved!");
        });

        server.createContext("/api/createProfessor", exchange -> {
            Map<String, String> params = parseExchange(exchange);
            Professor newProfessor = new Professor(params.get("name"), params.get("id"), params.get("department"));
            List<Professor> profs = DataManager.loadProfessors();
            profs.add(newProfessor);
            DataManager.saveProfessors(profs);
            sendResponse(exchange, "Professor Saved!");
        });

        server.createContext("/api/getCourses", exchange -> {
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < masterCourseList.size(); i++) {
                Course c = masterCourseList.get(i);
                json.append(String.format("{\"id\":\"%s\", \"name\":\"%s\"}", c.getCourseID(), c.getCourseName()));
                if (i < masterCourseList.size() - 1) json.append(",");
            }
            json.append("]");
            sendResponse(exchange, json.toString());
        });

        server.createContext("/api/createCourse", exchange -> {
            Map<String, String> params = parseExchange(exchange);
            String strategyType = params.get("strategy");
            GradingStrategy strategy = null;

            if (strategyType.equals("PassFail")) {
                strategy = new PassFailGrading();
            } else if (strategyType.equals("Curve")) {
                strategy = new CurveGrading(); 
            } else if (strategyType.equals("Letter")) {
                GradingCriteria criteria = new GradingCriteria(
                    Double.parseDouble(params.get("minA")), Double.parseDouble(params.get("minB")),
                    Double.parseDouble(params.get("minC")), Double.parseDouble(params.get("minD"))
                );
                strategy = new LetterGrading(criteria);
            }

            professorController.createCourse(params.get("courseName"), params.get("courseId"), strategy);
            sendResponse(exchange, "Course created successfully with " + strategyType + " grading!");
        });

        server.createContext("/api/enrollStudent", exchange -> {
            Map<String, String> params = parseExchange(exchange);
            Student student = DataManager.loadStudents().stream().filter(s -> s.getId().equals(params.get("studentId"))).findFirst().orElse(null);
            Course course = masterCourseList.stream().filter(c -> c.getCourseID().equals(params.get("courseId"))).findFirst().orElse(null);
            
            if (student != null && course != null) {
                studentController.enrollInCourse(student, course);
                sendResponse(exchange, "Successfully enrolled in " + course.getCourseName() + "!");
            } else {
                sendResponse(exchange, "Error enrolling.");
            }
        });

        server.createContext("/api/gradeStudent", exchange -> {
            Map<String, String> params = parseExchange(exchange);
            Course course = masterCourseList.stream().filter(c -> c.getCourseID().equals(params.get("courseId"))).findFirst().orElse(null);
            Student student = DataManager.loadStudents().stream().filter(s -> s.getId().equals(params.get("studentId"))).findFirst().orElse(null);
            
            if (course != null && student != null) {
                professorController.inputGrade(course, student, Double.parseDouble(params.get("score")));
                sendResponse(exchange, "Grade submitted successfully!");
            } else {
                sendResponse(exchange, "Error submitting grade.");
            }
        });

        server.createContext("/api/getTranscript", exchange -> {
            Map<String, String> params = parseExchange(exchange);
            Student student = DataManager.loadStudents().stream().filter(s -> s.getId().equals(params.get("studentId"))).findFirst().orElse(null);
            
            if (student != null) {
                Map<String, String> transcript = studentController.getMyTranscript(student);
                StringBuilder json = new StringBuilder("{");
                int count = 0;
                for (Map.Entry<String, String> entry : transcript.entrySet()) {
                    json.append(String.format("\"%s\":\"%s\"", entry.getKey(), entry.getValue()));
                    if (++count < transcript.size()) json.append(",");
                }
                json.append("}");
                sendResponse(exchange, json.toString());
            } else {
                sendResponse(exchange, "{}");
            }
        });

       server.createContext("/api/getNotifications", exchange -> {
            Map<String, String> params = parseExchange(exchange);
            String targetId = params.get("studentId");
            
            List<String> allNotifs = new java.util.ArrayList<>();
            
            for (Course c : masterCourseList) {
                for (Student s : c.getEnrolledStudents()) {
                    if (s.getId().equals(targetId) && s.getInbox() != null) {
                        allNotifs.addAll(s.getInbox()); 
                    }
                }
            }

            if (!allNotifs.isEmpty()) {
                StringBuilder json = new StringBuilder("[");
                for (int i = 0; i < allNotifs.size(); i++) {
                    json.append("\"").append(allNotifs.get(i)).append("\"");
                    if (i < allNotifs.size() - 1) json.append(",");
                }
                json.append("]");
                sendResponse(exchange, json.toString());
            } else {
                sendResponse(exchange, "[]");
            }
        });



        server.start();
        System.out.println("Server running on http://localhost:8080");
    }

    private static void serveHtmlFile(HttpExchange exchange, String filename) throws IOException {
        File htmlFile = new File(filename);
        if (htmlFile.exists()) {
            byte[] response = Files.readAllBytes(Paths.get(filename));
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, response.length);
            exchange.getResponseBody().write(response);
        } else {
            exchange.sendResponseHeaders(404, 0);
        }
        exchange.close();
    }

    private static Map<String, String> parseExchange(HttpExchange exchange) throws IOException {
        String formData = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Map<String, String> map = new HashMap<>();
        for (String pair : formData.split("&")) {
            String[] kv = pair.split("=");
            map.put(URLDecoder.decode(kv[0], "UTF-8"), kv.length > 1 ? URLDecoder.decode(kv[1], "UTF-8") : "");
        }
        return map;
    }

    private static void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length());
        exchange.getResponseBody().write(response.getBytes());
        exchange.close();
    }
}