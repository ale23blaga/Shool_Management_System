Sistem de Management al Studenților
Un sistem complet integrat de management al studenților, creat pentru a gestiona studenții, profesorii, cursurile, înscrierile, grupurile de studiu și diferite tipuri de utilizatori. Acest sistem permite utilizatorilor de diferite tipuri să interacționeze cu o bază de date pentru a efectua diverse acțiuni în funcție de rolurile lor (Admin, Profesor, Student).

Prezentare generală a proiectului
Sistemul de management al studenților este o platformă care permite studenților să se înscrie la cursuri, profesorilor să gestioneze activitățile cursurilor, și administratorilor să gestioneze utilizatorii și cursurile. Sistemul utilizează o bază de date MySQL pentru stocarea informațiilor despre utilizatori, cursuri, înscriere și grupuri de studiu. Aplicația are o interfață grafică de utilizator (GUI) construită folosind Java Swing pentru a oferi o metodă intuitivă de interacțiune cu sistemul.

Funcționalități principale
Pentru Studenți:

Înscriere la cursuri: Studenții pot căuta și se pot înscrie la cursuri.
Managementul grupurilor: Studenții pot să se alăture sau să părăsească grupuri de studiu asociate cursurilor la care sunt înscriși.
Vizualizarea notelor: Studenții pot vizualiza notele lor pentru diferite activități din cursurile la care sunt înscriși.
Sistem de mesagerie: Studenții pot trimite mesaje altor studenți din cadrul unui grup de studiu.
Dezinscriere de la cursuri: Studenții se pot dezinscrie de la cursurile la care nu mai sunt interesați.
Pentru Profesori:

Managementul activităților: Profesorii pot crea și modifica activitățile cursurilor (de exemplu, seminarii, laboratoare și cursuri).
Managementul notelor: Profesorii pot acorda și edita notele studenților.
Descărcarea notelor: Profesorii pot descărca notele studenților pentru diferite activități.
Listarea cursurilor: Profesorii pot vizualiza și gestiona cursurile pe care le predau.
Pentru Admin:

Managementul utilizatorilor: Adminii pot adăuga, edita sau șterge utilizatori (studenți, profesori, admini).
Managementul cursurilor: Adminii pot crea, edita și șterge cursuri.
Gestionarea înscrierilor: Adminii pot vizualiza și șterge înscrierile studenților.
Pentru Super Admin:

Managementul adminilor: Super adminii pot gestiona utilizatorii de tip admin, inclusiv adăugarea, editarea și ștergerea acestora.
Structura bazei de date
Aplicația este alimentată de o bază de date MySQL cu următoarele tabele cheie:

Users: Stochează informații pentru toți utilizatorii (studenți, profesori, admini, super-admini).
Professors: Stochează datele legate de profesori, inclusiv departamentul și programul lor de predare.
Students: Stochează informațiile legate de studenți, inclusiv cursurile la care sunt înscriși și notele lor.
Courses: Stochează informații despre cursuri și detalii asociate.
Enrollments: Urmărește înscrierea studenților la cursuri.
Study Groups: Permite studenților să se alăture grupurilor de studiu legate de cursurile lor.
Grades: Urmărește notele acordate studenților pentru diferite activități.
Tehnologii utilizate
Java: Limbajul principal de programare pentru logica de backend.
Java Swing: Framework-ul GUI pentru construirea interfeței utilizatorului.
MySQL: Baza de date pentru stocarea datelor legate de utilizatori și cursuri.
JDBC: Java Database Connectivity utilizat pentru interacțiuni cu baza de date.
Maven/Gradle: Instrumente de gestionare a dependențelor și automatizare a construcției (în funcție de configurația ta).
Cum să rulezi proiectul
Precondiții
Java: Asigură-te că ai Java instalat pe sistemul tău. Proiectul necesită JDK 8 sau mai mare.
MySQL: Proiectul folosește MySQL pentru gestionarea bazei de date. Asigură-te că MySQL este instalat și rulează.
IDE: Poți utiliza orice IDE pentru dezvoltarea Java, cum ar fi IntelliJ IDEA, Eclipse sau NetBeans.
Setarea bazei de date
Creează baza de date: Înainte de a rula proiectul, trebuie să creezi baza de date și tabelele sale. Poți folosi scripturile SQL furnizate pentru a configura tabelele necesare.
Nume baza de date: Proiect_PlatformaDeStudiu2
Rulează scripturile SQL furnizate pentru a configura toate tabelele și relațiile necesare.
Configurează conexiunea la baza de date: În clasa DatabaseConnection, asigură-te că conexiunea la baza de date este configurată corect. Furnizează numele de utilizator, parola și URL-ul bazei de date.

Baza de date constă din mai multe tabele, inclusiv:

Users: Stochează detalii pentru toți utilizatorii.
Professors: Stochează informații specifice profesorilor.
Students: Stochează informații specifice studenților.
Courses: Stochează cursurile disponibile pentru înscriere.
Enrollments: Urmărește înscrierea studenților la cursuri.
Study Groups: Permite studenților să se alăture grupurilor de studiu.
Catalog: Stochează notele și informațiile legate de activitățile studenților.
Funcționalități:
Managementul rolurilor utilizatorilor: Adminii pot gestiona utilizatorii, profesorii și studenții, adăugând, editând și ștergând utilizatori după cum este necesar.
Înscriere la cursuri: Studenții se pot înscrie sau dezinscrie de la cursuri, și pot vizualiza notele lor.
Sistem de mesagerie: Studenții dintr-un grup de studiu pot trimite și primi mesaje între ei.
Managementul grupurilor: Profesorii și studenții pot gestiona grupurile de studiu pentru fiecare curs.
Managementul activităților: Profesorii pot crea, edita și gestiona activitățile cursurilor (de exemplu, cursuri, seminarii, laboratoare).
Managementul notelor: Profesorii pot acorda note studenților pentru diverse activități.
Probleme cunoscute:
Redimensionarea UI: Aplicația ar putea necesita ajustări suplimentare pentru o redimensionare mai bună pe diferite dimensiuni de ecran.
Concurența: Anumite operațiuni, cum ar fi editarea simultană a aceleași informații ale unui curs sau student, ar putea necesita o gestionare suplimentară a concurenței.
Îmbunătățiri viitoare:
Validare îmbunătățită: Ar putea fi adăugată o validare mai detaliată a inputurilor și gestionarea erorilor.
Îmbunătățiri la mesagerie: Mesaje în timp real și notificări pentru studenții din grupuri.
Suport multi-limbă: Adăugarea unui suport pentru mai multe limbi pentru a face aplicația mai ușor de utilizat.s
