package Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import Database.Database;

/**
 *
 * @author Gilang Ramadhan
 */
public class Car {
    InputStreamReader inputStreamReader = new InputStreamReader(System.in);
    
    BufferedReader input = new BufferedReader(inputStreamReader);

    public void showMenuMobil() {
        String ulang = "";
        
        do {            
            System.out.println("==============================");
            System.out.println("          Menu Mobil          ");
            System.out.println("==============================");
            System.out.println("1. Tambah Mobil");
            System.out.println("2. List Mobil");
            System.out.println("3. Ubah Status Mobil");
            System.out.println("==============================");
            System.out.print("Pilih Menu: ");
            
            try {
                int menu = Integer.parseInt(input.readLine().trim());
                
                switch(menu) {
                    case 1:
                        addCar();
                    
                        break;
                    case 2:
                        carList();

                        break;
                    case 3:
                        changeCarStatus("", "");

                        break;
                    default:
                        System.out.println("Pilihan Menu Tidak Tersedia");

                        break;
                }
                System.out.println("==============================");
                System.out.print("Mulai Ulang Menu Mobil? (Y): ");
                
                ulang = input.readLine().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (ulang.equalsIgnoreCase("Y"));
    }
    
    protected void carList() {
        String sql = "SELECT * FROM cars";
        
        try {
            Database.rs = Database.stmt.executeQuery(sql);
            
            System.out.format("============================================================================%n");
            System.out.format("                                List Mobil                                  %n");
            System.out.format("============================================================================%n");
            System.out.format("Merek           | Plat Nomor      | Harga Sewa      | Status Mobil    %n");
            System.out.format("============================================================================%n");

            String tabelFormat = "%-15s | %-15s | %-15d | %-15s %n";

            while (Database.rs.next()) {
                String brand = Database.rs.getString("brand");
                String numberPlate = Database.rs.getString("numberPlate");
                
                int rentalPrice = Database.rs.getInt("rentalPrice");
                int status = Database.rs.getInt("status");

                System.out.format(tabelFormat, brand, numberPlate, rentalPrice, status == 0 ? "Tersedia" : status == 1 ? "Tidak Tersedia" : "Perbaikan" );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected void changeCarStatus(String numberPlate, String type) {
        String ulang = "";
        
        do {
            try {
                if(type.equalsIgnoreCase("addOrder")) {
                    String sql = String.format("UPDATE cars SET status = 1 WHERE numberPlate = '%s'", numberPlate);

                    Database.stmt.execute(sql);
                } else if(type.equalsIgnoreCase("changeStatusOrder")) {
                    String sql = String.format("UPDATE cars SET status = 0 WHERE numberPlate = '%s'", numberPlate);

                    Database.stmt.execute(sql);
                } else {
                    System.out.println("=====================================");
                    System.out.println("          Ubah Status Mobil          ");
                    System.out.println("=====================================");

                    System.out.print("Plat Nomor: ");

                    numberPlate = input.readLine().trim();

                    boolean isValid = isValidCar(numberPlate);

                    if(!isValid) {
                        System.out.println("==============================");
                        System.out.println("Plat Nomor Tidak Tersedia");
                    } else {
                        System.out.println("================================");
                        System.out.println("          Status Mobil          ");
                        System.out.println("================================");
                        System.out.println("1. Tersedia");
                        System.out.println("2. Tidak Tersedia");
                        System.out.println("3. Perbaikan");
                        System.out.println("================================");
                        System.out.print("Pilih Status: ");

                        int newStatus = Integer.parseInt(input.readLine().trim());

                        String sql = String.format("UPDATE cars SET status = '%d' WHERE numberPlate = '%s'", newStatus - 1, numberPlate);

                        Database.stmt.execute(sql);
                    }
                    
                    System.out.println("=========================================");
                    System.out.print("Mulai Ulang Menu Ubah Status Mobil? (Y): ");

                    ulang = input.readLine().trim();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (ulang.equalsIgnoreCase("Y"));
    }
    
    protected boolean isValidCar(String numberPlate) {
        String sql = String.format("SELECT numberPlate FROM cars WHERE numberPlate = '%s'", numberPlate);
        
        boolean isValid = false;
        
        try {
            Database.rs = Database.stmt.executeQuery(sql);
            
            if(Database.rs.next()) isValid = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return isValid;
    }
    
    protected String getNumberPlateCar(String orderId) {
        String sql = String.format("SELECT * FROM orders JOIN cars on orders.carId = cars.id", orderId);
        
        String numberPlate = "";
        
        try {
            Database.rs = Database.stmt.executeQuery(sql);
            
            if(Database.rs.next()) numberPlate = Database.rs.getString("numberPlate");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return numberPlate;
    }
    
    private void addCar() {
        String ulang = "";
        
        do {
            System.out.println("================================");
            System.out.println("          Tambah Mobil          ");
            System.out.println("================================");

            try {
                System.out.print("Merek: ");

                String brand = input.readLine().trim();

                System.out.print("Plat Nomor: ");

                String numberPlate = input.readLine().trim();

                boolean isFound = isValidCar(numberPlate);

                if(isFound) {
                    System.out.println("==============================");
                    System.out.println("Plat Nomor Sudah Tersedia");
                } else {
                    System.out.print("Harga Sewa: ");

                    int rentalPrice = Integer.parseInt(input.readLine().trim());

                    String sql = String.format("INSERT INTO cars (brand, numberPlate, rentalPrice) VALUE ('%s', '%s', '%d')", brand, numberPlate, rentalPrice);

                    Database.stmt.execute(sql);
                }
                
                System.out.println("==============================");
                System.out.print("Mulai Ulang Menu Tambah Mobil? (Y): ");

                ulang = input.readLine().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (ulang.equalsIgnoreCase("Y"));
    }
}
