DELIMITER $$

CREATE TRIGGER UpdateStudentHoursOnDrop
BEFORE DELETE ON Inscrieri_curs
FOR EACH ROW
BEGIN
    UPDATE Student
    SET Nr_ore = Nr_ore - 2
    WHERE StudentID = OLD.StudentID;
END $$

DELIMITER ;

