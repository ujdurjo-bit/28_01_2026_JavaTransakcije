/*Napiši Java konzolnu aplikaciju koja rješava sljedeći problem.

Kod unosa stavki (tablica Stavka) zabunom je unesena cijena po komadu za stavke sa id-evima 8 i 9.
Cijenu po komadu za stavku sa id-em 8 treba povećati za 10, a cijenu po komadu stavke sa id-em 9 treba smanjiti za 10
Osigurati da se obje izmjene dogode u transakciji, tj ili se izvrše obje izmjene ili niti jedna
Ispisati vrijednost stavki prije i nakon promjene*/

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import javax.sql.DataSource;
import java.sql.*;


public class Zadatak1 {
    public static void main(String[] args) {
        DataSource dataSource = createDataSource();
        Savepoint savepoint = null;

        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Uspješno spajanje na bazu!");

            //try-catch blok za izvršavanje transakcije
            try (Statement stat1 = connection.createStatement();
                 Statement stat2 = connection.createStatement()) {
                connection.setAutoCommit(false); //isključujemo automatski commit transakcije

                ispisStavkiPrije(connection);

                stat1.executeUpdate("UPDATE Stavka SET CijenaPoKomadu = CijenaPoKomadu + 10 WHERE IdStavka = 8");
                System.out.println("Na stavku 8 je dodano +10");
//                savepoint = connection.setSavepoint("kontrolnatocka"); //kreiranje kontrolne točke

                stat2.executeUpdate("UPDATE Stavka SET CijenaPoKomadu = CijenaPoKomadu - 10 WHERE IdStavka = 9");
                System.out.println("Na stavku 9 je oduzeto -10");


                ispisStavkiNakon(connection);

                connection.commit();
                System.out.println("Transakcija izvršena!");
            } catch (SQLException e) {
                connection.rollback();
                System.err.println("Transakcija kompletno poništena!");

            }

        } catch (SQLException e) {
            System.err.println("Greška prilikom spajanja na bazu!");
            e.printStackTrace();

        }

    }

    public static void ispisStavkiPrije(Connection connection) {

        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery("SELECT IdStavka, CijenaPoKomadu FROM Stavka WHERE IdStavka IN (8, 9)")) {

            System.out.println("Ispis stavki prije promjene!");
            while (resultSet.next()) {
                System.out.println("\n ID: " + resultSet.getInt("IdStavka") +  ", Cijena: " + resultSet.getDouble("CijenaPoKomadu"));
            }

        } catch (SQLException e) {
            System.err.println("Greška pri ispisu stavki prije promjene!");
            e.printStackTrace();
        }

    }


    public static void ispisStavkiNakon(Connection connection) throws SQLException {

        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery("SELECT IdStavka, CijenaPoKomadu FROM Stavka WHERE IdStavka IN (8, 9)")) {

            System.out.println("Ispis stavki nakon promjene!");
            while (resultSet.next()) {
                System.out.println("\n ID: " + resultSet.getInt("IdStavka")  + ", Cijena: " + resultSet.getDouble("CijenaPoKomadu"));
            }

        } catch (SQLException e) {
            System.err.println("Greška pri ispisu stavki nakon promjene!");
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