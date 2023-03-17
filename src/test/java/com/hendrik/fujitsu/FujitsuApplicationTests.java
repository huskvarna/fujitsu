package com.hendrik.fujitsu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FujitsuApplicationTests {

	@Test
	void contextLoads() {
	}

	//Tests for regional fee calculator
	@Test
	void testCalculateRegionalFeeForTallinn() {
		DeliveryController calculator = new DeliveryController();
		double expectedFeeForCar = 4;
		double expectedFeeForScooter = 3.5;
		double expectedFeeForBike = 3;

		assertEquals(expectedFeeForCar, calculator.calculateRegionalFee("Tallinn", "Car"));
		assertEquals(expectedFeeForScooter, calculator.calculateRegionalFee("Tallinn", "Scooter"));
		assertEquals(expectedFeeForBike, calculator.calculateRegionalFee("Tallinn", "Bike"));
	}

	@Test
	void testCalculateRegionalFeeForTartu() {
		DeliveryController calculator = new DeliveryController();
		double expectedFeeForCar = 3.5;
		double expectedFeeForScooter = 3;
		double expectedFeeForBike = 2.5;

		assertEquals(expectedFeeForCar, calculator.calculateRegionalFee("Tartu", "Car"));
		assertEquals(expectedFeeForScooter, calculator.calculateRegionalFee("Tartu", "Scooter"));
		assertEquals(expectedFeeForBike, calculator.calculateRegionalFee("Tartu", "Bike"));
	}

	@Test
	void testCalculateRegionalFeeForParnu() {
		DeliveryController calculator = new DeliveryController();
		double expectedFeeForCar = 3;
		double expectedFeeForScooter = 2.5;
		double expectedFeeForBike = 2;

		assertEquals(expectedFeeForCar, calculator.calculateRegionalFee("Pärnu", "Car"));
		assertEquals(expectedFeeForScooter, calculator.calculateRegionalFee("Pärnu", "Scooter"));
		assertEquals(expectedFeeForBike, calculator.calculateRegionalFee("Pärnu", "Bike"));
	}

	@Test
	void testCalculateRegionalFeeForInvalidCity() {
		DeliveryController calculator = new DeliveryController();
		assertEquals(0, calculator.calculateRegionalFee("Invalid City", "Car"));
		assertEquals(0, calculator.calculateRegionalFee("Invalid City", "Scooter"));
		assertEquals(0, calculator.calculateRegionalFee("Invalid City", "Bike"));
	}

	@Test
	void testCalculateRegionalFeeForInvalidVehicleType() {
		DeliveryController calculator = new DeliveryController();
		assertEquals(0, calculator.calculateRegionalFee("Tallinn", "Invalid Vehicle Type"));
	}

}
