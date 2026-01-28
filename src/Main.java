import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        DataSource dataSource = createDataSource();
        Savepoint savepoint = null;

        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Uspješno spajanje na bazu!");

            //try-catch blok za izvršavanje transakcije
            try (Statement stat1 = connection.createStatement(); Statement stat2 = connection.createStatement()) {
                connection.setAutoCommit(false); //isključujemo automatski commit transakcije
                stat1.executeUpdate("INSERT INTO Drzava (Naziv) VALUES ('Mađarska')");
                savepoint = connection.setSavepoint("kontrolnatocka"); //kreiranje kontrolne točke
                stat2.executeUpdate("UPDATE Drzava SET Naziv = 'Croatia' WHERE IDDrzava = "); //greška

                connection.commit();
                System.out.println("Transakcija izvršena!");
            } catch (SQLException e) {
//                System.err.println("Transakcija poništena!");
//                connection.rollback();

                System.err.println("Transakcija dijelom poništena!");
                try {
                    connection.rollback(savepoint);
                    connection.commit();
                    System.out.println("Transakcija vraćena na kontrolnu točku!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

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
