package student;
import java.util.List;
import rs.etf.sab.operations.*;
import rs.etf.sab.tests.TestHandler;
import rs.etf.sab.tests.TestRunner;


public class StudentMain {

    public static void main(String[] args) {
        CityOperations cityOperations = new ia210495_CityOperations(); // Change this to your implementation.
        DistrictOperations districtOperations = new ia210495_districtOperations(); // Do it for all classes.
        CourierOperations courierOperations = new ia210495_courierOperations(); // e.g. = new MyDistrictOperations();
        CourierRequestOperation courierRequestOperation = new ia210495_courierRequestOperation();
        GeneralOperations generalOperations = new ia210495_GeneralOperations();
        UserOperations userOperations = new ia210495_userOperations();
        VehicleOperations vehicleOperations = new ia210495_VehicleOperations();
        PackageOperations packageOperations = new ia210495_packageOperations();

        TestHandler.createInstance(
                cityOperations,
                courierOperations,
                courierRequestOperation,
                districtOperations,
                generalOperations,
                userOperations,
                vehicleOperations,
                packageOperations);

        TestRunner.runTests();
    }
}
