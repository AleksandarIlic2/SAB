/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import rs.etf.sab.operations.DistrictOperations;
/**
 *
 * @author Korisnik
 */
public class ia210495_districtOperations implements DistrictOperations {

    @Override
    public int insertDistrict(String string, int i, int i1, int i2) {
        String proveraPostojanjaGrada = "SELECT* FROM Grad WHERE IdGra=?";
        String upit = "INSERT INTO Opstina(Naziv, IdGra, kordinataX, kordinataY) VALUES(?, ?, ?, ?)"; 
        Connection con = DB.getInstance().getConnection();
        try(
              PreparedStatement proveraGrada = con.prepareStatement(proveraPostojanjaGrada);
              PreparedStatement p = con.prepareStatement(upit, PreparedStatement.RETURN_GENERATED_KEYS);)
          {
          proveraGrada.setInt(1, i);
          ResultSet r = proveraGrada.executeQuery();
          if (!r.next()) {
              return -1; // ne postoji taj grad
          }
          p.setString(1, string);
          p.setInt(2, i);
          p.setInt(3, i1);
          p.setInt(4, i2);
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
     //   throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int deleteDistricts(String... strings) {
        int brojObrisanih = 0;
        Connection con = DB.getInstance().getConnection();
        for (int i = 0; i < strings.length; i++) {
            String upit = "DELETE FROM Opstina WHERE Naziv=?";
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
    public boolean deleteDistrict(int i) {
        Connection con = DB.getInstance().getConnection();
        String proveraPostojanja = "SELECT* FROM Opstina WHERE IdOps=?";
        String upit = "DELETE FROM Opstina WHERE IdOps=?";
        try (
             PreparedStatement proveraPostojanjaS = con.prepareStatement(proveraPostojanja);
             PreparedStatement p = con.prepareStatement(upit);
            )
        {
            proveraPostojanjaS.setInt(1, i);
            ResultSet rs = proveraPostojanjaS.executeQuery();
            if (!rs.next()) {
                
                return false; // nema te opstine;
            }
            p.setInt(1, i);
           
            p.executeUpdate();
           
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ia210495_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int deleteAllDistrictsFromCity(String string) {
        int idGra = 0;
        Connection con = DB.getInstance().getConnection();
        String proveraPostojanja = "SELECT* FROM Opstina O JOIN Grad G ON O.IdGra = G.IdGra WHERE G.Naziv=?";
        String upit = "DELETE FROM Opstina WHERE IdGra=?";
        try (
             PreparedStatement proveraPostojanjaS = con.prepareStatement(proveraPostojanja);
             PreparedStatement p = con.prepareStatement(upit);
            )
        {
            proveraPostojanjaS.setString(1, string);
            ResultSet rs = proveraPostojanjaS.executeQuery();
            if (rs.next()) {        
                idGra = rs.getInt("IdGra");
            }
            else {
                return 0;// ne postoji grad sa tim imenom
            }
            p.setInt(1, idGra);
            int br = p.executeUpdate();
            return br;
        } catch (SQLException ex) {
            Logger.getLogger(ia210495_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0;
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Integer> getAllDistrictsFromCity(int i) {
        List<Integer> lista = new ArrayList<Integer>();
        String upit = "SELECT* FROM Opstina WHERE IdGra = ?";
        Connection con = DB.getInstance().getConnection();
        try(  
             PreparedStatement p = con.prepareStatement(upit);
             )
            {
            p.setInt(1, i);
            ResultSet rs = p.executeQuery();
            int flag = 0;
            while(rs.next()) {
                flag = 1;
                lista.add(rs.getInt("IdOps"));
            }
            if (flag == 0) return null;
            return lista;
                       
        } catch (SQLException ex) {
            Logger.getLogger(ia210495_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lista;
       // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Integer> getAllDistricts() {
        List<Integer> lista = new ArrayList<Integer>();
        String upit = "SELECT* FROM Opstina";
        Connection con = DB.getInstance().getConnection();
        try(  
             PreparedStatement p = con.prepareStatement(upit);
             )
            { 
            ResultSet rs = p.executeQuery();
            int flag = 0;
            while(rs.next()) {
                flag = 1;
                lista.add(rs.getInt("IdOps"));
            }
            if (flag == 0) return null;
            return lista;
                       
        } catch (SQLException ex) {
            Logger.getLogger(ia210495_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lista;
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    public static void main(String[] args) {
        ia210495_GeneralOperations ig = new ia210495_GeneralOperations();
        ia210495_CityOperations city = new ia210495_CityOperations();
        ia210495_districtOperations district = new ia210495_districtOperations();
        ig.eraseAll();
        int key = city.insertCity("BG", "123");
        district.insertDistrict("BG", key, 0, 0);
        List<Integer> l = district.getAllDistricts();
        for(Integer i: l){
            district.deleteDistrict(i);
            System.out.println(i);
        }
   
    }
}
