import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;

public class VehicleRentalTest {

    @Test
    public void testLicensePlateValidation() {
        // Valid license plates
        Vehicle vehicle1 = new Vehicle("AAA100");
        Vehicle vehicle2 = new Vehicle("ABC567");
        Vehicle vehicle3 = new Vehicle("ZZZ999");

        // Invalid license plates
        Vehicle vehicle4 = new Vehicle("");
        Vehicle vehicle5 = new Vehicle(null); 
        Vehicle vehicle6 = new Vehicle("AAA1000"); 
        Vehicle vehicle7 = new Vehicle("ZZZ99"); 

        // Test valid license plates
        assertTrue(vehicle1.validateLicensePlate(), "AAA100 should be valid.");
        assertTrue(vehicle2.validateLicensePlate(), "ABC567 should be valid.");
        assertTrue(vehicle3.validateLicensePlate(), "ZZZ999 should be valid.");

        // Test invalid license plates
        assertFalse(vehicle4.validateLicensePlate(), "Empty string should be invalid.");
        assertFalse(vehicle5.validateLicensePlate(), "Null should be invalid.");
        assertFalse(vehicle6.validateLicensePlate(), "AAA1000 should be invalid.");
        assertFalse(vehicle7.validateLicensePlate(), "ZZZ99 should be invalid.");
    }
}


