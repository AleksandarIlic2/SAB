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
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import rs.etf.sab.operations.CityOperations;


/**
 *
 * @author Korisnik
 */
public class ia210495_CityOperations implements CityOperations {
 
    @Override
    public int insertCity(String string, String string1) {        
        String dohvatiOstale = "SELECT* FROM Grad";
        String upit = "insert into Grad (Naziv, Broj) values(?, ?)";
          Connection con = DB.getInstance().getConnection();
        try(
                Statement stmt = con.createStatement();
                
                PreparedStatement p = con.prepareStatement(upit, PreparedStatement.RETURN_GENERATED_KEYS);)
            {
            ResultSet rs = stmt.executeQuery(dohvatiOstale);
            while (rs.next()) {
                if (rs.getString("Naziv").equals(string) || rs.getString("Broj").equals(string1))
                {
                    return -1;
                }
                    
            }
            p.setString(1, string);
            p.setString(2, string1);
            p.executeUpdate();                   
            ResultSet rs2 = p.getGeneratedKeys();
              //  
            if (rs2.next()) {
               
                return rs2.getInt(1);
            }
            else {
                return -1;
            }
                       
        } catch (SQLException ex) {
            Logger.getLogger(ia210495_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int deleteCity(String... strings) {

        int brojObrisanih = 0;
        Connection con = DB.getInstance().getConnection();
        for (int i = 0; i < strings.length; i++) {
            String upit = "DELETE FROM Grad WHERE Naziv = ?";
            try (
                 PreparedStatement p = con.prepareStatement(upit);
                
                )
            {
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
    public boolean deleteCity(int i) {
        String upit = "DELETE FROM Grad WHERE IdGra = ?";
        Connection con = DB.getInstance().getConnection();
        try(PreparedStatement p = con.prepareStatement(upit)
                ){
            p.setInt(1, i);
            int rs = p.executeUpdate();
            if (rs > 0) return true;
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(ia210495_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
//throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Integer> getAllCities() {
        String upit = "SELECT* FROM Grad";
        List<Integer> listaGradovaId = new ArrayList<Integer>();
        Connection con = DB.getInstance().getConnection();
        try(Statement stmt = con.createStatement();
               
                )
        {
             ResultSet rs = stmt.executeQuery(upit);
             while(rs.next()) {
                 listaGradovaId.add(rs.getInt("IdGra"));
             }
            
        } catch (SQLException ex) {
            Logger.getLogger(ia210495_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaGradovaId;
    }
    
    public static void main(String[] args) {
    ia210495_CityOperations i = new ia210495_CityOperations();
    ia210495_GeneralOperations j = new ia210495_GeneralOperations();
    
    j.eraseAll();
      String name1 = "Tokyo";
      String name2 = "Beijing";
      String postalCode1 = "100";
      String postalCode2 = "065001";
      i.insertCity(name1, postalCode1);
      i.insertCity(name2, postalCode2);
      List<Integer> list = i.getAllCities();
      Assert.assertEquals(2L, (long)list.size());
      Assert.assertEquals(2L, (long)i.deleteCity(new String[]{name1, name2}));


    j.eraseAll();
    }
    
}
