package com.evancharlton.mileage.calculators;

public class GallonsKilometersToMPGCalculationEngine extends USCalculationEngine {
	@Override
	public double calculateEconomy(double distance, double fuel) {
		distance = kmToMiles(distance);
		return super.calculateEconomy(distance, fuel);
	}

	@Override
	public String getDistanceUnits() {
		return " Kilometers";
	}

	@Override
	public String getDistanceUnitsAbbr() {
		return " K";
	}
}
