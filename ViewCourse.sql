SELECT c.course_id, i.instructor_id, s.student_id, t.classroom_id, u.range_id, t.in_session AS classroom_session, u.in_session AS range_session, c.course_date
FROM course c 
LEFT JOIN instructs i ON c.course_id = i.course_id
LEFT JOIN enrolled_in s ON s.course_id = c.course_id 
LEFT JOIN taught_in t ON t.course_id = s.course_id 
LEFT JOIN uses u ON c.course_id = u.course_id;

SELECT c.course_id, u.range_id, u.in_session
FROM course c
LEFT JOIN uses u ON c.course_id = u.course_id;