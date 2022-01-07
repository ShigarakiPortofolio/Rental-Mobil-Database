package Main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import Database.Database;

import Controller.Car;
import Controller.Order;
import Controller.User;

/**
 *
 * @author Gilang Ramadhan
 */
public class Main {
    
    static InputStreamReader inputStreamReader = new InputStreamReader(System.in);
    
    static BufferedReader input = new BufferedReader(inputStreamReader);
    
    public static void main(String[] args) {
        Car carMenu = new Car();
        
        Order orderMenu = new Order();
        
        User userMenu = new User();
        
        Database connection = new Database();
        
        try {
            connection.Database();
            
            String ulang = "";
            
            do {
                System.out.println("==============================");
                System.out.println("          Menu Utama          ");
                System.out.println("==============================");
                System.out.println("1. Menu Mobil");
                System.out.println("2. Menu Order");
                System.out.println("3. Menu User");
                System.out.println("==============================");
                System.out.print("Pilih Menu: ");

                int menu = Integer.parseInt(input.readLine().trim());

                switch(menu) {
                    case 1:
                        carMenu.showMenuMobil();

                        break;
                    case 2:
                        orderMenu.showMenuOrder();

                        break;
                        
                    case 3:
                        userMenu.showMenuUser();

                        break;
                    default:
                        System.out.println("Pilihan Menu Tidak Tersedia");

                        break;
                }

                System.out.println("==============================");
                System.out.print("Mulai Ulang Program? (Y): ");

                ulang = input.readLine().trim();
            } while(ulang.equalsIgnoreCase("Y"));
            
            Database.stmt.close();
            Database.conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
