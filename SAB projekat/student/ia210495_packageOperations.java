/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package student;
import org.junit.runner.JUnitCore;
import java.sql.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import rs.etf.sab.operations.PackageOperations;


/**
 *
 * @author Korisnik
 */
public class ia210495_packageOperations implements PackageOperations {

    @Override
    public int insertPackage(int i, int i1, String string, int i2, BigDecimal bd) // dodaje a ono sto nemas bude null
            // ubaci logiku za racunanje cene iz postavke
            
    {       
            
            String pronadjiKorisnika = "SELECT * FROM Korisnik WHERE korIme =?";
            List<Integer> kordinate = odrediKoordinate(i, i1);
            for(Integer t: kordinate) {
                System.out.println("student.ia210495_packageOperations.insertPackage()" + t);
            }
            double euk = sracunajDuzinu(kordinate.get(0), kordinate.get(1), kordinate.get(2), kordinate.get(3));
       
            
            double osnovnaCena = 0.0;
            int tezinskiFaktor = 0;
            int cenaPoKg = 0;
            if (i2 == 0) {
                tezinskiFaktor = 0;
                osnovnaCena = 10;
                cenaPoKg = 0;
            }
            else if (i2 == 1) {
                tezinskiFaktor = 1;
                osnovnaCena = 25;
                cenaPoKg = 100;
            }
            else if (i2 == 2) {
                tezinskiFaktor = 2;
                osnovnaCena = 75;
                cenaPoKg = 300;
            }
            System.out.println(euk);
            System.out.println(cenaPoKg);
            System.out.println(bd.doubleValue());
            System.out.println(tezinskiFaktor);
            
            double konacnaCena = (osnovnaCena + (tezinskiFaktor * bd.doubleValue()) * cenaPoKg) * euk; 
            BigDecimal konacnaCenaBigDecimal = BigDecimal.valueOf(konacnaCena);
         String upit = "insert into Paket (IdOpst1, IdOpst2, Tezina, Tip, IdKor, Status, Cena, VremePrihvatanja, IdKur) values(?, ?, ?, ?, ?, ?, ?, NULL, ?)";
         
         Connection con = DB.getInstance().getConnection();
        try(
                PreparedStatement p = con.prepareStatement(upit, PreparedStatement.RETURN_GENERATED_KEYS);
                PreparedStatement pronadjiKorisnikaS = con.prepareStatement(pronadjiKorisnika);
                )
            {
            pronadjiKorisnikaS.setString(1, string);
            ResultSet rsKor = pronadjiKorisnikaS.executeQuery();
            int idKorisnika = 0;
            if (rsKor.next()) {
                idKorisnika = rsKor.getInt("IdKor");
               // System.out.println("student.ia210495_packageOperations.insertPackage()" + idKorisnika);
            }    
         
            System.out.println("konacnaCenaBigDecimal: " + konacnaCenaBigDecimal );
            p.setInt(1, i);
            p.setInt(2, i1);
            p.setBigDecimal(3, bd);
            p.setInt(4, i2);
            p.setInt(5, idKorisnika);
            p.setInt(6, 0);
            p.setBigDecimal(7, konacnaCenaBigDecimal);
            p.setInt(8,0);
            p.executeUpdate();                   
            ResultSet rs = p.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            else {
                return -1;
            }
                       
        } catch (SQLException ex) {
            Logger.getLogger(ia210495_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
       // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int insertTransportOffer(String string, int i, BigDecimal bd) // dodavanje u tabelu ponuda 
            // ako je bd null random generisi neki broj;
    {
         String upit = "insert into Ponuda (Idkor, IdPak, Procenat) values(?, ?, ?)";
         String pronadjiKurira = "SELECT* FROM Korisnik K JOIN Kurir K2 ON K.IdKor = K2.IdKor  WHERE K.korIme=?";
         
         Connection con = DB.getInstance().getConnection();
        try(
                PreparedStatement p = con.prepareStatement(upit, PreparedStatement.RETURN_GENERATED_KEYS);
                PreparedStatement pronadjiKorisnikaS = con.prepareStatement(pronadjiKurira);
                )
            {
            pronadjiKorisnikaS.setString(1, string);
            ResultSet rsKor = pronadjiKorisnikaS.executeQuery();
            int idKorisnika = 0;
            if (rsKor.next()) {
                idKorisnika = rsKor.getInt("IdKor");
                System.out.println("student.ia210495_packageOperations.insertTransportOffer()" + idKorisnika);
            }    
            else {
                  System.out.println("Nije pronadjen");
            }    
            p.setInt(1, idKorisnika);
            p.setInt(2, i);
            
            if (bd == null){
                double novaVrednost = Math.random() * 10; // TODO Bolje
                p.setBigDecimal(3, BigDecimal.valueOf(novaVrednost));
                
            }
            else {
                p.setBigDecimal(3, bd);
            }

            p.executeUpdate();                   
            ResultSet rs = p.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            else {
                return -1;
            }
                       
        } catch (SQLException ex) {
            Logger.getLogger(ia210495_packageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean acceptAnOffer(int i) // menjas samo status isporuke, nakon ove funkcije treba triger da se aktivira
            // nek prommeni stattsu paketa sa 0 na 1 i to nek aktivira triger
    {
        
        String pronadjiPaket = "SELECT* FROM Paket P1 JOIN Ponuda P2 ON P1.IdPak = P2.IdPak WHERE IdPon =?" ;
        
        String pronadjiKurira = "SELECT* FROM Ponuda P WHERE IdPon =?";
         Connection con = DB.getInstance().getConnection();
        try(
                PreparedStatement pronadjiPaketS = con.prepareStatement(pronadjiPaket, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                PreparedStatement pronadjiKuriraS = con.prepareStatement(pronadjiKurira, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                )
            {
            pronadjiPaketS.setInt(1, i);
            ResultSet rsPaket = pronadjiPaketS.executeQuery();
            int idKorisnika = 0;
            if (rsPaket.next()) {
                rsPaket.updateInt("Status", 1);
                rsPaket.updateInt("IdKur", rsPaket.getInt("IdKor"));
                //rsPaket.updateInt("IdKur", rsPaket.getInt("IdKor"));
                java.sql.Timestamp trVreme = new java.sql.Timestamp(System.currentTimeMillis());
                rsPaket.updateTimestamp("VremePrihvatanja", trVreme);
                BigDecimal novaCena = rsPaket.getBigDecimal("Cena");
                System.out.println("TestCenaM " + novaCena);
                BigDecimal procenat = rsPaket.getBigDecimal("Procenat");
                procenat = procenat.divide(new BigDecimal("100"));
                System.out.println("procenat " + procenat);
                
                novaCena = novaCena.add(novaCena.multiply(procenat));
                System.out.println("novaCena: " + novaCena);
                rsPaket.updateBigDecimal("Cena", novaCena);
                rsPaket.updateRow();
                return true;                
            }    
            
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(ia210495_packageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
      //  throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Integer> getAllOffers() {
         String pronadjiPaket = "SELECT* FROM Ponuda" ;
         List<Integer> listaPonuda = new ArrayList<>();
         Connection con = DB.getInstance().getConnection();
        try(
                Statement s = con.createStatement();
                )
            {
            
            ResultSet ponude = s.executeQuery(pronadjiPaket);
            while(ponude.next()) {
                listaPonuda.add(ponude.getInt("IdPon"));
            }
            return listaPonuda;
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }   catch (SQLException ex) {
            Logger.getLogger(ia210495_packageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaPonuda;
    }

    @Override
    public List<PackageOperations.Pair<Integer, BigDecimal>> getAllOffersForPackage(int i) {
        String upit = "SELECT* FROM Ponuda P1 JOIN Paket P2 ON P1.IdPak = P2.IdPak WHERE IdPak=?";
        List<PackageOperations.Pair<Integer, BigDecimal>> listaPonuda = new ArrayList<>();
        Connection con = DB.getInstance().getConnection();
        try(
                PreparedStatement ps = con.prepareStatement(upit);
                )
            {
            ps.setInt(1, i);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
               // Pair<Integer,BigDecimal> p = new Pair<>(rs.getInt("IdPon"), rs.getBigDecimal("Procenat"));
                listaPonuda.add(new Pair(rs.getInt("IdPon"), rs.getBigDecimal("Procenat")));

            }
            return listaPonuda;
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }   catch (SQLException ex) {
            Logger.getLogger(ia210495_packageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaPonuda;
    //    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean deletePackage(int i) {
        {
        int brojObrisanih = 0;
        Connection con = DB.getInstance().getConnection();
       
        String upit = "DELETE FROM Paket WHERE IdPak=?";
        try (
             PreparedStatement p = con.prepareStatement(upit);

            )
        {
            p.setInt(1, i);
            p.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ia210495_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
    
        return false;
    }
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean changeWeight(int i, BigDecimal bd) // uradi update cene u paketu jer cena zavisi od tezine
    {
        String dohvatiPaket = "SELECT* FROM Paket WHERE IdPak =?";
        String promeniCenu = "SELECT* FROM Paket WHERE IdPak =?";
        Connection con  = DB.getInstance().getConnection();
        try(PreparedStatement p = con.prepareStatement(dohvatiPaket, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                PreparedStatement p2 = con.prepareStatement(promeniCenu, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                )
        {
            p.setInt(1, i);
            int i2 = 0;
            int ops1 = 0;
            int ops2 = 0;
            ResultSet rs = p.executeQuery();
            if(rs.next()) {
                rs.updateBigDecimal("Tezina", bd);
                rs.updateRow();
                ops1 = rs.getInt("IdOpst1");
                ops2 = rs.getInt("IdOpst2");
                i2 = rs.getInt("Tip");
               
            }
          
            List<Integer> kordinate = odrediKoordinate(ops1, ops2);
          //  for(Integer t: kordinate) {
              //  System.out.println("student.ia210495_packageOperations.insertPackage()" + t);
            //}
            double euk = sracunajDuzinu(kordinate.get(0), kordinate.get(1), kordinate.get(2), kordinate.get(3));
            double osnovnaCena = 0.0;
            int tezinskiFaktor = 0;
            int cenaPoKg = 0;
            if (i2 == 0) {
                tezinskiFaktor = 0;
                osnovnaCena = 10;
                cenaPoKg = 0;
            }
            else if (i2 == 1) {
                tezinskiFaktor = 1;
                osnovnaCena = 25;
                cenaPoKg = 100;
            }
            else if (i2 == 2) {
                tezinskiFaktor = 2;
                osnovnaCena = 75;
                cenaPoKg = 300;
            }
            System.out.println(euk);
            System.out.println(cenaPoKg);
            System.out.println(bd.doubleValue());
            System.out.println(tezinskiFaktor);
            
            double konacnaCena = (osnovnaCena + (tezinskiFaktor * bd.doubleValue()) * cenaPoKg) * euk; 
            p2.setInt(1, i);
            ResultSet rs2 = p2.executeQuery();
            if (rs2.next()) {
                rs2.updateBigDecimal("Cena", BigDecimal.valueOf(konacnaCena));
                rs2.updateRow();
            }
            
            
            return true;
            
        } catch (SQLException ex) {
            Logger.getLogger(ia210495_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean changeType(int i, int i1) // promeni tip i cenu;
    {
        String dohvatiPaket = "SELECT* FROM Paket WHERE IdPak =?";
        String promeniCenu = "SELECT* FROM Paket WHERE IdPak =?";
        Connection con  = DB.getInstance().getConnection();
        try(PreparedStatement p = con.prepareStatement(dohvatiPaket, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                PreparedStatement p2 = con.prepareStatement(promeniCenu, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                )
        {
            p.setInt(1, i);
            int i2 = 0;
            int ops1 = 0;
            int ops2 = 0;
            BigDecimal bd = null;
            ResultSet rs = p.executeQuery();
            if(rs.next()) {
                bd = rs.getBigDecimal("Cena");
                rs.updateInt("Tip", i1);
                rs.updateRow();
                ops1 = rs.getInt("IdOpst1");
                ops2 = rs.getInt("IdOpst2");
                i2 = rs.getInt("Tip");
               
            }
          
            List<Integer> kordinate = odrediKoordinate(ops1, ops2);
           /* for(Integer t: kordinate) {
               System.out.println("student.ia210495_packageOperations.insertPackage()" + t);
            }*/
            double euk = sracunajDuzinu(kordinate.get(0), kordinate.get(1), kordinate.get(2), kordinate.get(3));
            double osnovnaCena = 0.0;
            int tezinskiFaktor = 0;
            int cenaPoKg = 0;
            if (i2 == 0) {
                tezinskiFaktor = 0;
                osnovnaCena = 10;
                cenaPoKg = 0;
            }
            else if (i2 == 1) {
                tezinskiFaktor = 1;
                osnovnaCena = 25;
                cenaPoKg = 100;
            }
            else if (i2 == 2) {
                tezinskiFaktor = 2;
                osnovnaCena = 75;
                cenaPoKg = 300;
            }
            System.out.println(euk);
            System.out.println(cenaPoKg);
            
            System.out.println(tezinskiFaktor);
            
            double konacnaCena = (osnovnaCena + (tezinskiFaktor * bd.doubleValue()) * cenaPoKg) * euk; 
            p2.setInt(1, i);
            ResultSet rs2 = p2.executeQuery();
            if (rs2.next()) {
                rs2.updateBigDecimal("Cena", BigDecimal.valueOf(konacnaCena));
                rs2.updateRow();
            }
            
            
            return true;
            
        } catch (SQLException ex) {
            Logger.getLogger(ia210495_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
       // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Integer getDeliveryStatus(int i) {
         String dohvatiPaket = "SELECT* FROM Paket WHERE IdPak =?";
        
        Connection con  = DB.getInstance().getConnection();
        try(PreparedStatement p = con.prepareStatement(dohvatiPaket);
               
                )
        {
            p.setInt(1, i);
            Integer mojInt = 0;
            ResultSet rs = p.executeQuery();
            if(rs.next()) {
                mojInt = rs.getInt("Status");
               
            }
            else{
                return null;
            }
            return mojInt;
        } catch (SQLException ex) {
            Logger.getLogger(ia210495_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
       // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public BigDecimal getPriceOfDelivery(int i) {
        String dohvatiPaket = "SELECT* FROM Paket WHERE IdPak =?";
        
        Connection con  = DB.getInstance().getConnection();
        try(PreparedStatement p = con.prepareStatement(dohvatiPaket);
               
                )
        {
            p.setInt(1, i);
            BigDecimal bd = null;
            ResultSet rs = p.executeQuery();
            if(rs.next()) {
                bd = rs.getBigDecimal("Cena"); 
            }
            else{
                return null;
            }
            return bd;
        } catch (SQLException ex) {
            Logger.getLogger(ia210495_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
       // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Date getAcceptanceTime(int i) {
        String dohvatiPaket = "SELECT* FROM Paket WHERE IdPak =?";
        
        Connection con  = DB.getInstance().getConnection();
        try(PreparedStatement p = con.prepareStatement(dohvatiPaket);
               
                )
        {
            p.setInt(1, i);
            Date datum = null;
            ResultSet rs = p.executeQuery();
            if(rs.next()) {
                datum = rs.getDate("VremePrihvatanja"); 
            }
            else{
                return null;
            }
            return datum;
        } catch (SQLException ex) {
            Logger.getLogger(ia210495_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
      //  throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Integer> getAllPackagesWithSpecificType(int i) {
         String pronadjiPaket = "SELECT* FROM Paket WHERE Tip =?" ;
         List<Integer> listaPaketa = new ArrayList<>();
         Connection con = DB.getInstance().getConnection();
        try(
                PreparedStatement ps = con.prepareStatement(pronadjiPaket);
                )
            {
                
            ps.setInt(1, i);            
            ResultSet paketi = ps.executeQuery();
            while(paketi.next()) {
                listaPaketa.add(paketi.getInt("IdPak"));
            }
            return listaPaketa;
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }   catch (SQLException ex) {
            Logger.getLogger(ia210495_packageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaPaketa;
     //   throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Integer> getAllPackages() {
            String pronadjiPakete = "SELECT* FROM Paket" ;
         List<Integer> listaPaketa = new ArrayList<>();
         Connection con = DB.getInstance().getConnection();
        try(
                Statement s = con.createStatement();
                )
            {
  
            ResultSet paketi = s.executeQuery(pronadjiPakete);
            while(paketi.next()) {
                listaPaketa.add(paketi.getInt("IdPak"));
            }
            return listaPaketa;
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }   catch (SQLException ex) {
            Logger.getLogger(ia210495_packageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaPaketa;
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Integer> getDrive(String string) { // kurir je u statusu vozi a paket nije isporucen, trazi se svi id paketa koji se zajedno isporucuju a ti dobijes nazivKurira
            //kurir je u statusu vozi, paket nije isporucen a kurir je u statusu vozi
         String pronadjiPaket = "SELECT* FROM Paket P JOIN Kurir K ON P.IdKur = K.IdKor JOIN Korisnik K2 ON K2.IdKor = K.IdKor WHERE korIme=? AND P.Status !=3 AND K.Status = 1" ;
         List<Integer> listaPaketa = new ArrayList<>();
         Connection con = DB.getInstance().getConnection();
        try(
                PreparedStatement ps = con.prepareStatement(pronadjiPaket);
                )
            {
                
            ps.setString(1, string);            
            ResultSet paketi = ps.executeQuery();
            while(paketi.next()) {
                listaPaketa.add(paketi.getInt("IdPak"));
            }
            return listaPaketa;
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }   catch (SQLException ex) {
            Logger.getLogger(ia210495_packageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaPaketa;
            
    
        
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int driveNextPackage(String string) {
        String proveraVoznje = "SELECT* FROM Kurir K JOIN Korisnik K2 ON K2.IdKor = K.IdKor WHERE K2.korIme =? AND K.Status = 1";
        String zaProfit = "SELECT* FROM Voznja V JOIN Paket P ON V.IdPak = P.IdPak JOIN Kurir K2 ON K2.IdKor = P.IdKur JOIN Korisnik K3 ON K3.IdKor = K2.IdKor JOIN Vozilo V1 ON V1.Idvoz = K2.VoziloId WHERE K3.korIme =? ORDER BY rb ASC";
        String sviPaketiZaNovuVoznju = "SELECT* FROM Paket P JOIN Kurir K ON P.IdKur = K.IdKor JOIN Korisnik K2 ON K.IdKor = K2.IdKor WHERE P.Status = 1 AND K2.korIme =? ORDER BY P.VremePrihvatanja ASC";
        String izmeniProfit = "SELECT* FROM Kurir K1 JOIN Korisnik K2 ON K1.IdKor = K2.IdKor WHERE K2.korIme =?";
        int idKurira = 0;
       // String napraviVoznju = ""
        Connection con = DB.getInstance().getConnection();
        try(
                PreparedStatement ps = con.prepareStatement(proveraVoznje, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                PreparedStatement paketiZaNovuVoznjuS = con.prepareStatement(sviPaketiZaNovuVoznju,  ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                PreparedStatement izmeniProfitS = con.prepareStatement(izmeniProfit, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                )
        {
        
        ps.setString(1, string);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) // kurir vec vozi
        {
           // int preostalo = rs.getInt("Preostalo");
            System.out.println("preostalo");
            int idPaketaZaVratiti = 0;
           // String odrediIdKurira = "SELECT* FROM Voznja V"
            String dohvatiSledeciPaket = "SELECT* FROM Voznja V JOIN Paket P ON V.IdPak = P.IdPak JOIN Kurir K2 ON K2.IdKor = P.IdKur JOIN Korisnik K3 ON K3.IdKor = K2.IdKor  WHERE K3.korIme =? AND Uzeto = 0 ORDER BY rb ASC";
            String dohvatiPrethodniPaket = "SELECT* FROM Voznja V JOIN Paket P ON V.IdPak = P.IdPak JOIN Kurir K2 ON K2.IdKor = P.IdKur JOIN Korisnik K3 ON K3.IdKor = K2.IdKor  WHERE K3.korIme =? AND Uzeto = 1 ORDER BY rb DESC";
            
            String promeniStatusPaketa = "SELECT* FROM Paket WHERE IdPak =?";
           
            String proveriJelPoslednjiPaket = "SELECT COUNT(*) as Preostalo FROM Voznja V JOIN Kurir K ON V.IdKur = K.IdKor JOIN Korisnik K2 ON K.Idkor = K2.IdKor WHERE K2.korIme=? AND Uzeto = 0";
            try(PreparedStatement dohvatiSledeciPaketS = con.prepareStatement(dohvatiSledeciPaket, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                    PreparedStatement dohvatiPrethodniPaketS = con.prepareStatement(dohvatiSledeciPaket, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                    PreparedStatement promeniStatusPaketaS = con.prepareStatement(promeniStatusPaketa, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                    PreparedStatement proveriJelPoslednjiPaketS = con.prepareStatement(proveriJelPoslednjiPaket, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                   
                    )
            {
                dohvatiSledeciPaketS.setString(1, string);
                ResultSet rs5 = dohvatiSledeciPaketS.executeQuery();
                if(rs5.next()) {
                    dohvatiPrethodniPaketS.setString(1, string);
                    ResultSet rsStatusPrethodni = dohvatiPrethodniPaketS.executeQuery();
                    if(rsStatusPrethodni.next()){
                        //System.out.println(rsStatusPrethodni.getInt("Status"));
                       // rsStatusPrethodni.updateInt("Status", 3);
                        //rsStatusPrethodni.updateRow();
                    }
                    else {
                        System.out.println("nema prethodnog");
                    }
                    idPaketaZaVratiti = rs5.getInt("IdPak");
                    rs5.updateInt("Uzeto", 1);
                    rs5.updateRow();
                    
                    
                    promeniStatusPaketaS.setInt(1, idPaketaZaVratiti);
                    ResultSet rsStatus = promeniStatusPaketaS.executeQuery();
                    if (rsStatus.next()) {
                        rsStatus.updateInt("Status", 3);
                        rsStatus.updateRow();
                    }
                    
                    proveriJelPoslednjiPaketS.setString(1, string);
                    ResultSet rsPoslednji = proveriJelPoslednjiPaketS.executeQuery();
                    int poslednji = 0;
                    if(rsPoslednji.next()) {
                        int preostalo = rsPoslednji.getInt("Preostalo");
                         System.out.println("p1" + preostalo);
                        if (preostalo == 0)
                        {
                           
                            try(PreparedStatement zaProfitS = con.prepareStatement(zaProfit);
                                    )
                            {
                                List<Integer> opstineSve = new ArrayList<>();
                                int odgovarajuceGorivo = 0;
                                BigDecimal odgovarajucaPotrosnja = null;
                                zaProfitS.setString(1, string);
                                double udaljenost = 0.0;
                                int tipGoriva = 0;
                             //   BigDecimal potrosnja = null;
                                double potrosenihLitara = 0.0;
                                BigDecimal ostavenNovac = BigDecimal.ZERO;
                                ResultSet rsProfit = zaProfitS.executeQuery();
                                int prethodna = 0;
                                while(rsProfit.next()) {
                                    System.out.println("TestPaketaZavrsni: " +rsProfit.getBigDecimal("Cena") );
                                    ostavenNovac = ostavenNovac.add(rsProfit.getBigDecimal("Cena"));
                                    odgovarajucaPotrosnja = rsProfit.getBigDecimal("Potrosnja");
                                    odgovarajuceGorivo = rsProfit.getInt("TipGoriva");
                                    
                                     int ops1 = rsProfit.getInt("IdOpst1");
                                     int ops2 = rsProfit.getInt("IdOpst2");
                                     opstineSve.add(ops1);
                                     opstineSve.add(ops2);
                                    System.out.println(ops1);
                                    System.out.println(ops2);
                                   
                                }
                                for (int t = 0; t < opstineSve.size() - 1; t++) {
                                    List<Integer> kordinate = odrediKoordinate(opstineSve.get(t), opstineSve.get(t + 1));
                                    double duzina = sracunajDuzinu(kordinate.get(0), kordinate.get(1), kordinate.get(2), kordinate.get(3));
                                    udaljenost = udaljenost + duzina;
                                }
                                
                                potrosenihLitara = odgovarajucaPotrosnja.doubleValue() * udaljenost;
                                BigDecimal potrosenNovac = BigDecimal.ZERO;
                                if (tipGoriva == 0) {
                                   potrosenNovac = BigDecimal.valueOf(potrosenihLitara * 15);
                                }
                                else if(tipGoriva == 1){
                                     potrosenNovac = BigDecimal.valueOf( potrosenihLitara * 32);
                                }
                                else if (tipGoriva == 2){
                                     potrosenNovac = BigDecimal.valueOf( potrosenihLitara * 36);;
                                }
                                System.out.println("Ostvaren novac" + ostavenNovac);
                                System.out.println("Potrosen novac" + potrosenNovac);
                                izmeniProfitS.setString(1, string);
                                ResultSet rIzmeniProfit = izmeniProfitS.executeQuery();
                                if (rIzmeniProfit.next()){
                                    int brPaketa = rIzmeniProfit.getInt("BrojPaketaIsporucenih");
                                    brPaketa++;
                                    rIzmeniProfit.updateInt("BrojPaketaIsporucenih", brPaketa);
                                  //  rIzmeniProfit.updateRow();
                                    rIzmeniProfit.updateBigDecimal("Profit", ostavenNovac.subtract(potrosenNovac));
                                    rIzmeniProfit.updateRow();
                                }
                            }
                          
                        }
                        
                    }
             

                }
                return idPaketaZaVratiti;
            }
        }
        else // kurir jos ne vozi
        { // TODO: sta ako je prvi paket ujedno i jedini - to nemas (proveri)
            System.out.println("TEST");
            String promeniStatusKuriru = "SELECT* FROM Kurir K1 JOIN Korisnik K2 On K1.IdKor = K2.IdKor WHERE K2.korIme = ?";
            PreparedStatement promeniStatusS = con.prepareStatement(promeniStatusKuriru, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            promeniStatusS.setString(1, string);
            ResultSet rsPromeniStatursKuriru = promeniStatusS.executeQuery();
            if (rsPromeniStatursKuriru.next()) {
                rsPromeniStatursKuriru.updateInt("Status", 1);
                rsPromeniStatursKuriru.updateRow();
            }
            System.out.println("student.ge()");
            String novaVoznja = "INSERT INTO Voznja(Rb, IdKur, IdPak, Uzeto) VALUES (?, ?, ?, ?)";
            String promeniPaket = "SELECT* FROM Paket WHERE IdPak =?";
            List<Integer> paketi = new ArrayList<>();
            paketiZaNovuVoznjuS.setString(1, string);
            ResultSet rs2 = paketiZaNovuVoznjuS.executeQuery();
            int idPrvogPaketa = 0;
            int rb = 1;
            while(rs2.next()) {
                idKurira = rs2.getInt("IdKur"); // uzmi kurira za taj paket
                try(PreparedStatement p = con.prepareStatement(novaVoznja);
                    PreparedStatement p2 = con.prepareStatement(promeniPaket, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                        )
                {
                    int idPaketa = rs2.getInt("IdPak");
             
                    p.setInt(1, rb);
                    p.setInt(2, idKurira);
                    p.setInt(3, idPaketa);
                    p.setInt(4, 0);  
                    
                    if (rb == 1) {
                        p.setInt(4, 1);
                       // rs2.updateInt("Uzeto", 1);
                       // rs2.updateRow();
                        idPrvogPaketa = idPaketa;
                    }
                    p.executeUpdate();
                    p2.setInt(1, idPaketa);
                    ResultSet rs3 = p2.executeQuery();
                    if(rs3.next()) {
                        if (rb == 1) rs3.updateInt("Status", 3);
                        else rs3.updateInt("Status", 2); // svima status na 3 da su pokupljeni
                        rs3.updateRow();
                    }      
               
                    rb++;
                }
            }
            
            if (rb == 2) // dakle imas samo 1 paket i to je cela voznja, znaci taj 1 je i poslednji ujedno
            {
             try(PreparedStatement zaProfitS = con.prepareStatement(zaProfit);
                                    )
                            {
                                List<Integer> opstineSve = new ArrayList<>();
                                int odgovarajuceGorivo = 0;
                                BigDecimal odgovarajucaPotrosnja = null;
                                zaProfitS.setString(1, string);
                                double udaljenost = 0.0;
                                int tipGoriva = 0;
                             //   BigDecimal potrosnja = null;
                                double potrosenihLitara = 0.0;
                                BigDecimal ostavenNovac = BigDecimal.ZERO;
                                ResultSet rsProfit = zaProfitS.executeQuery();
                                int prethodna = 0;
                                while(rsProfit.next()) {
                                    System.out.println("TestPaketaZavrsni: " +rsProfit.getBigDecimal("Cena") );
                                    ostavenNovac = ostavenNovac.add(rsProfit.getBigDecimal("Cena"));
                                    odgovarajucaPotrosnja = rsProfit.getBigDecimal("Potrosnja");
                                    odgovarajuceGorivo = rsProfit.getInt("TipGoriva");
                                    
                                     int ops1 = rsProfit.getInt("IdOpst1");
                                     int ops2 = rsProfit.getInt("IdOpst2");
                                     opstineSve.add(ops1);
                                     opstineSve.add(ops2);
                                    System.out.println(ops1);
                                    System.out.println(ops2);
                                   
                                }
                                for (int t = 0; t < opstineSve.size() - 1; t++) {
                                    List<Integer> kordinate = odrediKoordinate(opstineSve.get(t), opstineSve.get(t + 1));
                                    double duzina = sracunajDuzinu(kordinate.get(0), kordinate.get(1), kordinate.get(2), kordinate.get(3));
                                    udaljenost = udaljenost + duzina;
                                }
                                
                                potrosenihLitara = odgovarajucaPotrosnja.doubleValue() * udaljenost;
                                BigDecimal potrosenNovac = BigDecimal.ZERO;
                                if (tipGoriva == 0) {
                                   potrosenNovac = BigDecimal.valueOf(potrosenihLitara * 15);
                                }
                                else if(tipGoriva == 1){
                                     potrosenNovac = BigDecimal.valueOf( potrosenihLitara * 32);
                                }
                                else if (tipGoriva == 2){
                                     potrosenNovac = BigDecimal.valueOf( potrosenihLitara * 36);;
                                }
                                System.out.println("Ostvaren novac" + ostavenNovac);
                                System.out.println("Potrosen novac" + potrosenNovac);
                                izmeniProfitS.setString(1, string);
                                ResultSet rIzmeniProfit = izmeniProfitS.executeQuery();
                                if (rIzmeniProfit.next()){
                                    int brPaketa = rIzmeniProfit.getInt("BrojPaketaIsporucenih");
                                    brPaketa++;
                                    rIzmeniProfit.updateInt("BrojPaketaIsporucenih", brPaketa);
                                  //  rIzmeniProfit.updateRow();
                                    rIzmeniProfit.updateBigDecimal("Profit", ostavenNovac.subtract(potrosenNovac));
                                    rIzmeniProfit.updateRow();
                    }
            }
                          
        }
                        

            
            return idPrvogPaketa;
        }
        } catch (SQLException ex) {
            Logger.getLogger(ia210495_packageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public class Pair<A,B> implements PackageOperations.Pair{
        private A prviParametar;
        private B drugiParametar;

        public Pair(A prviParametar, B drugiParametar) {
            this.prviParametar = prviParametar;
            this.drugiParametar = drugiParametar;
        }
        
        
        @Override
        public Object getFirstParam() {
            return this.prviParametar;
           // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        @Override
        public Object getSecondParam() {
            return this.drugiParametar;
            //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
        
        
    }
    
    private List<Integer> odrediKoordinate(int ops1, int ops2) {
        
        List<Integer> listaKoordinata = new ArrayList<>();
        String pronadjiKordinate1 = "SELECT* FROM Opstina WHERE IdOps =?";
        String pronadjiKordinate2 = "SELECT* FROM Opstina WHERE IdOps =?";
          
        Connection con = DB.getInstance().getConnection();
        try(
               PreparedStatement pronadjiKordinate1S = con.prepareStatement(pronadjiKordinate1);    
               PreparedStatement pronadjiKordinate2S = con.prepareStatement(pronadjiKordinate2);    
               )
            {
            pronadjiKordinate1S.setInt(1, ops1);
            ResultSet rs = pronadjiKordinate1S.executeQuery();
            if (rs.next()){
                listaKoordinata.add(rs.getInt("kordinataX"));
                listaKoordinata.add(rs.getInt("kordinataY"));   
            }

            
            pronadjiKordinate2S.setInt(1, ops2);
            ResultSet rs2 = pronadjiKordinate2S.executeQuery();
            if (rs2.next()) {
                listaKoordinata.add(rs2.getInt("kordinataX"));
                listaKoordinata.add(rs2.getInt("kordinataY"));
            }

            
            return listaKoordinata;
        } catch (SQLException ex) {
            Logger.getLogger(ia210495_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaKoordinata;
    }
    
   private double sracunajDuzinu(int x1, int y1, int x2, int y2) {
       return Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
   }
   
 

    
}
