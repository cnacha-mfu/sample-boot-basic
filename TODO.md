# In-Class Demonstration Flow — Spring Boot Basics

This repository has these branches:

- **`main`** — the in-class version. Some code is blanked out with `TODO` comments. We fill these in together during the demonstration.
- **[`complete`](https://github.com/cnacha-mfu/sample-boot-basic/tree/complete)** — the finished code for every step. Use it to catch up or check your work.

**Start the application first** and restart it after each change:

```
mvn spring-boot:run
```

Then open <http://localhost:8080/api/hello> to check it is alive. Follow the steps in order; each one adds a single new idea.

> **Postman:** import [`sample-boot-basic.postman_collection.json`](sample-boot-basic.postman_collection.json) (File → Import, or drag the file in). It contains every request used below, in order, with tests that check the expected status codes — *Run collection* replays the whole flow. Restart the app first if you want a clean (empty) student list.

---

## Step 1: Anatomy of a Spring Boot app

**Files:** `src/main/java/th/mfu/App.java`, `HelloController.java` (both already complete — read together)

**Idea:** `@SpringBootApplication` on `App` starts an embedded web server — no Tomcat/Jetty install, no WAR deploy. `@RestController` marks a class whose methods answer HTTP requests, and whatever a method returns becomes the response body. `@GetMapping("/hello")` binds a method to `GET /api/hello`.

**Try it:** run `mvn spring-boot:run`, open <http://localhost:8080/api/hello>. Compare with last week's servlet: where did `web.xml`, the server install, and the deploy step go?

---

## Step 2: Read a value from the URL

**File:** `HelloController.java`

**Idea:** a path variable — `@GetMapping("/hello/{name}")` + `@PathVariable` — hands a piece of the URL to your method as a parameter.

**Fill in together:** in `helloName`, return a greeting that includes `name`.

**Try it:** open <http://localhost:8080/api/hello/Nacha>.

**Solution:** [HelloController](https://github.com/cnacha-mfu/sample-boot-basic/blob/complete/src/main/java/th/mfu/HelloController.java)

---

## Step 3: A model class — plain Java in, JSON out

**File:** `src/main/java/th/mfu/Student.java` (already complete — read together)

**Idea:** a POJO with attributes + getters/setters is all Spring needs to convert JSON to objects and back. No annotations required on the class.

> In the lab you will write the `User` class (username, displayname, email) yourself — same shape as `Student` here. VS Code writes the getters/setters for you: right-click → *Source Action → Generate Getters and Setters*.

---

## Step 4: Register — POST, @RequestBody, and status codes

**File:** `src/main/java/th/mfu/StudentController.java`

**Idea:** `@PostMapping` receives data; `@RequestBody` turns the request's JSON into a `Student` object. A `ResponseEntity` lets you choose the **status code** — the API's way of saying what happened: `201 CREATED` for success, `409 CONFLICT` for a duplicate. A static `HashMap` is today's stand-in for a database.

**Fill in together:** in `registerStudent`:
1. Add `@PostMapping("/students")` on the method and `@RequestBody` on the parameter.
2. If `students` already contains the id → `return new ResponseEntity<>("Student id already exists", HttpStatus.CONFLICT);`
3. Otherwise put the student in the map → `return new ResponseEntity<>("Student registered successfully", HttpStatus.CREATED);`

**Try it (Postman, or curl):**

```
curl -i -X POST http://localhost:8080/students -H "Content-Type: application/json" -d "{\"id\":\"6531501001\",\"name\":\"Alice\",\"email\":\"alice@lamduan.mfu.ac.th\"}"
```

Run it twice — the second call must answer `409`.

**Solution:** [StudentController](https://github.com/cnacha-mfu/sample-boot-basic/blob/complete/src/main/java/th/mfu/StudentController.java)

---

## Step 5: Fetch — GET one (or 404) and GET all

**File:** `StudentController.java` (same file)

**Idea:** the same patterns read data back: `@PathVariable` picks the student, `404 NOT FOUND` when the id is unknown, and returning a `Collection<Student>` becomes a JSON array automatically.

**Fill in together:**
1. `getStudent` — add `@GetMapping("/students/{id}")` + `@PathVariable`; unknown id → `HttpStatus.NOT_FOUND`, otherwise return the student with `HttpStatus.OK`.
2. `listStudents` — add `@GetMapping("/students")`; return `students.values()` with `HttpStatus.OK`.

**Try it:** <http://localhost:8080/students/6531501001>, <http://localhost:8080/students/nobody> (watch the status code in the Network tab or `curl -i`), <http://localhost:8080/students>.

**Solution:** [StudentController](https://github.com/cnacha-mfu/sample-boot-basic/blob/complete/src/main/java/th/mfu/StudentController.java)

---

## Step 6: Unit test — how the lab grades you

**File:** `src/test/java/th/mfu/StudentControllerTest.java` (already complete — read it together)

**Idea:** the test creates the controller like any Java object and calls its methods directly — no server, no browser — then asserts on the `ResponseEntity` status and body. This is exactly how the lab's `UserControllerTest` grades your work, on every push, via GitHub Actions.

**Try it:**

```
mvn test -Dtest=StudentControllerTest
```

Run it **before** Step 4 (red — the TODOs return `null`) and **after** Step 5 (green). Red → implement → green is the rhythm of the lab.

**Solution:** [StudentControllerTest](https://github.com/cnacha-mfu/sample-boot-basic/blob/complete/src/test/java/th/mfu/StudentControllerTest.java)

---

## After class

You are now ready for the graded lab: [lab-web-boot](https://github.com/maefahluang-uni/lab-web-boot) — a **user registration API** built with exactly these pieces:

| Lab requirement | Demo step |
|---|---|
| `User` model with attributes + getters/setters | Step 3 |
| `POST /users` → `201 CREATED`, duplicate → `409 CONFLICT` | Step 4 |
| `GET /users/{username}` → `200 OK` / `404 NOT FOUND` | Step 5 |
| `GET /users` → list all | Step 5 |
| Graded by `UserControllerTest` (`mvn verify`) | Step 6 |

> **Tip:** only one program can listen on port 8080 at a time. If `mvn spring-boot:run` fails with `Port 8080 was already in use`, stop the other server first.
