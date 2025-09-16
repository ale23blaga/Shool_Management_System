DELIMITER $$

CREATE TRIGGER after_users_insert
AFTER INSERT ON Users
FOR EACH ROW
BEGIN
    -- Verificare daca user-ul nou e student
    IF NEW.UserType = 'student' THEN
        -- Inserare in tabela de studenti
        INSERT INTO Student (StudentID, UserID, An_studiu, Nr_ore)
        VALUES (NEW.UserID, NEW.UserID, 1, 0);  -- Anul implicit este 1, si numarul total de ore este tot 1

	-- Verificare daca user-ul nou e profesor
    ELSEIF NEW.UserType = 'professor' THEN
        -- Inserare in tabela cu profesori
        INSERT INTO Profesor (ProfesorID, UserID, DepartamentID, Nr_min_ore, Nr_maxim_ore, Total_cursuri, Salariu)
        VALUES (NEW.UserID, NEW.UserID, 10, 10, 30, 0, 0.00);  -- Valori default, dupa care in functie de schimbari se vor recalcula
    END IF;
END $$

DELIMITER ;

