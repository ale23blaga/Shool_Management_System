DELIMITER $$

CREATE TRIGGER AssignDepartmentHead
AFTER INSERT ON Profesor
FOR EACH ROW
BEGIN
    -- Daca nu exista sef de departament, se va asigna primul profesor adaugat
    IF (SELECT Sef_departamentID FROM Departament WHERE ID_Departament = NEW.DepartamentID) IS NULL THEN
        UPDATE Departament
        SET Sef_departamentID = NEW.ProfesorID
        WHERE ID_Departament = NEW.DepartamentID;
    END IF;
END $$

DELIMITER ;
