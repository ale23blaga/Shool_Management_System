package school.management.system.dao;

import school.management.system.models.CatalogEntry;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CatalogDAO {

    // CREATE
    public void addCatalogEntry(CatalogEntry entry) {
        String sql = "INSERT INTO Catalog (intrareID, ProfesorID, StudentID, CursID, Nota, ProcentajID, prezenta, DataA) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, entry.getIntrareID());
            stmt.setInt(2, entry.getProfesorID());
            stmt.setInt(3, entry.getStudentID());
            stmt.setInt(4, entry.getCursID());
            stmt.setInt(5, entry.getNota());
            stmt.setInt(6, entry.getProcentajID());
            stmt.setBoolean(7, entry.isPrezenta());
            stmt.setDate(8, entry.getDataA());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ ALL
    public List<CatalogEntry> getAllCatalogEntries() {
        List<CatalogEntry> list = new ArrayList<>();
        String sql = "SELECT * FROM Catalog";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new CatalogEntry(
                        rs.getInt("intrareID"),
                        rs.getInt("ProfesorID"),
                        rs.getInt("StudentID"),
                        rs.getInt("CursID"),
                        rs.getInt("Nota"),
                        rs.getInt("ProcentajID"),
                        rs.getBoolean("prezenta"),
                        rs.getDate("DataA")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // UPDATE
    public void updateCatalogEntry(CatalogEntry entry) {
        String sql = "UPDATE Catalog SET ProfesorID=?, StudentID=?, CursID=?, Nota=?, ProcentajID=?, prezenta=?, DataA=? "
                + "WHERE intrareID=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, entry.getProfesorID());
            stmt.setInt(2, entry.getStudentID());
            stmt.setInt(3, entry.getCursID());
            stmt.setInt(4, entry.getNota());
            stmt.setInt(5, entry.getProcentajID());
            stmt.setBoolean(6, entry.isPrezenta());
            stmt.setDate(7, entry.getDataA());
            stmt.setInt(8, entry.getIntrareID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteCatalogEntry(int intrareID) {
        String sql = "DELETE FROM Catalog WHERE intrareID=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, intrareID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Note student din db
    public List<CatalogEntry> getGradesForStudent(int studentID) {
        List<CatalogEntry> gradesList = new ArrayList<>();

        String sql = "SELECT * FROM Catalog WHERE StudentID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int intrareID = rs.getInt("intrareID");
                    int profesorID = rs.getInt("ProfesorID");
                    int cursID = rs.getInt("CursID");
                    int nota = rs.getInt("Nota");
                    int procentajID = rs.getInt("ProcentajID");
                    boolean prezenta = rs.getBoolean("prezenta");
                    Date dataA = rs.getDate("DataA");

                    CatalogEntry entry = new CatalogEntry(
                            intrareID,
                            profesorID,
                            studentID,
                            cursID,
                            nota,
                            procentajID,
                            prezenta,
                            dataA
                    );
                    gradesList.add(entry);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gradesList;
    }

    public List<CatalogEntry> getCatalogEntriesByCourse(int cursID) {
        List<CatalogEntry> list = new ArrayList<>();
        String sql = "SELECT * FROM Catalog WHERE CursID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cursID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractCatalogEntry(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    //Fiecare nota adugata in catalog
    public CatalogEntry getCatalogEntry(int intrareID) {
        CatalogEntry c = null;
        String sql = "SELECT * FROM Catalog WHERE IntrareID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, intrareID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    c = extractCatalogEntry(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    private CatalogEntry extractCatalogEntry(ResultSet rs) throws SQLException {
        int intrareID = rs.getInt("intrareID");
        int profesorID = rs.getInt("ProfesorID");
        int studentID = rs.getInt("StudentID");
        int cursID = rs.getInt("CursID");
        int nota = rs.getInt("Nota");
        int procentajID = rs.getInt("ProcentajID");
        boolean prezenta = rs.getBoolean("prezenta");
        Date dataA = rs.getDate("DataA");

        return new CatalogEntry(
                intrareID, profesorID, studentID,
                cursID, nota, procentajID, prezenta, dataA
        );
    }

    public List<CatalogEntry> getCatalogEntriesByProfessor(int profesorID) {

        List<CatalogEntry> list = new ArrayList<>();
        String sql = "SELECT cat.*\n" +
                "FROM Catalog cat\n" +
                "JOIN Curs c ON cat.CursID = c.CursID\n" +
                "JOIN Activitati_Curs ac ON c.CursID = ac.CursID\n" +
                "WHERE ac.ProfesorID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, profesorID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractCatalogEntry(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
