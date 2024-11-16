/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package student;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.GeneralOperations;
import student.DB;
/**
 *
 * @author Korisnik
 */
public class ia210495_GeneralOperations implements GeneralOperations{

    @Override
    public void eraseAll() {
        String[] upiti = {
        "DELETE FROM Ponuda",
        "DELETE FROM ZahtevZaKurira",     
        "DELETE FROM Kurir",
        "DELETE FROM Paket",
        "DELETE FROM Administrator",
        "DELETE FROM Korisnik",
        "DELETE FROM Opstina",   
        "DELETE FROM Vozilo",
        "DELETE FROM Grad",
        "DELETE FROM Voznja"
        };
      
        Connection con = DB.getInstance().getConnection();
        for (int i = 0; i < upiti.length; i++) {
            try (

                 PreparedStatement p = con.prepareStatement(upiti[i]);
                    )
            {
             //   System.out.println(upiti[i]);
              p.executeUpdate();

            } catch (SQLException ex) {
                Logger.getLogger(ia210495_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
       
        
         // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
