#doua proceduri
#select = proiectie imi iau doar ceva pi
#where = selectie sigma
DELIMITER $$

CREATE PROCEDURE EnrollStudentInCourse(
    IN p_StudentID INT,
    IN p_CursID INT,
    IN p_DataInscriere DATE
)
BEGIN
    DECLARE v_max_students INT;
    DECLARE v_current_students INT;

    -- Check the course capacity
    SELECT Nr_maxim_studenti INTO v_max_students
    FROM Curs
    WHERE CursID = p_CursID;

    SELECT COUNT(*) INTO v_current_students
    FROM Inscrieri_curs
    WHERE CursID = p_CursID;

    IF v_current_students >= v_max_students THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Course is already full.';
    ELSE
        -- Insert the enrollment record
        INSERT INTO Inscrieri_curs (StudentID, CursID, Data_Inscriere)
        VALUES (p_StudentID, p_CursID, p_DataInscriere);
    END IF;
END$$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE AssignProfessorToCourse(
    IN p_ProfesorID INT,
    IN p_CursID INT
)
BEGIN
    DECLARE v_max_hours INT;
    DECLARE v_assigned_hours INT;

    -- Check the professor's maximum allowed hours
    SELECT Nr_maxim_ore INTO v_max_hours
    FROM Profesor
    WHERE ProfesorID = p_ProfesorID;

    -- Calculate the current assigned hours
    SELECT SUM(ac.Procentaj) INTO v_assigned_hours
    FROM Activitati_Curs ac
    WHERE ac.ProfesorID = p_ProfesorID;

    IF v_assigned_hours >= v_max_hours THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Professor cannot take on more hours.';
    ELSE
        -- Assign the professor to the course
        INSERT INTO Profesor_Curs (ProfesorID, CursID)
        VALUES (p_ProfesorID, p_CursID);
    END IF;
END$$

DELIMITER ;
