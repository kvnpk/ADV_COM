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

        // --- PAGE ROUTES ---
        server.createContext("/", exchange -> {
            String path = exchange.getRequestURI().getPath();
            if (path.equals("/") || path.equals("/index.html")) serveHtmlFile(exchange, "index.html");
            else if (path.equals("/student.html")) serveHtmlFile(exchange, "student.html");
            else if (path.equals("/professor.html")) serveHtmlFile(exchange, "professor.html");
            else if (path.equals("/register.html")) serveHtmlFile(exchange, "register.html");
            else {
                String error = "404 - Page not found!";
                exchange.sendResponseHeaders(404, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.close();
            }
        });

        // --- LOGIN API ROUTES ---
        server.createContext("/api/loginStudent", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                Map<String, String> params = parseFormData(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
                String id = params.get("id");
                
                Student found = DataManager.loadStudents().stream()
                        .filter(s -> s.getId().equals(id)).findFirst().orElse(null);

                String response = (found != null) ? "{\"success\": true, \"name\": \"" + found.getName() + "\"}" : "{\"success\": false}";
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, response.length());
                exchange.getResponseBody().write(response.getBytes());
            }
            exchange.close();
        });

        server.createContext("/api/loginProfessor", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                Map<String, String> params = parseFormData(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
                String id = params.get("id");
                
                Professor found = DataManager.loadProfessors().stream()
                        .filter(p -> p.getId().equals(id)).findFirst().orElse(null);

                String response = (found != null) ? "{\"success\": true, \"name\": \"" + found.getName() + "\"}" : "{\"success\": false}";
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, response.length());
                exchange.getResponseBody().write(response.getBytes());
            }
            exchange.close();
        });

        // --- CREATION / ENROLLMENT API ROUTES ---
        // (These remain exactly the same as before)
        server.createContext("/api/getStudents", exchange -> {
            List<Student> students = DataManager.loadStudents(); 
            
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < students.size(); i++) {
                Student s = students.get(i);
                json.append(String.format("{\"name\":\"%s\", \"id\":\"%s\"}", s.getName(), s.getId()));
                if (i < students.size() - 1) json.append(",");
            }
            json.append("]");
            
            byte[] response = json.toString().getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.length);
            exchange.getResponseBody().write(response);
            exchange.close();
        });

        server.createContext("/api/createStudent", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                Map<String, String> params = parseFormData(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
                Student newStudent = new Student.StudentBuilder(params.get("name"), params.get("id")).setMajor(params.get("major")).build();
                
                List<Student> allStudents = DataManager.loadStudents();
                allStudents.add(newStudent);
                DataManager.saveStudents(allStudents); // Saved using DataManager!
                
                String response = "Student saved successfully!";
                exchange.sendResponseHeaders(200, response.length());
                exchange.getResponseBody().write(response.getBytes());
            }
            exchange.close();
        });

        server.createContext("/api/createProfessor", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                Map<String, String> params = parseFormData(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
                Professor newProfessor = new Professor(params.get("name"), params.get("id"), params.get("department"));
                
                List<Professor> allProfessors = DataManager.loadProfessors();
                allProfessors.add(newProfessor);
                DataManager.saveProfessors(allProfessors); // Saved using DataManager!
                
                String response = "Professor saved successfully!";
                exchange.sendResponseHeaders(200, response.length());
                exchange.getResponseBody().write(response.getBytes());
            }
            exchange.close();
        });

        server.createContext("/api/enrollStudent", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                Map<String, String> params = parseFormData(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
                String studentId = params.get("studentId");
                String courseName = params.get("courseName");
                
                Student activeStudent = DataManager.loadStudents().stream().filter(s -> s.getId().equals(studentId)).findFirst().orElse(null);
                Course targetCourse = masterCourseList.stream().filter(c -> c.getCourseName().equals(courseName)).findFirst().orElse(null);

                if (activeStudent != null && targetCourse != null) {
                    studentController.enrollInCourse(activeStudent, targetCourse); // OOP Controller!
                    String response = "Successfully enrolled in " + courseName + "!";
                    exchange.sendResponseHeaders(200, response.length());
                    exchange.getResponseBody().write(response.getBytes());
                }
            }
            exchange.close();
        });

        server.createContext("/api/submitGrade", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                Map<String, String> params = parseFormData(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
                String studentId = params.get("studentId");
                String courseName = params.get("courseName");
                double score = Double.parseDouble(params.get("score"));
                
                Student targetStudent = DataManager.loadStudents().stream().filter(s -> s.getId().equals(studentId)).findFirst().orElse(null);
                Course targetCourse = masterCourseList.stream().filter(c -> c.getCourseName().equals(courseName)).findFirst().orElse(null);

                if (targetStudent != null && targetCourse != null) {
                    professorController.inputGrade(targetCourse, targetStudent, score); // OOP Strategy!
                    String response = "Grade saved and student notified!";
                    exchange.sendResponseHeaders(200, response.length());
                    exchange.getResponseBody().write(response.getBytes());
                }
            }
            exchange.close();
        });

        server.createContext("/api/getTranscript", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                Map<String, String> params = parseFormData(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
                String studentId = params.get("studentId");
                Student activeStudent = DataManager.loadStudents().stream().filter(s -> s.getId().equals(studentId)).findFirst().orElse(null);

                if (activeStudent != null) {
                    Map<String, String> transcript = studentController.getMyTranscript(activeStudent);
                    
                    StringBuilder json = new StringBuilder("{");
                    for (Map.Entry<String, String> entry : transcript.entrySet()) {
                        json.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\",");
                    }
                    if (json.length() > 1) json.setLength(json.length() - 1); 
                    json.append("}");

                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, json.length());
                    exchange.getResponseBody().write(json.toString().getBytes(StandardCharsets.UTF_8));
                }
            }
            exchange.close();
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
            String error = "404 - " + filename + " not found!";
            exchange.sendResponseHeaders(404, error.length());
            exchange.getResponseBody().write(error.getBytes());
        }
        exchange.close();
    }

    private static Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        for (String pair : formData.split("&")) {
            String[] kv = pair.split("=");
            map.put(URLDecoder.decode(kv[0], "UTF-8"), kv.length > 1 ? URLDecoder.decode(kv[1], "UTF-8") : "");
        }
        return map;
    }
}