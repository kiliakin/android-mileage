package com.evancharlton.mileage.calculators;

import com.evancharlton.mileage.PreferencesProvider;

public class CalculationEngine {
	private int m_inputVolume;
	private int m_inputDistance;
	private int m_outputVolume;
	private int m_outputDistance;
	private boolean m_inverse = false;

	public double litresToGallons(double amount) {
		return amount *= 0.264172052; // number of gallons per litre
	}

	public double gallonsToLitres(double amount) {
		return amount *= 3.78541178; // number of litres per gallon
	}

	public double milesToKM(double amount) {
		return amount *= 1.609344; // number of kilometers per mile
	}

	public double milesToCkm(double miles) {
		return milesToKM(miles) * 100;
	}

	public double kmToMiles(double amount) {
		return amount *= 0.621371192; // number of miles per kilometer
	}

	public double gallonsToImpGallons(double gallons) {
		return gallons *= 0.83267384; // number of imperial gallons per gallon
	}

	public double impGallonsToGallons(double gallons) {
		return gallons *= 1.20095042; // number of gallons per imperial gallon
	}

	public double impGallonsToLitres(double gallons) {
		return gallons *= 4.54609188; // number of litres per imperial gallon
	}

	public double litresToImpGallons(double litres) {
		return litres *= 0.219969157; // number of imperial gallons per litre
	}

	public String help() {
		StringBuilder sb = new StringBuilder();
		sb.append("This system expects:\n");
		sb.append("  fuel: ").append(getVolumeUnits()).append("\n");
		sb.append("  odometer: ").append(getDistanceUnits()).append("\n");
		sb.append("  economy: ").append(getEconomyUnits());
		return sb.toString();
	}

	public void setInputVolume(int volume) {
		m_inputVolume = volume;
	}

	public void setInputDistance(int distance) {
		m_inputDistance = distance;
	}

	public void setOutputVolume(int volume) {
		m_outputVolume = volume;
	}

	public void setOutputDistance(int distance) {
		m_outputDistance = distance;
	}

	public void setEconomy(int economy) {
		switch (economy) {
			case PreferencesProvider.MI_PER_GALLON:
				setOutputDistance(PreferencesProvider.MILES);
				setOutputVolume(PreferencesProvider.GALLONS);
				m_inverse = false;
				break;
			case PreferencesProvider.LITRES_PER_CKM:
				setOutputDistance(PreferencesProvider.KILOMETERS_100);
				setOutputVolume(PreferencesProvider.LITRES);
				m_inverse = true;
		}
	}

	public double calculateEconomy(double distance, double fuel) {
		double kilometers = distance;
		double litres = fuel;
		switch (m_inputDistance) {
			case PreferencesProvider.MILES:
				kilometers = milesToKM(distance);
				break;
			case PreferencesProvider.KILOMETERS:
				// do nothing
				break;
		}
		switch (m_inputVolume) {
			case PreferencesProvider.GALLONS:
				litres = gallonsToLitres(fuel);
				break;
			case PreferencesProvider.LITRES:
				// do nothing
				break;
			case PreferencesProvider.IMP_GALLONS:
				litres = impGallonsToLitres(fuel);
				break;
		}

		switch (m_outputDistance) {
			case PreferencesProvider.MILES:
				distance = kmToMiles(kilometers);
				break;
			case PreferencesProvider.KILOMETERS:
				distance = kilometers * 100;
				break;
		}
		switch (m_outputVolume) {
			case PreferencesProvider.GALLONS:
				fuel = litresToGallons(litres);
				break;
			case PreferencesProvider.LITRES:
				fuel = litres;
				break;
			case PreferencesProvider.IMP_GALLONS:
				fuel = litresToImpGallons(litres);
				break;
		}
		double ratio = distance / fuel;
		if (m_inverse) {
			ratio = 1 / ratio;
		}
		return ratio;
	}

	public String getEconomyUnits() {
		String d;
		String v;
		switch (m_outputDistance) {
			case PreferencesProvider.MILES:
				d = "m";
				break;
			case PreferencesProvider.KILOMETERS:
				d = "km";
				break;
			default:
				d = "";
		}
		switch (m_outputVolume) {
			case PreferencesProvider.GALLONS:
				v = "g";
				break;
			case PreferencesProvider.LITRES:
				v = "L";
				break;
			case PreferencesProvider.IMP_GALLONS:
				v = "ig";
				break;
			default:
				v = "";
		}
		if (m_inverse) {
			return v + "/" + d;
		}
		return d + "p" + v;
	}

	public double getWorstEconomy() {
		if (m_inverse) {
			return getBestEconomy();
		}
		return 0D;
	}

	public double getBestEconomy() {
		if (m_inverse) {
			return getWorstEconomy();
		}
		return Double.MAX_VALUE;
	}

	public String getVolumeUnits() {
		switch (m_inputVolume) {
			case PreferencesProvider.GALLONS:
				return " Gallons";
			case PreferencesProvider.LITRES:
				return " Litres";
			case PreferencesProvider.IMP_GALLONS:
				return " Imperial Gallons";
		}
		return "?";
	}

	public String getVolumeUnitsAbbr() {
		switch (m_inputVolume) {
			case PreferencesProvider.GALLONS:
				return " Gal";
			case PreferencesProvider.LITRES:
				return " L";
			case PreferencesProvider.IMP_GALLONS:
				return " Imp. Gal";
		}
		return "?";
	}

	public String getDistanceUnits() {
		switch (m_inputDistance) {
			case PreferencesProvider.MILES:
				return " Miles";
			case PreferencesProvider.KILOMETERS:
				return " Kilometers";
		}
		return "?";
	}

	public String getDistanceUnitsAbbr() {
		switch (m_inputDistance) {
			case PreferencesProvider.MILES:
				return " Mi";
			case PreferencesProvider.KILOMETERS:
				return " K";
		}
		return "?";
	}

	/**
	 * See if a is better than b.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public boolean better(double a, double b) {
		if (m_inverse) {
			return b > a;
		}
		return a > b;
	}

	/**
	 * Is a worse than b?
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public boolean worse(double a, double b) {
		if (m_inverse) {
			return b < a;
		}
		return a < b;
	}
}
