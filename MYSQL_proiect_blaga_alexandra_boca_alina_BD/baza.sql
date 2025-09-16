-- creare baza de date
CREATE DATABASE IF NOT EXISTS Proiect_PlatformaDeStudiu2;
USE Proiect_PlatformaDeStudiu2;

-- 2. creare TABLES

-- a) Adresa
CREATE TABLE Adresa (
  AdresaID INT AUTO_INCREMENT PRIMARY KEY,
  Tara VARCHAR(30),
  Judet VARCHAR(30),
  Oras VARCHAR(30),
  Strada VARCHAR(30),
  Numar VARCHAR(5)
);

-- b) Users
CREATE TABLE Users (
  UserID INT AUTO_INCREMENT PRIMARY KEY,
  CNP VARCHAR(13) NOT NULL,
  Nume VARCHAR(30) NOT NULL,
  Prenume VARCHAR(30) NOT NULL,
  AdresaID INT NOT NULL,
  NrTelefon VARCHAR(10),
  Email VARCHAR(50),
  IBAN VARCHAR(34),
  ContractNumber VARCHAR(20),
  UserType ENUM('student', 'professor', 'administrator', 'super-administrator') NOT NULL,
  username VARCHAR(45),
  parola VARCHAR(45),
  INDEX username_UNIQUE (username ASC)
);

-- c) Departament
CREATE TABLE Departament(
  ID_Departament INT AUTO_INCREMENT PRIMARY KEY,
  Nume_dep VARCHAR(25),
  Sef_departamentID INT
);

-- d) Profesor
CREATE TABLE Profesor(
  ProfesorID INT AUTO_INCREMENT PRIMARY KEY,
  UserID INT NOT NULL,
  DepartamentID INT,
  Nr_min_ore INT,
  Nr_maxim_ore INT,
  Total_cursuri INT,
  Salariu DECIMAL(5,2)
);

-- e) Curs
CREATE TABLE Curs(
  CursID INT AUTO_INCREMENT PRIMARY KEY,
  Nume_curs VARCHAR(30),
  Descriere TEXT,
  Nr_maxim_studenti INT
);

-- f) Student
CREATE TABLE Student(
  StudentID INT AUTO_INCREMENT PRIMARY KEY,
  UserID INT NOT NULL,
  An_studiu INT,
  Nr_ore INT
);

-- g) Activitati_Curs
CREATE TABLE Activitati_Curs(
  ActivitateID INT AUTO_INCREMENT PRIMARY KEY,
  Tip_activitate ENUM('CURS','SEMINAR','LABORATOR'),
  ProfesorID INT NOT NULL,
  CursID INT NOT NULL,
  Procentaj INT,
  StartDate DATE,
  EndDate DATE,
  Recurenta ENUM('saptamanal','o data la doua saptamani','lunar') NOT NULL,
  max_participanti INT
);

-- h) Catalog
CREATE TABLE Catalog(
  intrareID INT AUTO_INCREMENT PRIMARY KEY,
  ProfesorID INT NOT NULL,
  StudentID INT NOT NULL,
  CursID INT NOT NULL,
  Nota INT,
  ProcentajID INT NOT NULL,
  prezenta BOOLEAN DEFAULT 1,
  DataA DATE
);

-- i) Inscrieri_curs
CREATE TABLE Inscrieri_curs(
  InscriereID INT AUTO_INCREMENT PRIMARY KEY,
  StudentID INT NOT NULL,
  CursID INT NOT NULL,
  Data_Inscriere DATE NOT NULL
);

-- j) Grup_Studiu
CREATE TABLE Grup_Studiu(
  GrupID INT PRIMARY KEY,
  CursID INT UNIQUE,
  min_participanti INT
);

-- k) MembruGrup
CREATE TABLE MembruGrup(
  ID_Membru INT  AUTO_INCREMENT PRIMARY KEY,
  GrupID INT,
  StudentID INT 
);

-- l) Profesor_Curs
CREATE TABLE Profesor_Curs(
  Profesor_cursID INT AUTO_INCREMENT PRIMARY KEY,
  ProfesorID INT NOT NULL,
  CursID INT NOT NULL
);

-- a) Foreign Key pt Users -> Adresa
ALTER TABLE Users
  ADD CONSTRAINT fk_Adresa_user 
  FOREIGN KEY (AdresaID) REFERENCES Adresa(AdresaID)
  ON DELETE CASCADE ON UPDATE CASCADE;

-- b) Foreign Keys pt Profesor
ALTER TABLE Profesor
  ADD CONSTRAINT FK_ProfesorUser 
  FOREIGN KEY (UserID) REFERENCES Users(UserID)
    ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT fk_ProfesorDepartament 
  FOREIGN KEY (DepartamentID) REFERENCES Departament(ID_Departament)
    ON DELETE CASCADE ON UPDATE CASCADE;

-- c) Foreign Key pt Student
ALTER TABLE Student
  ADD CONSTRAINT fk_StudentUser 
  FOREIGN KEY (UserID) REFERENCES Users(UserID)
    ON DELETE CASCADE ON UPDATE CASCADE;

-- d) Foreign Keys pt Catalog
ALTER TABLE Catalog
  ADD CONSTRAINT FK_CatalogProfesor 
  FOREIGN KEY (ProfesorID) REFERENCES Profesor(ProfesorID)
    ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT FK_CatalogStudent 
  FOREIGN KEY (StudentID) REFERENCES Student(StudentID)
    ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT FK_CatalogCurs 
  FOREIGN KEY (CursID) REFERENCES Curs(CursID)
    ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT fk_CatalogProcentaj 
  FOREIGN KEY (ProcentajID) REFERENCES Activitati_Curs(ActivitateID)
    ON DELETE CASCADE ON UPDATE CASCADE;

-- e) Foreign Keys pt Activitati_Curs
ALTER TABLE Activitati_Curs
  ADD CONSTRAINT FK_ActivitatiCurs 
  FOREIGN KEY (CursID) REFERENCES Curs(CursID)
    ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT FK_ActivitatiProfesor
  FOREIGN KEY (ProfesorID) REFERENCES Profesor(ProfesorID)
    ON DELETE CASCADE ON UPDATE CASCADE;

-- f) Foreign Keys pt Inscrieri_curs
ALTER TABLE Inscrieri_curs
  ADD CONSTRAINT FK_InscriereStudent 
  FOREIGN KEY (StudentID) REFERENCES Student(StudentID)
    ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT FK_InscriereCurs 
  FOREIGN KEY (CursID) REFERENCES Curs(CursID)
    ON DELETE CASCADE ON UPDATE CASCADE;

-- g) Foreign Keys pt Grup_Studiu
ALTER TABLE Grup_Studiu
  ADD CONSTRAINT FK_GrupStudiuCurs 
  FOREIGN KEY (CursID) REFERENCES Curs(CursID)
    ON DELETE CASCADE ON UPDATE CASCADE;

-- h) Foreign Keys pt MembruGrup
ALTER TABLE MembruGrup
  ADD CONSTRAINT FK_MembruGrupGrup 
  FOREIGN KEY (GrupID) REFERENCES Grup_Studiu(GrupID)
    ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT FK_MembruGrupStudent 
  FOREIGN KEY (StudentID) REFERENCES Student(StudentID)
    ON DELETE CASCADE ON UPDATE CASCADE;

-- i) Foreign Keys pt Profesor_Curs
ALTER TABLE Profesor_Curs
  ADD CONSTRAINT FK_ProfesorCursProfesor 
  FOREIGN KEY (ProfesorID) REFERENCES Profesor(ProfesorID)
    ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT FK_ProfesorCursCurs 
  FOREIGN KEY (CursID) REFERENCES Curs(CursID)
    ON DELETE CASCADE ON UPDATE CASCADE;

-- j) Foreign Key pt Departament -> Sef_departamentID
ALTER TABLE Departament
  ADD CONSTRAINT fk_SefDepartament 
  FOREIGN KEY (Sef_departamentID) REFERENCES Profesor(ProfesorID)
    ON DELETE CASCADE ON UPDATE CASCADE;



CREATE TABLE IF NOT EXISTS GroupMessages (
  MessageID INT AUTO_INCREMENT PRIMARY KEY,
  GrupID INT NOT NULL,
  StudentID INT NOT NULL,
  Content TEXT NOT NULL,
  Timestamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

  -- foreign keys
  CONSTRAINT FK_GroupMessageGroup 
    FOREIGN KEY (GrupID) REFERENCES Grup_Studiu(GrupID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,

  CONSTRAINT FK_GroupMessageStudent
    FOREIGN KEY (StudentID) REFERENCES Student(StudentID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

INSERT INTO Adresa (Tara, Judet, Oras, Strada, Numar)
VALUES 
  ('Romania', 'Iasi', 'Iasi', 'Lapusneanu', '10'),
  ('Romania', 'Cluj', 'Cluj-Napoca', 'Unirii', '21'),
  ('Romania', 'Bucuresti', 'Bucuresti', 'Victoriei', '45');
  INSERT INTO Users (
  UserID, 
  CNP, 
  Nume, 
  Prenume, 
  AdresaID, 
  NrTelefon, 
  Email, 
  IBAN, 
  ContractNumber, 
  UserType, 
  username, 
  parola
)
VALUES
-- Administrator user
(1, '1980101123456', 'Popescu', 'Ion', 1, '0745123456', 'ion.popescu@example.com', 'RO09INGB0000999900000001', 'ABC123', 'administrator', 'admin', 'admin123'),

-- Professor user
(2, '1890709123456', 'Ionescu', 'Maria', 2, '0756123456', 'maria.ionescu@example.com', 'RO49AAAA1B31007593840000', 'DEF456', 'professor', 'prof_maria', 'maria123'),

-- Student user
(3, '5000000000001', 'Pop', 'Andrei', 3, '0722000000', 'andrei.pop@example.com', 'RO77AAAA1B31007593840001', 'STU789', 'student', 'stud_andrei', 'andrei123'),

-- Another Student user
(4, '6000000000002', 'Georgescu', 'Alex', 3, '0733000001', 'alex.georgescu@example.com', 'RO88AAAA1B31007593840002', 'STU790', 'student', 'stud_alex', 'alex123');


  INSERT INTO Users (
  UserID, 
  CNP, 
  Nume, 
  Prenume, 
  AdresaID, 
  NrTelefon, 
  Email, 
  IBAN, 
  ContractNumber, 
  UserType, 
  username, 
  parola
)
VALUES
(5, '6000000000002', 'Blaga', 'Angela', 3, '9728282', 'Mami@example.com', 'Ro8888888', 'SUP88', 'super-administrator', 'super', 'super123'); 

INSERT INTO Departament (ID_Departament, Nume_dep, Sef_departamentID)
VALUES 
  (10, 'Matematica', NULL),
  (20, 'Informatica', NULL),
  (30, 'Fizica', NULL);

INSERT INTO Profesor (
  ProfesorID, 
  UserID, 
  DepartamentID, 
  Nr_min_ore, 
  Nr_maxim_ore, 
  Total_cursuri, 
  Salariu
)
VALUES
(2, 2, 20, 10, 20, 5, 400.00);

INSERT INTO Student (StudentID, UserID, An_studiu, Nr_ore)
VALUES
(3, 3, 2, 30),
(4, 4, 1, 25);

INSERT INTO Curs (CursID, Nume_curs, Descriere, Nr_maxim_studenti)
VALUES
(300, 'Programare in Java', 'Curs de baza pentru Java', 50),
(301, 'Analiza Matematica', 'Curs de analiza', 100),
(302, 'Structuri de Date', 'Curs avansat de structuri de date', 60);

INSERT INTO Activitati_Curs (
  ActivitateID,
  Tip_activitate,
  ProfesorID,
  CursID,
  Procentaj,
  StartDate,
  EndDate,
  Recurenta,
  max_participanti
)
VALUES
(400, 'CURS', 2, 300, 25, '2025-02-01', '2025-06-01', 'saptamanal', 50),
(401, 'SEMINAR', 2, 300, 25, '2025-02-01', '2025-06-01', 'saptamanal', 25),
(402, 'LABORATOR', 2, 300, 50, '2025-02-01', '2025-06-01', 'saptamanal', 25);

INSERT INTO Catalog (
  intrareID,
  ProfesorID,
  StudentID,
  CursID,
  Nota,
  ProcentajID,
  prezenta,
  DataA
)
VALUES
(500, 2, 3, 300, 8, 400, 1, '2025-03-15'),
(501, 2, 4, 300, 9, 400, 1, '2025-03-15');

INSERT INTO Inscrieri_curs (
  InscriereID,
  StudentID,
  CursID,
  Data_Inscriere
)
VALUES
(600, 3, 300, '2025-01-20'),
(601, 4, 300, '2025-01-21');
INSERT INTO Grup_Studiu (
  GrupID,
  CursID,
  min_participanti
)
VALUES
(700, 300, 2);

INSERT INTO MembruGrup (
  GrupID,
  StudentID
)
VALUES
(700, 3), -- Andrei Pop
(700, 4); -- Alex Georgescu


INSERT INTO Profesor_Curs (
  Profesor_cursID,
  ProfesorID,
  CursID
)
VALUES
(800, 2, 300);
