
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalSystem {
    private static RentalSystem instance;
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private RentalHistory rentalHistory = new RentalHistory();

    private RentalSystem() {
        loadData();
    }

    public static RentalSystem getInstance() {
        if (instance == null) {
            instance = new RentalSystem();
        }
        return instance;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        saveVehicle(vehicle);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        saveCustomer(customer);
    }

    public void rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
            vehicle.setStatus(Vehicle.VehicleStatus.RENTED);
            RentalRecord record = new RentalRecord(vehicle, customer, date, amount, "RENT");
            rentalHistory.addRecord(record);
            saveRecord(record);
            System.out.println("Vehicle rented to " + customer.getCustomerName());
        } else {
            System.out.println("Vehicle is not available for renting.");
        }
    }

    public void returnVehicle(Vehicle vehicle, Customer customer, LocalDate date, double extraFees) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.RENTED) {
            vehicle.setStatus(Vehicle.VehicleStatus.AVAILABLE);
            RentalRecord record = new RentalRecord(vehicle, customer, date, extraFees, "RETURN");
            rentalHistory.addRecord(record);
            saveRecord(record);
            System.out.println("Vehicle returned by " + customer.getCustomerName());
        } else {
            System.out.println("Vehicle is not rented.");
        }
    }

    private void saveVehicle(Vehicle vehicle) {
        try (FileWriter fw = new FileWriter("vehicles.txt", true)) {
            fw.write(vehicle.getLicensePlate() + "," +
                    vehicle.getMake() + "," +
                    vehicle.getModel() + "," +
                    vehicle.getYear() + "," +
                    vehicle.getStatus() + "," +
                    (vehicle instanceof Car ? "Car" : "Motorcycle") + "\n");
        } catch (IOException e) {
            System.out.println("Error saving vehicle: " + e.getMessage());
        }
    }

    private void saveCustomer(Customer customer) {
        try (FileWriter fw = new FileWriter("customers.txt", true)) {
            fw.write(customer.getCustomerId() + "," +
                    customer.getCustomerName() + "\n");
        } catch (IOException e) {
            System.out.println("Error saving customer: " + e.getMessage());
        }
    }

    private void saveRecord(RentalRecord record) {
        try (FileWriter fw = new FileWriter("rental_records.txt", true)) {
            fw.write(
                    record.getCustomer().getCustomerId() + "," +
                    record.getVehicle().getLicensePlate() + ",");

        } catch (IOException e) {
            System.out.println("Error saving rental record: " + e.getMessage());
        }
    }

    private void loadData() {
            try (BufferedReader br = new BufferedReader(new FileReader("vehicles.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String LicensePlate = parts[0];
                String make = parts[1];
                String model = parts[2];
                int year = Integer.parseInt(parts[3]);
                Vehicle.VehicleStatus status = Vehicle.VehicleStatus.valueOf(parts[4]);
            }
        } catch (IOException e) {
            System.out.println("Could not load vehicles: " + e.getMessage());
        }

     
        try (BufferedReader br = new BufferedReader(new FileReader("customers.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                customers.add(new Customer(id, name));
            }
        } catch (IOException e) {
            System.out.println("Could not load customers: " + e.getMessage());
        }

        try (BufferedReader br = new BufferedReader(new FileReader("rental_records.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int customerId = Integer.parseInt(parts[0]);
                String plate = parts[1];
                LocalDate date = LocalDate.parse(parts[2]);
                double amount = Double.parseDouble(parts[3]);
                String type = parts[4];

                Customer customer = findCustomerById(customerId);
                Vehicle vehicle = findVehicleByPlate(plate);

                if (customer != null && vehicle != null) {
                    RentalRecord record = new RentalRecord(vehicle, customer, date, amount, type);
                    rentalHistory.addRecord(record);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not load rental records: " + e.getMessage());
        }
    }

    public void displayAvailableVehicles() {
        System.out.println("|     Type         |\tPlate\t|\tMake\t|\tModel\t|\tYear\t|");
        System.out.println("---------------------------------------------------------------------------------");
        for (Vehicle v : vehicles) {
            if (v.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
                System.out.println("|     " + (v instanceof Car ? "Car          " : "Motorcycle   ") + "|\t" + v.getLicensePlate() + "\t|\t" + v.getMake() + "\t|\t" + v.getModel() + "\t|\t" + v.getYear() + "\t|");
            }
        }
        System.out.println();
    }

    public void displayAllVehicles() {
        for (Vehicle v : vehicles) {
            System.out.println("  " + v.getInfo());
        }
    }

    public void displayAllCustomers() {
        for (Customer c : customers) {
            System.out.println("  " + c.toString());
        }
    }

    public void displayRentalHistory() {
        for (RentalRecord record : rentalHistory.getRentalHistory()) {
            System.out.println(record.toString());
        }
    }

    public Vehicle findVehicleByPlate(String plate) {
        for (Vehicle v : vehicles) {
            if (v.getLicensePlate().equalsIgnoreCase(plate)) {
                return v;
            }
        }
        return null;
    }

    public Customer findCustomerById(int id) {
        for (Customer c : customers) {
            if (c.getCustomerId() == id) {
                return c;
            }
        }
        return null;
    }

    public Customer findCustomerByName(String name) {
        for (Customer c : customers) {
            if (c.getCustomerName().equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }
}