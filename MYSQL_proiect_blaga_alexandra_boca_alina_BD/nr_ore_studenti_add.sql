DELIMITER $$

CREATE TRIGGER UpdateStudentHoursOnEnrollment
AFTER INSERT ON Inscrieri_curs
FOR EACH ROW
BEGIN
    -- Pentru fiecare Curs la care se inscrie Nr de ore realizate de student creste
    UPDATE Student
    SET Nr_ore = Nr_ore + 2
    WHERE StudentID = NEW.StudentID;
END $$

DELIMITER ;

