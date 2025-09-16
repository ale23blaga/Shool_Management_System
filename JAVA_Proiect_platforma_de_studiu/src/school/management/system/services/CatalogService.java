package school.management.system.services;

import school.management.system.dao.CatalogDAO;
import school.management.system.models.CatalogEntry;
import java.util.List;

public class CatalogService {
    private CatalogDAO catalogDAO = new CatalogDAO();

    public void addCatalogEntry(CatalogEntry entry) {
        catalogDAO.addCatalogEntry(entry);
    }

    public List<CatalogEntry> getAllEntries() {
        return catalogDAO.getAllCatalogEntries();
    }

    public void updateCatalogEntry(CatalogEntry entry) {
        catalogDAO.updateCatalogEntry(entry);
    }

    public void deleteCatalogEntry(int id) {
        catalogDAO.deleteCatalogEntry(id);
    }
}

