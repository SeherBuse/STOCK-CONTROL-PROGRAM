package pkg201735044;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class VeriTabani {

    static Connection con;
    static Statement stmt;
    String vtAd;
//Veri tabanı oluşturuldu ve Generic Collections'dan HashSet kullanıldı.    
    Set<Urun> urunSet;
    
    VeriTabani() {
        vtAd = "StokKontroll.db";
        urunSet=new HashSet<>();
        /*veritabanı yoksa oluşturur varsa bağlanır*/
        try {
            yeniVtOlustur(vtAd);
        } catch (Exception e) {
            System.out.println(e);
        }

    }
//Veri tabanı oluşturur ve java ile bağlantısını sağlar
    public void yeniVtOlustur(String dosyaadi) throws ClassNotFoundException {
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:StokKontroll.db");
            stmt = con.createStatement();
            String sql = ("CREATE TABLE if not exists StokKontroll "
                    + "(UrunBarkodNo INT PRIMARY KEY NOT NULL, "
                    + "UrunAd CHAR(50) NOT NULL, "
                    + "UrunAdedi INT NOT NULL,"
                    + "UrunFiyati INT NOT NULL)");
            stmt.executeUpdate(sql);
            System.out.println("Veritabanı Oluşturma Başarılı");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
//Menü oluşturur
    public void islemYap() {

        Scanner scan = new Scanner(System.in, "iso-8859-9");
        int secim;

        while (true) {
            System.out.println("*************");
            System.out.println("1.list");
            System.out.println("2.Add");
            System.out.println("3.Update");
            System.out.println("4.Delete");
            System.out.println("5.Exit");
            System.out.print("Your choice:");
            secim = scan.nextInt();

            System.out.println("*************");

            if (secim == 1) {
                Listele();
            }
            if (secim == 2) {
                Ekle();
            }
            if (secim == 3) {
                Guncelle();
            }
            if (secim == 4) {
                Sil();
            }
            if (secim == 5) {
                try {
                    stmt.close();
                    con.close();
                } catch (Exception e) {
                    System.out.println(e);
                }

                break;
            }
        }
    }
//Veri tabanındakileri listeler
    public void Listele() {
        try {

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from StokKontroll");
            while (rs.next()) {
                System.out.println("Barcode No:" + rs.getInt(1) + "\t Name of The Product: " + rs.getString(2) + "\t Product Quantity: " + rs.getInt(3) + "\t Product Price: " + rs.getInt(4) + "TL");
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }
//Veri tabanına ekler
    public void Ekle() {
        
        Urun u=new Urun();
        
        Scanner scan = new Scanner(System.in, "iso-8859-9");
        System.out.print("Product Barcode No: ");
        int UrunBarkodNo = scan.nextInt();
        u.BarkodNo=UrunBarkodNo;
        System.out.print("Name of the product: ");
        String UrunAd = scan.next();
        u.UrunAd=UrunAd;
        System.out.print("Number of products: ");
        int UrunAdet = scan.nextInt();
        u.Adet=UrunAdet;
        System.out.print("Product Price:");
        int UrunFiyat = scan.nextInt();
        u.Fiyat=UrunFiyat;
        urunSet.add(u);
        
        try {
            Statement stmt = con.createStatement();
            String sorgu = String.format("insert into StokKontroll values( %d, '%s','%d', '%d')", UrunBarkodNo, UrunAd, UrunAdet, UrunFiyat);
            int ekleme = stmt.executeUpdate(sorgu);
            System.out.println("Registry Added");
        } catch (Exception e) {
            System.out.println(e);
        }

    }
//Veri tabanındaki bir malzemeyi günceller
    public void Guncelle() {
        Scanner scan = new Scanner(System.in, "iso-8859-9");
        try {
            Listele();
            System.out.print("Enter Barcode Number: ");
            int EskiBarkodNo = scan.nextInt();
            System.out.print("New Barcode Number: ");
            int YeniBarkodNo = scan.nextInt();
            System.out.print("New Product Name: ");
            String YeniUrunAd = scan.next();
            System.out.print("New Product Quantity: ");
            int YeniUrunAdet = scan.nextInt();
            System.out.print("New price: ");
            int YeniUrunFiyat = scan.nextInt();

            String sorgu = String.format("update StokKontroll set UrunBarkodNo='" + YeniBarkodNo + "', UrunAd='" + YeniUrunAd + "', UrunAdedi='" + YeniUrunAdet
                    + "'" + ", UrunFiyati='" + YeniUrunFiyat + "'"
                    + " WHERE UrunBarkodNo='%d'", EskiBarkodNo);

            Statement stmt = con.createStatement();
            int guncelleme = stmt.executeUpdate(sorgu);
            System.out.println("Registry Updated");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
//Veri tabanından bir malzeme siler
    public void Sil() {
        Scanner scan = new Scanner(System.in, "iso-8859-9");
        try {
            Listele();
            System.out.print("Enter Barcode Number: ");
            int SilinecekBarkodNo = scan.nextInt();

            String sorgu = "delete from StokKontroll where UrunBarkodNo=" + SilinecekBarkodNo;
            Statement stmt1 = con.createStatement();
            stmt1.executeUpdate(sorgu);
            System.out.println("Registry Deleted");

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
