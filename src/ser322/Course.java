package ser322;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.Date;

public class Course extends Option implements OptionProtocol {
    boolean isEditing = false;

    public void openMenu(Connection conn, Scanner scr) {
        do {
            displayMenuOptions();
            userOpt = scr.nextLine();
            System.out.println("You selected option: " + userOpt);
            // validate user input
            switch (userOpt) {
                case "1":
                    create(conn, scr);
                    break;
                case "2":
                    view(conn, scr);
                    break;
                case "3":
                    edit(conn, scr);
                    break;
                case "4":
                    delete(conn, scr);
                    break;
                case "0":
                    returnToMainMenu();
                    break;
                default:
                    invalidInput("4");
                    break;
            }
        } while (isDone == false);
    }

    public void displayMenuOptions() {
        System.out.println("Manage Courses");
        System.out.println("\t1 - Create New Course");
        System.out.println("\t2 - View Courses");
        System.out.println("\t3 - Edit Course");
        System.out.println("\t4 - Delete Course");
        System.out.println("\t0 - Return to Main Menu");
        System.out.println("Please select a valid menu option (0-4)");
    }

    public void displayEditOptions() {
        System.out.println("Courses: Edit Options");
        System.out.println("\t1 - Date");
        System.out.println("\t2 - Payment Record");
        System.out.println("\t3 - Written Test Score");
        System.out.println("\t4 - Exercise 1 Score");
        System.out.println("\t5 - Exercise 2 Score");
        System.out.println("\t6 - Exercise 3 Score");
        System.out.println("\t7 - Exercise 4 Score");
        System.out.println("\t8 - Exercise 5 Score");
        System.out.println("\t0 - Back to Ranges Menu");
        System.out.println("Please select a valid menu option (0-3)");
    }

    public void view(Connection conn, Scanner scr) {
        String queryStmt = "SELECT * from course";
        try {
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(queryStmt);
            viewDB(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void edit(Connection conn, Scanner scr) {
        isEditing = true;

        do {

            displayEditOptions();
            userOpt = scr.nextLine();

            switch (userOpt) {
                case "1":
                    // date
                    handleDateUpdate(conn, scr);
                    break;
                case "2":
                    // payment record
                    handlePaymentUpdate(conn, scr);
                    break;
                case "3":
                    // written score
                    handleScoreUpdate(conn, scr, 0);
                    break;
                case "4":
                    // exercise score
                    handleScoreUpdate(conn, scr, 1);
                    break;
                case "5":
                    // exercise score
                    handleScoreUpdate(conn, scr, 2);
                    break;
                case "6":
                    // exercise score
                    handleScoreUpdate(conn, scr, 3);
                    break;
                case "7":
                    // exercise score
                    handleScoreUpdate(conn, scr, 4);
                    break;
                case "8":
                    // exercise score
                    handleScoreUpdate(conn, scr, 5);
                    break;
                case "0":
                    isEditing = false;
                    break;
                default:
                    invalidInput("4");
                    break;
            }

        } while (isEditing);
    }

    private void handleDateUpdate(Connection conn, Scanner scr) {

        PreparedStatement ps = UpdateType.date.getPreparedStatement(conn);

        int _courseId = promptCourseId(scr);

        System.out.print("Enter new date: (YYYY-MM-DD)\n");
        String _dateStr = scr.next();
        java.sql.Date date = parseDate(_dateStr);

        try {
            ps.setDate(1, date);
            ps.setInt(2, _courseId);
            updateDB(ps, conn);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void handlePaymentUpdate(Connection conn, Scanner scr) {
        PreparedStatement ps = UpdateType.paymentRecord.getPreparedStatement(conn);

        int _studentId = promptStudentId(scr);
        int _courseId = promptCourseId(scr);

        System.out.println("Enter payment status: (1 == paid, 0 == unpaid)");
        int _paymentComplete = scr.nextInt();

        try {
            ps.setInt(1, _paymentComplete);
            ps.setInt(2, _studentId);
            ps.setInt(3, _courseId);
            updateDB(ps, conn);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void handleScoreUpdate(Connection conn, Scanner scr, int ex) {
        PreparedStatement ps = null;

        switch (ex) {
            case 1:
                ps = UpdateType.exerciseScore1.getPreparedStatement(conn);
                break;
            case 2:
                ps = UpdateType.exerciseScore2.getPreparedStatement(conn);
                break;
            case 3:
                ps = UpdateType.exerciseScore3.getPreparedStatement(conn);
                break;
            case 4:
                ps = UpdateType.exerciseScore4.getPreparedStatement(conn);
                break;
            case 5:
                ps = UpdateType.exerciseScore5.getPreparedStatement(conn);
                break;
            default:
                ps = UpdateType.writtenScore.getPreparedStatement(conn);
        }

        int _studentId = promptStudentId(scr);
        int _courseId = promptCourseId(scr);
        int _newScore = promptNewScore(scr);

        try {
            ps.setInt(1, _newScore);
            ps.setInt(2, _studentId);
            ps.setInt(3, _courseId);
            updateDB(ps, conn);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void delete(Connection conn, Scanner scr) {
        // Input Store Variables
        Integer _pk = null;

        PreparedStatement ps = null;

        System.out.println("Enter course to delete: pk(course_id):");
        _pk = scr.nextInt();

        String deleteStmt = "DELETE FROM course WHERE course_id = ?";
        try {
            ps = conn.prepareStatement(deleteStmt);
            ps.setInt(1, _pk);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (updateDB(ps, conn)) {
            System.out.println("Record deleted successfully.");
        } else {
            System.out.println("No records found to delete.");
        }
    }

    public void create(Connection conn, Scanner scr) {
        CourseModel cm = new CourseModel();
        addCourse(conn, scr, cm);

        Integer _numStudents = null;
        if (cm.course_type.toLowerCase().equals("street")) {
            System.out.println("\n##### " + "Creating new Street Course \n");
            System.out.println("Enter the number students you'd like to add to this course: (max 30)");
            _numStudents = scr.nextInt();
            addInstructors(conn, scr, cm.course_id, RangeType.street, _numStudents);
            addStudents(conn, scr, cm.course_id, _numStudents);
        } else if (cm.course_type.toLowerCase().equals("dirt")) {
            System.out.println("\n##### " + "Creating new Dirt Course \n");
            System.out.println("Enter the number students you'd like to add to this course: (max 15)");
            _numStudents = scr.nextInt();
            addInstructors(conn, scr, cm.course_id, RangeType.dirt, _numStudents);
            addStudents(conn, scr, cm.course_id, _numStudents);
        }
    }

    private int promptStudentId(Scanner scr) {
        System.out.println("Enter student ID:");
        return scr.nextInt();
    }

    private int promptCourseId(Scanner scr) {
        System.out.println("Enter course ID:");
        return scr.nextInt();
    }

    private int promptNewScore(Scanner scr) {
        System.out.println("Enter new score:");
        return scr.nextInt();
    }

    // HELPER METHODS

    public void addInstructors(Connection conn, Scanner scr, int courseId, RangeType rt, int numStudents) {
        switch (rt) {
            case dirt:
                handleDirt(conn, scr, numStudents, courseId);
                break;
            case street:
                handleStreet(conn, scr, numStudents, courseId);
                break;
        }
    }

    public void handleDirt(Connection conn, Scanner scr, int numStudents, int courseId) {
        if (numStudents > 15) {
            numStudents = 15;
            System.out.println("You entered a value greater than 15. Students will be capped at 15.");
        }
        for (int i = 0; i < 3; i++) {
            InstructorModel im = new InstructorModel();
            System.out.println("Enter ID for dirt coach:");
            im.course_id = courseId;
            im.instructor_id = scr.nextInt();
            im.in_session = "BOTH";
            im.instructor_role = "dirt_coach";
            try {
                updateDB(im.createInstructsEntry(conn, courseId), conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void handleStreet(Connection conn, Scanner scr, int numStudents, int courseId) {
        String session = "AM";
        if (numStudents > 15) {
            session = "PM";
            if (numStudents > 30) {
                System.out.println("You entered a value greater than 30. Students will be capped at 30.");
                numStudents = 30;
            }
        }
        for (int i = 0; i < 4; i++) {
            InstructorModel im = new InstructorModel();
            if (i == 0) {
                System.out.println("Enter ID for street teacher:");

                im.course_id = courseId;
                im.instructor_id = scr.nextInt();
                im.in_session = session;
                im.instructor_role = "street_teacher";
                try {
                    updateDB(im.createInstructsEntry(conn, courseId), conn);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Enter ID for street coach #" + i);

                im.course_id = courseId;
                im.instructor_id = scr.nextInt();
                im.in_session = session;
                im.instructor_role = "street_teacher";
                try {
                    updateDB(im.createInstructsEntry(conn, courseId), conn);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addStudents(Connection conn, Scanner scr, int courseId, int numStudents) {

        boolean addingStudents = true;
        int j = 0;

        while (addingStudents && j < numStudents) {
            StudentModel sm = new StudentModel();

            System.out.println("Enter ID for Student #" + (j + 1));
            sm.student_id = scr.nextInt();

            System.out.println("Has the student completed payment for the course? (1/0)");
            sm.is_payment_completed = scr.nextInt();

            System.out.println("What is the student's written score?");
            sm.written_score = scr.nextInt();

            System.out.println("What is the students score for exercise #1?");
            sm.exercise_1_score = scr.nextInt();

            System.out.println("What is the students score for exercise #2?");
            sm.exercise_2_score = scr.nextInt();

            System.out.println("What is the students score for exercise #3?");
            sm.exercise_3_score = scr.nextInt();

            System.out.println("What is the students score for exercise #4?");
            sm.exercise_4_score = scr.nextInt();

            System.out.println("What is the students score for exercise #5?");
            sm.exercise_5_score = scr.nextInt();

            updateDB(sm.createEnrollmentEntry(conn, courseId), conn);
            j++;
        }
    }

    public void addCourse(Connection conn, Scanner scr, CourseModel cm) {
        PreparedStatement ps = null;
        PreparedStatement psdc = null;
        boolean duplicate = false;

        System.out.println("Enter course id:");
        cm.course_id = scr.nextInt();
        System.out.println("Enter course name:");
        cm.course_name = scr.next();
        System.out.println("Enter course description:");
        cm.course_description = scr.nextLine();
        System.out.print("Enter course date: (YYYY-MM-DD) \n");
        String _dateStr = scr.nextLine();
        cm.date = parseDate(_dateStr);
        System.out.print("Enter course cost: \n");
        cm.cost = scr.nextInt();
        System.out.print("Enter course type: (dirt/street)\n");
        cm.course_type = scr.next();

        // Check for Duplicate Entry
        String lookupStmt = "SELECT * FROM course WHERE course_id = ?";
        try {
            psdc = conn.prepareStatement(lookupStmt);
            psdc.setInt(1, cm.course_id);
            duplicate = checkDuplicate(psdc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (duplicate) {
            System.out.println("\nDUPLICATE ENTRY:: Course with provided ID already exists. Returning to menu.\n");
            return;
        }

        // Proceed with Insertion Flow
        String insertStmt = "INSERT INTO course VALUES (?, ?, ?, ?, ?, ?);";
        try {
            ps = conn.prepareStatement(insertStmt);

            ps.setInt(1, cm.course_id);
            ps.setString(2, cm.course_name);
            ps.setString(3, cm.course_description);
            ps.setDate(4, cm.date);
            ps.setInt(5, cm.cost);
            ps.setString(6, cm.course_type);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (updateDB(ps, conn)) {
                System.out.println("Successfully added course to DB");
            } else {
                System.out.println("Failed to add course to DB");
            }
            try {
                // Ensure Statement is Closed
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException se2) {
                se2.printStackTrace();
                System.out.println("Not all DB resources freed!");
            }
        }
    }

    // MODELS

    enum UpdateType {
        date("UPDATE course SET course_date = ? WHERE course_id = ?;"),
        paymentRecord("UPDATE enrolled_in SET is_payment_completed = ? WHERE student_id = ? AND course_id = ?;"),
        writtenScore("UPDATE enrolled_in SET written_score = ? WHERE student_id = ? AND course_id = ?;"),
        exerciseScore1("UPDATE enrolled_in SET exercise_1_score = ? WHERE student_id = ? AND course_id = ?;"),
        exerciseScore2("UPDATE enrolled_in SET exercise_2_score = ? WHERE student_id = ? AND course_id = ?;"),
        exerciseScore3("UPDATE enrolled_in SET exercise_3_score = ? WHERE student_id = ? AND course_id = ?;"),
        exerciseScore4("UPDATE enrolled_in SET exercise_4_score = ? WHERE student_id = ? AND course_id = ?;"),
        exerciseScore5("UPDATE enrolled_in SET exercise_5_score = ? WHERE student_id = ? AND course_id = ?;");

        private final String query;

        private UpdateType(String query) {
            this.query = query;
        }

        public String getQuery() {
            return this.query;
        }

        public PreparedStatement getPreparedStatement(Connection conn) {
            PreparedStatement _ps = null;
            try {
                _ps = conn.prepareStatement(this.query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return _ps;
        }
    }

    class InstructorModel {
        int instructor_id;
        int course_id;
        String in_session;
        String instructor_role;

        public PreparedStatement createInstructsEntry(Connection conn, int course_id) {
            PreparedStatement ps = null;
            String query = "INSERT INTO instructs VALUES (?, ?, ?, ?);";
            try {
                ps = conn.prepareStatement(query);
                ps.setInt(1, instructor_id);
                ps.setInt(2, course_id);
                ps.setString(3, in_session);
                ps.setString(4, instructor_role);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return ps;
        }
    }

    enum RangeType {
        dirt,
        street;
    }

    class StudentModel {
        int student_id;
        int is_payment_completed;
        int written_score;
        int exercise_1_score;
        int exercise_2_score;
        int exercise_3_score;
        int exercise_4_score;
        int exercise_5_score;

        public PreparedStatement createEnrollmentEntry(Connection conn, int course_id) {
            PreparedStatement ps = null;
            String query = "INSERT INTO enrolled_in VALUES (?,?,?,?,?,?,?,?,?);";
            try {
                ps = conn.prepareStatement(query);
                ps.setInt(1, course_id);
                ps.setInt(2, this.student_id);
                ps.setInt(3, this.is_payment_completed);
                ps.setInt(4, this.written_score);
                ps.setInt(5, this.exercise_1_score);
                ps.setInt(6, this.exercise_2_score);
                ps.setInt(7, this.exercise_3_score);
                ps.setInt(8, this.exercise_4_score);
                ps.setInt(9, this.exercise_5_score);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return ps;
        }
    }

    class CourseModel {
        int course_id;
        String course_name;
        String course_description;
        java.sql.Date date;
        int cost;
        String course_type;

        public PreparedStatement createEnrollmentEntry(Connection conn) {
            PreparedStatement ps = null;
            String query = "INSERT INTO course VALUES (?, ?, ?, ?, ?, ?);";
            try {
                ps = conn.prepareStatement(query);
                ps.setInt(1, this.course_id);
                ps.setString(2, this.course_name);
                ps.setString(3, this.course_description);
                ps.setDate(4, this.date);
                ps.setInt(5, this.cost);
                ps.setString(6, this.course_type);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return ps;
        }
    }
}
