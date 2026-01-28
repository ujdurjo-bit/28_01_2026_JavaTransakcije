import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        DataSource dS = createDataSource();

        try (Connection connection = dS.getConnection()) {
            System.out.println("Uspješno spajanje na bazu!");

            //try-catch blockza izvršavanje transakcije
            try (Statement stmnt1 = connection.createStatement();
                Statement stmnt2 = connection.createStatement()) {
                connection.setAutoCommit(false); //isključujemo automatski commit transakcije
                stmnt1.executeUpdate("INSERT INTO Drzava (Naziv) VALUES ('Indija')");
                stmnt2.executeUpdate("UPDATE Drzava SET Naziv = 'Croatia' WHERE IDDrzava = "); //greška

                connection.commit();
                System.out.println("Transakcija izvršena!");

            } catch (SQLException e) {
                System.err.println("Transakcija poništena!");
                connection.rollback();

            }






        } catch (SQLException e) {
            System.err.println("Greška prilikom spajanja na bazu!");
            e.printStackTrace();

        }


    }

    //metoda spajanje na bazu
    private static DataSource createDataSource() {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setServerName("localhost");
        ds.setDatabaseName("AdventureWorksOBP");
        ds.setUser("sa");
        ds.setPassword("SQL");
        ds.setEncrypt(false);
        return ds;

    }

}
