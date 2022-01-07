package Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import Database.Database;

/**
 *
 * @author Gilang Ramadhan
 */
public class Order extends Car {
    InputStreamReader inputStreamReader = new InputStreamReader(System.in);
    
    BufferedReader input = new BufferedReader(inputStreamReader);
       
    User user = new User();

    public void showMenuOrder() {
        String ulang = "";
        
        do {            
            System.out.println("==============================");
            System.out.println("          Menu Order          ");
            System.out.println("==============================");
            System.out.println("1. Tambah Order");
            System.out.println("2. List Order");
            System.out.println("3. Ubah Status Order");
            System.out.println("==============================");
            System.out.print("Pilih Menu: ");
            
            try {
                int menu = Integer.parseInt(input.readLine().trim());
                
                switch(menu) {
                    case 1:
                        addOrder();
                    
                        break;
                    case 2:
                        orderList();

                        break;
                    case 3:
                        changeOrderStatus();

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
    
    private void addOrder() {
        String ulang = "";
        
        do {
            carList();
            
            System.out.println("================================");
            System.out.println("          Tambah Order          ");
            System.out.println("================================");

            try {
                System.out.print("NIK: ");

                String idNumber = input.readLine().trim();

                System.out.print("Nama: ");

                String name = input.readLine().trim();

                System.out.print("Nomor HP: ");

                String phoneNumber = input.readLine().trim();

                System.out.print("Plat Nomor: ");
                
                user.addUser(idNumber, name, phoneNumber);

                String numberPlate = input.readLine().trim();

                boolean isValid = isValidCar(numberPlate);

                if(!isValid) {
                    System.out.println("==============================");
                    System.out.println("Plat Nomor Tidak Tersedia");
                } else {
                    String sql = "SELECT id, idNumber, status FROM users WHERE idNumber = '%s'";
                    
                    sql = String.format(sql, idNumber);
                    
                    Database.rs = Database.stmt.executeQuery(sql);
                    
                    int getUserId = 0;
                    
                    int getUserStatus = 0;
                    
                    if(Database.rs.next()) {
                        getUserId = Database.rs.getInt("id");
                        getUserStatus = Database.rs.getInt("status");
                    }
                    
                    sql = "SELECT id, brand, numberPlate, rentalPrice, status FROM cars WHERE numberPlate = '%s'";
                    
                    sql = String.format(sql, numberPlate);
                    
                    Database.rs = Database.stmt.executeQuery(sql);
                    
                    int getCarId = 0;
                    
                    String getBrand = "";
                    
                    int getRentalPrice = 0;
                    
                    int getCarStatus = 0;
                    
                    if(Database.rs.next()) {
                        getCarId = Database.rs.getInt("id");
                        
                        getBrand = Database.rs.getString("brand");
                        
                        getRentalPrice = Database.rs.getInt("rentalPrice");
                        
                        getCarStatus = Database.rs.getInt("status");
                    }
                    
                    if(getCarStatus == 0) {
                        System.out.print("Lama Sewa: ");

                        int longLease = Integer.parseInt(input.readLine().trim());

                        System.out.print("Deposit: ");

                        int deposit = Integer.parseInt(input.readLine().trim());

                        LocalDateTime date = LocalDateTime.now();

                        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
                        DateTimeFormatter orderDateFormat = DateTimeFormatter.ofPattern("ddMMyyyy/HHmmss");

                        String rentalDate = dateFormat.format(date);
                        String returnDate = dateFormat.format(date.plusDays(longLease));

                        String orderId = orderDateFormat.format(date);

                        int totalPay = getRentalPrice * longLease;

                        int remainingPay = totalPay - deposit;

                        sql = "INSERT INTO orders (orderId, userId, carId, rentalDate, longLease, returnDate, totalPay, deposit, remainingPay) VALUE ('%s', '%d', '%d', '%s', '%d', '%s', '%d', '%d', '%d')";

                        sql = String.format(sql, orderId, getUserId, getCarId, rentalDate, longLease, returnDate, totalPay, deposit, remainingPay);

                        Database.stmt.execute(sql);

                        changeCarStatus(numberPlate, "addOrder");

                        paymentSlip(orderId, idNumber, rentalDate, returnDate, numberPlate, getBrand, getRentalPrice, deposit, totalPay, remainingPay);
                    } else if (getUserStatus == 1) {
                        System.out.println("==============================");
                        System.out.println("Status Transaksi User Belum Selesai");
                    } else {
                        System.out.println("==============================");
                        System.out.println("Status Mobil Tidak Tersedia");
                    }
                }
                
                System.out.println("==============================");
                System.out.print("Mulai Ulang Menu Tambah Order? (Y): ");

                ulang = input.readLine().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (ulang.equalsIgnoreCase("Y"));
    }
    
    private void changeOrderStatus() {
        String ulang = "";
        
        do {
            try {
                System.out.println("=====================================");
                System.out.println("          Ubah Status Order          ");
                System.out.println("=====================================");

                System.out.print("Order Id: ");

                String orderId = input.readLine().trim();
                
                String numberPlate = getNumberPlateCar(orderId);
                
                String idNumber = user.getIdNumber(orderId);
                
                if(!numberPlate.equalsIgnoreCase("")) {                    
                    String sql = String.format("UPDATE orders SET status = 1 WHERE orderId = '%s'", orderId);

                    Database.stmt.execute(sql);

                    changeCarStatus(numberPlate, "changeOrderStatus");
                    
                    user.changeUserStatus(idNumber, "changeOrderStatus");
                }

                System.out.println("=========================================");
                System.out.print("Mulai Ulang Menu Ubah Status Order? (Y): ");

                ulang = input.readLine().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (ulang.equalsIgnoreCase("Y"));
    }
    
    private void orderList() {
        String sql = "SELECT * FROM orders JOIN users ON orders.userId = users.id JOIN cars on orders.carId = cars.id";
        
        try {
            Database.rs = Database.stmt.executeQuery(sql);
            
            System.out.format("===============================================================================================================================================================================================%n");
            System.out.format("                                                                                          List Order                                                                                           %n");
            System.out.format("===============================================================================================================================================================================================%n");
            System.out.format("Order ID        | NIK             | Nama                 | Nomor HP        | Plat Mobil      | Tanggal Sewa         | Lama Sewa  | Tanggal Kembali      | Total Bayar     | Status       %n");
            System.out.format("===============================================================================================================================================================================================%n");

            String tabelFormat = "%-15s | %-15s | %-20s | %-15s | %-15s | %-20s | %-10d | %-20s | %-15d | %-12s %n";

            while (Database.rs.next()) {
                String orderId = Database.rs.getString("orderId");
                String idNumber = Database.rs.getString("idNumber");
                String name = Database.rs.getString("name");
                String phoneNumber = Database.rs.getString("phoneNumber");
                String numberPlate = Database.rs.getString("numberPlate");
                
                String rentalDate = Database.rs.getString("rentalDate");
                
                int longLease = Database.rs.getInt("longLease");
                
                String returnDate = Database.rs.getString("returnDate");
                
                int totalPay = Database.rs.getInt("totalPay");
                int status = Database.rs.getInt("status");

                System.out.format(tabelFormat, orderId, idNumber, name, phoneNumber, numberPlate, rentalDate, longLease, returnDate, totalPay, status == 1 ? "Selesai" : "Belum Selesai");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void paymentSlip(String orderId, String NIK, String tanggalSewa, String tanggalKembali, String platNomor, String merek, int hargaSewa, int deposit, int totalBayar, int sisaBayar) {
        System.out.format("========================================================================================================================================================================%n");
        System.out.format("                                                                 Slip Pembayaran                                                                                        %n");
        System.out.format("========================================================================================================================================================================%n");
        
        String tabelFormat = "%-15s | %-15s | %-15s | %-15s | %-15s | %-15s | %-10d | %-10d | %-15d | %-15d %n";
        
        System.out.format("Order ID        | NIK             | Tanggal Sewa    | Tanggal Kembali | Plat Mobil      | Merek Mobil     | Harga Sewa | Deposit    | Total Bayar     | Sisa Bayar      %n");
        System.out.format("========================================================================================================================================================================%n");
        
        System.out.format(tabelFormat, orderId, NIK, tanggalSewa, tanggalKembali, platNomor, merek, hargaSewa, deposit, totalBayar, sisaBayar);
    }
   
}
