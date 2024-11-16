/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package student;

import java.math.BigDecimal;
import java.util.List;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.VehicleOperations;
import student.DB;


/**
 *
 * @author Korisnik
 */
public class ia210495_VehicleOperations implements VehicleOperations {

    @Override
    public boolean insertVehicle(String string, int i, BigDecimal bd) {
         String upit = "insert into Vozilo (RegistracioniBroj, TipGoriva, Potrosnja) values(?, ?, ?)";
         String proveraIsithTablica = "SELECT* FROM Vozilo WHERE RegistracioniBroj =?";
         Connection con = DB.getInstance().getConnection();
        try(
               PreparedStatement proveraTablica = con.prepareStatement(proveraIsithTablica);
                PreparedStatement p = con.prepareStatement(upit, PreparedStatement.RETURN_GENERATED_KEYS);)
            {
            proveraTablica.setString(1, string);
            ResultSet rs0 = proveraTablica.executeQuery();
            if (rs0.next()) {
                if(rs0.getString("RegistracioniBroj").equals(string)) {
                    return false;
                }
            }
            
            p.setString(1, string);
            p.setInt(2, i);
            p.setBigDecimal(3, bd);
            p.executeUpdate();                   
            ResultSet rs = p.getGeneratedKeys();
            if (rs.next()) {
                return true;
            }
            else {
                return false;
            }
                       
        } catch (SQLException ex) {
            Logger.getLogger(ia210495_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
     
    }

    @Override
    public int deleteVehicles(String... strings) {
        int brojObrisanih = 0;
        Connection con = DB.getInstance().getConnection();
        for (int i = 0; i < strings.length; i++) {
            String upit = "DELETE FROM Vozilo WHERE RegistracioniBroj = ?";
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
    }

    @Override
    public List<String> getAllVehichles() {
        List<String> lista = new ArrayList<String>();
        String upit = "SELECT RegistracioniBroj FROM Vozilo";
        Connection con = DB.getInstance().getConnection();
        try(
               
             Statement s = con.createStatement();
             ResultSet rs = s.executeQuery(upit);
             )
            {
                
            while(rs.next()) {
                String br = rs.getString("RegistracioniBroj");
                lista.add(br);
            }
            return lista;
                       
        } catch (SQLException ex) {
            Logger.getLogger(ia210495_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
       
     
        
      return lista;
    }

    @Override
    public boolean changeFuelType(String string, int i) {
        String dohvatiVozilo = "SELECT* FROM Vozilo WHERE RegistracioniBroj = ?";
        Connection con  = DB.getInstance().getConnection();
        try(PreparedStatement p = con.prepareStatement(dohvatiVozilo, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)
                 
                )
        {
            p.setString(1, string);
            ResultSet rs = p.executeQuery();
            while(rs.next()) {
                rs.updateInt("TipGoriva", i);
                rs.updateRow();
                return true;
            }
            return false;
            
        } catch (SQLException ex) {
            Logger.getLogger(ia210495_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean changeConsumption(String string, BigDecimal bd) {
        String dohvatiVozilo = "SELECT* FROM Vozilo WHERE RegistracioniBroj = ?";
        Connection con  = DB.getInstance().getConnection();
        try(PreparedStatement p = con.prepareStatement(dohvatiVozilo, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)
                 
                )
        {
            p.setString(1, string);
            ResultSet rs = p.executeQuery();
            while(rs.next()) {
                rs.updateBigDecimal("Potrosnja", bd);
                rs.updateRow();
                return true;
            }
            return false;
            
        } catch (SQLException ex) {
            Logger.getLogger(ia210495_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
       
    }
    
}
