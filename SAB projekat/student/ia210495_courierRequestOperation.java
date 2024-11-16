/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package student;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.CourierRequestOperation;
/**
 *
 * @author Korisnik
 */
public class ia210495_courierRequestOperation implements CourierRequestOperation{

    @Override
    public boolean insertCourierRequest(String string, String string1) {
            System.out.println(string);
            System.out.println(string1);
            String pronadjiKurira = "SELECT* FROM Korisnik WHERE korIme=?";
            String pronadjiVozilo = "SELECT* FROM Vozilo WHERE RegistracioniBroj=?";
            int idVozila = 0;
            int idKurira = 0;
            String upit = "insert into ZahtevZaKurira (IdKor, IdVoz) values(?, ?)";
            Connection con = DB.getInstance().getConnection();
          try(
                PreparedStatement pronadjiKuriraS = con.prepareStatement(pronadjiKurira);    
                PreparedStatement pstmt = con.prepareStatement(pronadjiVozilo);
                
                 PreparedStatement upisiZahtevKurira = con.prepareStatement(upit, PreparedStatement.RETURN_GENERATED_KEYS);)
              {
              pronadjiKuriraS.setString(1, string);
              ResultSet rs0 = pronadjiKuriraS.executeQuery();
              if (rs0.next()) {  
                 idKurira = rs0.getInt("IdKor");
              }
              else {
                    System.out.println(string);
                  return false; // nema tog usera
              }
              pstmt.setString(1, string1);
              ResultSet rs = pstmt.executeQuery();
              if (rs.next()) {
                
                  idVozila = rs.getInt("IdVoz");
              }
              else {
                  return false; // nema tog vozila;
              }
              System.out.println(idKurira);
              System.out.println(idVozila);
              upisiZahtevKurira.setInt(1, idKurira);
              upisiZahtevKurira.setInt(2, idVozila);
              upisiZahtevKurira.executeUpdate();   
              return true;
          } catch (SQLException ex) {
              Logger.getLogger(ia210495_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
          }
          return true;
         // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
      }

    @Override
    public boolean deleteCourierRequest(String string) {
            String pronadjiKurira = "SELECT* FROM Korisnik WHERE korIme = ?";
            int idKurira = 0;
            String upit = "DELETE FROM ZahtevZaKurira WHERE IdKor=?";
            Connection con = DB.getInstance().getConnection();
          try(
                PreparedStatement pronadjiKuriraS = con.prepareStatement(pronadjiKurira);   
                PreparedStatement izbrisiZahtevZaKurira = con.prepareStatement(upit, PreparedStatement.RETURN_GENERATED_KEYS);)
              {
              pronadjiKuriraS.setString(1, string);
              ResultSet rs0 = pronadjiKuriraS.executeQuery();
              if (rs0.next()) {
                 idKurira = rs0.getInt("IdKor");
              }
              else {
                  return false; // nema tog usera
              }
              izbrisiZahtevZaKurira.setInt(1, idKurira);
              izbrisiZahtevZaKurira.executeUpdate();
              return true;
          } catch (SQLException ex) {
              Logger.getLogger(ia210495_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
          }
          return true;

         // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
      }
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    

    @Override
    public boolean changeVehicleInCourierRequest(String string, String string1) {
            String pronadjiKurira = "SELECT* FROM Kurir K1 JOIN Korisnik K2 ON K1.IdKor = K2.IdKor WHERE korIme=?";
            String pronadjiVozilo = "SELECT* FROM Vozilo WHERE RegistracioniBroj=?";
            String dohvKuriraUpdate = "SELECT* FROM ZahtevZaKurira WHERE IdKor=?";
            int idVozila = 0;
            int idKurira = 0;
          
            Connection con = DB.getInstance().getConnection();
          try(
                PreparedStatement pronadjiKuriraS = con.prepareStatement(pronadjiKurira);    
                PreparedStatement pronadjiVoziloS = con.prepareStatement(pronadjiVozilo);
                PreparedStatement dohvKuriraUpdateS = con.prepareStatement(dohvKuriraUpdate, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                PreparedStatement p = con.prepareStatement(pronadjiKurira, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);)
              {             
              pronadjiVoziloS.setString(1, string1);
              ResultSet rs = pronadjiVoziloS.executeQuery();
              if (rs.next()) {
                
                  idVozila = rs.getInt("IdVoz");
                  System.out.println(idVozila);
              }
              else {
                  System.out.println("Nema tog vozila");
                  return false; // nema tog vozila;
              }
              
              pronadjiKuriraS.setString(1, string);
              ResultSet rs0 = pronadjiKuriraS.executeQuery();
              if (rs0.next()) {  
                 idKurira = rs0.getInt("IdKor"); 
                 System.out.println(idKurira);
              }
              else {
                  System.out.println("Nema tog korisnika");
                  return false; // nema tog usera
              }
              dohvKuriraUpdateS.setInt(1, idKurira);
              ResultSet rs2 = dohvKuriraUpdateS.executeQuery();
              if (rs2.next()){
                    rs2.updateInt("IdVoz", idVozila);
                    rs2.updateRow();  
              }
        
              return true;
              
          } catch (SQLException ex) {
              Logger.getLogger(ia210495_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
          }
          return true;
       // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<String> getAllCourierRequests() // vrati sve username od svih korisnika koji imaju neki zahtev
    {
        //TODO
         String pronadjiPakete = "SELECT* FROM ZahtevZaKurira Z JOIN Korisnik K ON Z.IdKor = K.IdKor" ;
         List<String> listaKurira = new ArrayList<>();
         Connection con = DB.getInstance().getConnection();
        try(
                Statement s = con.createStatement();
                )
            {
  
            ResultSet paketi = s.executeQuery(pronadjiPakete);
            while(paketi.next()) {
                listaKurira.add(paketi.getString("korIme"));
            }
            return listaKurira;
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }   catch (SQLException ex) {
            Logger.getLogger(ia210495_packageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaKurira;
       // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean grantRequest(String string)  {
        //TODO
        Connection con = DB.getInstance().getConnection();
        String upit =  "{ call OdobriZahtev(?, ?)}";
        try(CallableStatement cs = con.prepareCall(upit); ) {
            cs.setString(1, string);
            cs.registerOutParameter(2, java.sql.Types.INTEGER);          
            cs.execute();
            int povratna = cs.getInt(2);
            if (povratna == -1) return false;
            else return true;
        } catch (SQLException ex) {
            Logger.getLogger(ia210495_courierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
       // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
     public static void main(String[] args) {
        ia210495_GeneralOperations er = new ia210495_GeneralOperations();
        ia210495_userOperations us = new ia210495_userOperations();
        ia210495_courierOperations kurir = new ia210495_courierOperations();
        ia210495_courierRequestOperation kurirOperacija = new ia210495_courierRequestOperation();
        ia210495_VehicleOperations vehicle = new ia210495_VehicleOperations();
       
        er.eraseAll();
        us.insertUser("Username1231", "Username1231", "Username1231", "Username1231");
        vehicle.insertVehicle("BG123", 0, BigDecimal.ONE);
        vehicle.insertVehicle("BG1234", 0, BigDecimal.ONE);
       // kurir.insertCourier("Username1231", "BG123");
        kurirOperacija.insertCourierRequest("Username1231", "BG123");
        List<String> l = kurirOperacija.getAllCourierRequests();
        for(String s: l){
            System.out.println(s);
        }
     //   kurirOperacija.grantRequest("Username1231");
        
        //kurirOperacija.changeVehicleInCourierRequest("Username1231", "BG1234");
    }
}
