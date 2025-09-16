create database Proiect_PlatformaDeStudiu;
use Proiect_PlatformaDeStudiu;

create table Users(
UserID int unique primary key,
CNP varchar(13) not null,
Nume varchar(30) not null,
Prenume varchar(30) not null,
AdresaId int not null,
NrTelefon varchar(10),
Email varchar(50),
IBAN varchar(34), 
ContractNumber VARCHAR(20),
UserType ENUM('student', 'professor', 'administrator', 'super-administrator') NOT NULL
 -- adaugte file contract, tipuri de user
);

create table Adresa(
AdresaID int auto_increment primary key,
Tara varchar(30),
Judet varchar (30),
oras varchar(30),
strada varchar(30),
numar varchar(5)
-- intrebare? facem pe generic sau cum ii chestionarul de adresa de la boltFood?
);

create table Profesor(
ProfesorID int primary key,
UserID int not null,
DepartamentID int,
Nr_min_ore int,
Nr_maxim_ore int,
Total_cursuri int,
Salariu decimal(5,2)
);

create table Departament(
ID_Departament int unique primary key,
Nume_dep varchar(25), 
Sef_departamentID int
);

create table Curs(
CursID int unique primary key,
Nume_curs varchar(30),
Descriere text,
Nr_maxim_studenti int
);

create table Activitati_Curs(
ActivitateID int unique primary key,
Tip_activitate enum("CURS", "SEMINAR", "LABORATOR"), 
ProfesorID int not null,
CursID int not null,
Procentaj int, -- adica 25 care va fi la 100
StartDate date,
EndDate date,
Recurenta enum ('saptamanal', 'o data la doua saptamani', 'lunar') not null,
max_participanti int
);

create table Catalog(
intrareID int unique primary key,
ProfesorID int not null, 
StudentID int not null,
CursID int not null,
Nota int,
ProcentajID int not null,
prezenta boolean default 1,
DataA date
);

create table Student(
StudentID int unique primary key,
UserID int not null,
An_studiu int,
Nr_ore int
);

create table MembruGrup
(
ID_Membru int unique auto_increment primary key,
GrupID int,
StudentID int unique
);

create table Grup_Studiu(
GrupID int primary key,
CursID int unique,
min_participanti int
);

create table Inscrieri_curs(
InscriereID int unique primary key,
StudentID int not null,
CursID int not null, 
Data_Inscriere date not null
);

create table Profesor_Curs(
Profesor_cursID int unique primary key,
ProfesorID int not null,
CursID INT not null
);

#chei straine
#profesor
alter table Profesor
add constraint FK_ProfesorUser foreign key (UserID) references Users(UserID)
	on delete cascade on update cascade,
add constraint fk_ProfesorDepartament foreign key (DepartamentID) references Departament(ID_Departament)
	on delete cascade on update cascade;
    
#student
alter table student 
add constraint fk_StudentUser foreign key (UserID) references users(UserID)
	on delete cascade on update cascade;
    
#Catalog
ALTER TABLE Catalog 
ADD CONSTRAINT FK_CatalogProfesor FOREIGN KEY (ProfesorID) REFERENCES Profesor(ProfesorID) 
    ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT FK_CatalogStudent FOREIGN KEY (StudentID) REFERENCES Student(StudentID) 
    ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT FK_CatalogCurs FOREIGN KEY (CursID) REFERENCES Curs(CursID) 
    ON DELETE CASCADE ON UPDATE CASCADE,
Add constraint fk_CatlaogProcentaj foreign key (ProcentajID) references Activitati_curs(ActivitateID)
	ON DELETE CASCADE ON UPDATE CASCADE;

#activitati-curs
ALTER TABLE Activitati_Curs 
ADD CONSTRAINT FK_ActivitatiCurs FOREIGN KEY (CursID) REFERENCES Curs(CursID) 
    ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT FK_ActivitatiProfesor foreign key (ProfesorID) references Profesor(ProfesorID);

#inscrieri curs
ALTER TABLE Inscrieri_curs 
ADD CONSTRAINT FK_InscriereStudent FOREIGN KEY (StudentID) REFERENCES Student(StudentID) 
    ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT FK_InscriereCurs FOREIGN KEY (CursID) REFERENCES Curs(CursID) 
    ON DELETE CASCADE ON UPDATE CASCADE;

#grup studiu
ALTER TABLE Grup_Studiu 
ADD CONSTRAINT FK_GrupStudiuCurs FOREIGN KEY (CursID) REFERENCES Curs(CursID) 
    ON DELETE CASCADE ON UPDATE CASCADE;

#Membru Grup
ALTER TABLE MembruGrup 
ADD CONSTRAINT FK_MembruGrupGrup FOREIGN KEY (GrupID) REFERENCES Grup_Studiu(GrupID) 
    ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT FK_MembruGrupStudent FOREIGN KEY (StudentID) REFERENCES Student(StudentID) 
    ON DELETE CASCADE ON UPDATE CASCADE;

#Profesor-curs
ALTER TABLE Profesor_Curs 
ADD CONSTRAINT FK_ProfesorCursProfesor FOREIGN KEY (ProfesorID) REFERENCES Profesor(ProfesorID) 
    ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT FK_ProfesorCursCurs FOREIGN KEY (CursID) REFERENCES Curs(CursID) 
    ON DELETE CASCADE ON UPDATE CASCADE;

#Sef departament
Alter table departament 
add constraint fk_SefDepartament foreign key (Sef_departamentID) references profesor(ProfesorID)
	ON DELETE CASCADE ON UPDATE CASCADE;
    

#adresa
alter table users
add constraint fk_Adresa_user foreign key (AdresaID) references Adresa(AdresaID)
	ON DELETE CASCADE ON UPDATE CASCADE;
    
    ALTER TABLE `proiect_platformadestudiu`.`users` 
ADD COLUMN `username` VARCHAR(45) NULL AFTER `UserID`,
ADD COLUMN `parola` VARCHAR(45) NULL AFTER `username`,
ADD UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE;
;
