package Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import Database.Database;

/**
 *
 * @author Gilang Ramadhan
 */
public class User {
    InputStreamReader inputStreamReader = new InputStreamReader(System.in);
    
    BufferedReader input = new BufferedReader(inputStreamReader);
    
    public void showMenuUser() {
        String ulang = "";
        
        do {            
            System.out.println("==============================");
            System.out.println("          Menu User          ");
            System.out.println("==============================");
            System.out.println("1. List User");
            System.out.println("==============================");
            System.out.print("Pilih Menu: ");
            
            try {
                int menu = Integer.parseInt(input.readLine().trim());
                
                switch(menu) {
                    case 1:
                        userList();

                        break;
                    default:
                        System.out.println("Pilihan Menu Tidak Tersedia");

                        break;
                }
                System.out.println("==============================");
                System.out.print("Mulai Ulang Menu Order? (Y): ");
                
                ulang = input.readLine().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (ulang.equalsIgnoreCase("Y"));
    }
    
    public String getIdNumber(String orderId) {
        String sql = String.format("SELECT * FROM orders JOIN users on orders.userId = users.id", orderId);
        
        String idNumber = "";
        
        try {
            Database.rs = Database.stmt.executeQuery(sql);
            
            if(Database.rs.next()) idNumber = Database.rs.getString("idNumber");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return idNumber;
    }
    
    protected void addUser(String idNumber, String name, String phoneNumber) {
        try {
            boolean isFound = isValidUser(idNumber);

            if(!isFound) {
                String sql = String.format("INSERT INTO users (idNumber, name, phoneNumber) VALUE ('%s', '%s', '%s')", idNumber, name, phoneNumber);

                Database.stmt.execute(sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected void changeUserStatus(String idNumber, String type) {
        try {
            if(type.equalsIgnoreCase("addOrder")) {
                String sql = String.format("UPDATE users SET status = 1 WHERE idNumber = '%s'", idNumber);

                Database.stmt.execute(sql);
            } else if(type.equalsIgnoreCase("changeStatusOrder")) {
                String sql = String.format("UPDATE users SET status = 0 WHERE idNumber = '%s'", idNumber);

                Database.stmt.execute(sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected boolean isValidUser(String idNumber) {
        String sql = String.format("SELECT IdNumber, name, phoneNumber FROM users WHERE IdNumber = '%s'", idNumber);
        
        boolean isValid = false;
        
        try {
            Database.rs = Database.stmt.executeQuery(sql);
            
            if(Database.rs.next()) isValid = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return isValid;
    }
    
    private void userList() {
        String sql = "SELECT idNumber, name, phoneNumber, status FROM users";
        
        try {
            Database.rs = Database.stmt.executeQuery(sql);
            
            System.out.format("============================================================================%n");
            System.out.format("                                 List Order                                 %n");
            System.out.format("============================================================================%n");
            System.out.format("No. | NIK             | Nama            | Nomor HP        | Status          %n");
            System.out.format("============================================================================%n");

            String tabelFormat = "%-15s | %-15s | %-15s | %-15s %n";

            while (Database.rs.next()) {
                String idNumber = Database.rs.getString("idNumber");
                String name = Database.rs.getString("name");
                String phoneNumber = Database.rs.getString("phoneNumber");
                
                int status = Database.rs.getInt("status");

                System.out.format(tabelFormat, idNumber, name, phoneNumber, status == 1 ? "Selesai" : "Belum Selesai");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
