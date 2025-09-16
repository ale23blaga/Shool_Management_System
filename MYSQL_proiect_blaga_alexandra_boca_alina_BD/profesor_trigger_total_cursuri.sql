#Update Profesor Total Courses
DELIMITER $$

CREATE TRIGGER UpdateProfessorTotalCourses
AFTER INSERT ON Profesor_Curs
FOR EACH ROW
BEGIN
    -- Crestem numarul total de cursuri la care preda profesorul
    UPDATE Profesor
    SET Total_cursuri = Total_cursuri + 1
    WHERE ProfesorID = NEW.ProfesorID;
END $$

DELIMITER ;
