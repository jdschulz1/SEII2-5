package tabletopsDAO;
import java.util.List;

import javax.persistence.TypedQuery;

import tabletopsPD.Table;

public class TableDAO { 

    public static void saveTable(Table table) {
      EM.getEM().persist(table);
    }
    public static void addTable(Table table) {
      EM.getEM().persist(table);
    }

    public static List<Table> listTable()
    {
      TypedQuery<Table> query = EM.getEM().createQuery("SELECT table FROM table table", Table.class);
      return query.getResultList();
    }

    public static Table findTableById(int id)
    {
      Table table = EM.getEM().find(Table.class, new Integer(id));
      return table;
    }

    /**
     * @param table
     */
    public static void removeTable(Table table)
    {
      EM.getEM().remove(table);
    }
  }


