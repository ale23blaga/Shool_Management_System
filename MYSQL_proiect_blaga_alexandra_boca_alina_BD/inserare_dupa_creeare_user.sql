DELIMITER $$

CREATE TRIGGER after_users_insert
AFTER INSERT ON Users
FOR EACH ROW
BEGIN
    DECLARE new_username VARCHAR(45);
    DECLARE username_exists INT DEFAULT 1;
    DECLARE suffix INT DEFAULT 1;

    -- useri inserati
    SET new_username = NEW.username;

    -- Verifica daca username ul exista deja sau nu
    SELECT COUNT(*) INTO username_exists
    FROM Users
    WHERE username = new_username;

    -- Daca exista ii adaugam un numar pana devine unic
    WHILE username_exists > 0 DO
        SET new_username = CONCAT(NEW.username, suffix);  
        -- Verificam iar daca user name ul exista
        SELECT COUNT(*) INTO username_exists
        FROM Users
        WHERE username = new_username;
        SET suffix = suffix + 1;  -- Increment suffix number
    END WHILE;

    INSERT INTO Users (CNP, Nume, Prenume, AdresaID, NrTelefon, Email, IBAN, ContractNumber, UserType, username, parola)
    VALUES (
        NEW.CNP, 
        NEW.Nume, 
        NEW.Prenume, 
        NEW.AdresaID, 
        NEW.NrTelefon, 
        NEW.Email, 
        NEW.IBAN, 
        NEW.ContractNumber, 
        NEW.UserType, 
        new_username,  -- Use generated unique username
        NEW.parola
    );

    -- Check if the new user is a student
    IF NEW.UserType = 'student' THEN
        INSERT INTO Student (StudentID, UserID, An_studiu, Nr_ore)
        VALUES (NEW.UserID, NEW.UserID, 1, 0);
        -- Explanation: 
        -- StudentID = NEW.UserID to match, An_studiu=1 (first year), Nr_ore=0 by default.
    ELSEIF NEW.UserType = 'professor' THEN
        INSERT INTO Profesor (ProfesorID, UserID, DepartamentID, Nr_min_ore, Nr_maxim_ore, Total_cursuri, Salariu)
        VALUES (NEW.UserID, NEW.UserID, 0, 10, 30, 0, 0.00);
        -- set everything to default values for professor
    END IF;
END $$

DELIMITER ;
