/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package student;
import java.sql.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import rs.etf.sab.operations.CourierOperations;
/**
 *
 * @author Korisnik
 */
public class ia210495_courierOperations implements CourierOperations{

    @Override
    public boolean insertCourier(String string, String string1) {
          String pronadjiVozilo = "SELECT* FROM Vozilo WHERE RegistracioniBroj=?";
          String pronadjiKurira = "SELECT* FROM Korisnik WHERE korIme = ?";
          int idVozila = 0;
          int idKurira = 0;
          String upit = "insert into Kurir (IdKor, VoziloId, BrojPaketaIsporucenih, Status, Profit) values(?, ?, ?, ?, ?)";
          Connection con = DB.getInstance().getConnection();
        try(
               PreparedStatement pstmt = con.prepareStatement(pronadjiVozilo);
               PreparedStatement pronadjiKuriraS = con.prepareStatement(pronadjiKurira);    
               PreparedStatement upisiKurira = con.prepareStatement(upit, PreparedStatement.RETURN_GENERATED_KEYS);)
            {
            pronadjiKuriraS.setString(1, string);
            ResultSet rs0 = pronadjiKuriraS.executeQuery();
            if (rs0.next()) {
               idKurira = rs0.getInt("IdKor");
            }
            else {
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
            
            upisiKurira.setInt(1, idKurira);
            upisiKurira.setInt(2, idVozila);
            upisiKurira.setInt(3, 0);
            upisiKurira.setInt(4, 0);
            upisiKurira.setFloat(5, (float) 0.0);
            int val = upisiKurira.executeUpdate();
            return true;
        
                       
        } catch (SQLException ex) {
            Logger.getLogger(ia210495_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
     
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean deleteCourier(String string) {
      
          String pronadjiKurira = "SELECT* FROM Korisnik WHERE korIme = ?";
          int idVozila = 0;
          int idKurira = 0;
          String upit = "DELETE FROM Kurir WHERE IdKor=?";
          Connection con = DB.getInstance().getConnection();
        try(
           
               PreparedStatement pronadjiKuriraS = con.prepareStatement(pronadjiKurira);    
               PreparedStatement obrisiKurira = con.prepareStatement(upit);)
            {
            pronadjiKuriraS.setString(1, string);
            ResultSet rs0 = pronadjiKuriraS.executeQuery();
            if (rs0.next()) {
               System.out.println("Kurir: " + rs0.getInt("IdKor"));
               idKurira = rs0.getInt("IdKor");
            }
            else {
                return false; // nema tog usera
            }
            obrisiKurira.setInt(1, idKurira);
            int val = obrisiKurira.executeUpdate();
            System.out.println(val);
            return true;
        
                       
        } catch (SQLException ex) {
            Logger.getLogger(ia210495_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
     
    }

    @Override
    public List<String> getCouriersWithStatus(int i) {
          int idVozila = 0;
          List<String> listaKurira = new ArrayList<String>();
          String pronadjiKurira = "SELECT* FROM Kurir K1 JOIN Korisnik K2 ON K1.IdKor = K2.IdKor WHERE Status =?";
          
          Connection con = DB.getInstance().getConnection();
        try(
           
               PreparedStatement pronadjiKuriraS = con.prepareStatement(pronadjiKurira);    
               )
            {
            pronadjiKuriraS.setInt(1, i);
            ResultSet rs0 = pronadjiKuriraS.executeQuery();
            while (rs0.next()) {             
               listaKurira.add(rs0.getString("korIme"));
            }
     
            return listaKurira;             
        } catch (SQLException ex) {
            Logger.getLogger(ia210495_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaKurira;
       // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<String> getAllCouriers() {
         int idVozila = 0;
          List<String> listaKurira = new ArrayList<String>();
          String pronadjiKurira = "SELECT* FROM Kurir K1 JOIN Korisnik K2 ON K1.IdKor = K2.IdKor ORDER BY Profit DESC";
          
          Connection con = DB.getInstance().getConnection();
        try(
           
               PreparedStatement pronadjiKuriraS = con.prepareStatement(pronadjiKurira);    
               )
            {
            ResultSet rs0 = pronadjiKuriraS.executeQuery();
            while (rs0.next()) {             
               listaKurira.add(rs0.getString("korIme"));
            }
     
            return listaKurira;             
        } catch (SQLException ex) {
            Logger.getLogger(ia210495_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaKurira;
       // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public BigDecimal getAverageCourierProfit(int i) // moze za AVG da se odradi vrv, proveri da li bi tako radilo za vise 
    {
        int idVozila = 0;
        List<Double> listaKurira = new ArrayList<Double>();
        String pronadjiKurira = "SELECT* FROM Kurir K1 JOIN Korisnik K2 ON K1.IdKor = K2.IdKor WHERE BrojPaketaIsporucenih >= ?";
          
        Connection con = DB.getInstance().getConnection();
        try(
               PreparedStatement pronadjiKuriraS = con.prepareStatement(pronadjiKurira);    
               )
            {
            pronadjiKuriraS.setInt(1, i);
            ResultSet rs0 = pronadjiKuriraS.executeQuery();
            while (rs0.next()) {             
               listaKurira.add(rs0.getDouble("Profit"));
            }
            Double b = 0.0;
            for (Double temp: listaKurira){
                b += temp;
            }
            
            return BigDecimal.valueOf(b / listaKurira.size());             
        } catch (SQLException ex) {
            Logger.getLogger(ia210495_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public static void main(String[] args) {
        ia210495_courierOperations kurir = new ia210495_courierOperations();
        ia210495_VehicleOperations veh = new ia210495_VehicleOperations();
        ia210495_userOperations user = new ia210495_userOperations();
        ia210495_GeneralOperations gOp = new ia210495_GeneralOperations();
        gOp.eraseAll();
        String username = "crno.dete";
        String firstName = "Svetislav";
        String lastName = "Kisprdilov";
        String password = "sisatovac123";
        Assert.assertTrue(user.insertUser(username, firstName, lastName, password));
        String licencePlateNumber = "BG234DU";
        BigDecimal fuelConsumption = new BigDecimal(6.3D);
        int fuelType = 1;
        Assert.assertTrue(veh.insertVehicle(licencePlateNumber, fuelType, fuelConsumption));
        kurir.insertCourier("crno.dete", "BG234DU");
      
        List<String> lista = new ArrayList<>();
        lista = kurir.getAllCouriers();
        for (String s: lista){
            System.err.println(s);
        }
     
    }
}
