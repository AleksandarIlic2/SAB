/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package student;
import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import rs.etf.sab.operations.UserOperations;
/**
 *
 * @author Korisnik
 */
public class ia210495_userOperations implements UserOperations{

    @Override
    public boolean insertUser(String string, String string1, String string2, String string3) {
        /*if (Character.isUpperCase(string1.charAt(0)) == false || Character.isUpperCase(string2.charAt(0)) == false){
            return false;
        }
        if (string3.length() <= 8) return false;
        for (int i = 0; i < string3.length(); i++) {
            char ct = string3.charAt(i);
            if (Character.isDigit(ct) == false && Character.isLetter(ct) == false)
                return false;
        }*/
        String sviUseri = "SELECT* FROM Korisnik";
         String upit = "insert into Korisnik (korIme, Ime, Prezime, Sifra, BrojPaketa ) values(?, ?, ?, ?, ?)";
          Connection con = DB.getInstance().getConnection();
        try(
                Statement stmt = con.createStatement();
                
                PreparedStatement p = con.prepareStatement(upit, PreparedStatement.RETURN_GENERATED_KEYS);)
            {
            ResultSet rs = stmt.executeQuery(sviUseri);
            while(rs.next()) {
                if (rs.getString("korIme").equals(string))
                    return false; // user vec postoji
            }
            p.setString(1, string);
            p.setString(2, string1);
            p.setString(3, string2);
            p.setString(4, string3);
            p.setInt(5, 0);
            p.executeUpdate();
            ResultSet rs2 = p.getGeneratedKeys();
            if (rs2.next()) {
                return true;
            }
            else {
                return false;
            }
                       
        } catch (SQLException ex) {
            Logger.getLogger(ia210495_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
     
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int declareAdmin(String string) {
         String sviUseri = "SELECT* FROM Korisnik";
         String noviAdmin = "INSERT INTO Administrator(IdKor) VALUES(?)";
         String dohvatiKorisnika = "SELECT* FROM Administrator A INNER JOIN Korisnik K ON K.IdKor = A.IdKor WHERE K.korIme = ?";
     //    String upit = "insert into Korisnik (korIme, Ime, Prezime, Sifra, BrojPaketa ) values(?, ?, ?, ?, ?)";
          Connection con = DB.getInstance().getConnection();
        try(
                Statement stmt = con.createStatement();
                PreparedStatement p2 = con.prepareStatement(dohvatiKorisnika);
                PreparedStatement p3 = con.prepareStatement(noviAdmin);
                )
            {
            ResultSet rs = stmt.executeQuery(sviUseri);
            int flag = 0;
            int idKor = 0;
            while(rs.next()) {
                if (rs.getString("korIme").equals(string))
                {
                    idKor = rs.getInt("IdKor");
                    flag = 1;
                }
                    
            }
            if (flag == 0) return 2; // lack of given user;
            p2.setString(1, string);
            ResultSet rs2 = p2.executeQuery();
           
            if(!rs2.next()) {
                
                p3.setInt(1, idKor);
                p3.executeUpdate();
                return 0; // success
                //rs2.updateInt("TipGoriva", i);
              //  rs2.updateRow();
            }
            else {
                return 1;// already admin
            }
                     
        } catch (SQLException ex) {
            Logger.getLogger(ia210495_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
     
        return 0;
    }

    @Override
    public Integer getSentPackages(String... strings) { // TODO
        int brojPaketa =0 ;
        Connection con = DB.getInstance().getConnection();
        String upit = "SELECT* FROM Korisnik WHERE korIme =?";
        for (int i = 0; i < strings.length; i++) {
            
            try (
                 PreparedStatement p = con.prepareStatement(upit);
                
                )
            {
                p.setString(1, strings[i]);
                ResultSet rs = p.executeQuery();
                if (rs.next()){
                    brojPaketa += rs.getInt("brojPaketa");
                }
                
                else {
                    return null;
                }
                
               
            } catch (SQLException ex) {
                Logger.getLogger(ia210495_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
        
        return brojPaketa;
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int deleteUsers(String... strings) {
         int brojObrisanih = 0;
         int korId = 0;
        Connection con = DB.getInstance().getConnection();
        for (int i = 0; i < strings.length; i++) {
            String upit = "DELETE FROM Korisnik WHERE korIme = ?";
            String upitAdmin = "SELECT* FROM Administrator A INNER JOIN Korisnik K ON K.IdKor = A.IdKor WHERE K.korIme =?";
            String obrisiAdmine = "DELETE FROM Administrator WHERE IdKor =?";
            
            try (
                 PreparedStatement p = con.prepareStatement(upit);
                 PreparedStatement pAdmini = con.prepareStatement(upitAdmin);
                 PreparedStatement pObrisiAdmine = con.prepareStatement(obrisiAdmine);
                 
                )
            {
                pAdmini.setString(1, strings[i]);
                ResultSet rs = pAdmini.executeQuery();
                if (rs.next()) {
                    korId = rs.getInt("IdKor");
                    pObrisiAdmine.setString(1, strings[i]);
                    pObrisiAdmine.executeUpdate();
                }
                
                p.setString(1, strings[i]);              
                brojObrisanih += p.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(ia210495_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
        return brojObrisanih; 
       // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<String> getAllUsers() {
       List<String> imena = new ArrayList<String>();
       String upit = "SELECT* FROM Korisnik";
       Connection con = DB.getInstance().getConnection();
       try(Statement stmt = con.createStatement();
               
               
               ){
           ResultSet rs = stmt.executeQuery(upit);
           while(rs.next()) {
               imena.add(rs.getString("korIme"));
           }
           return imena;
       } catch (SQLException ex) {
            Logger.getLogger(ia210495_userOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
       return imena;
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public static void main(String[] args) {
    ia210495_userOperations i = new ia210495_userOperations();


    }
    
}
