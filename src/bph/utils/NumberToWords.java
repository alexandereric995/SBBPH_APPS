package bph.utils;

import java.text.DecimalFormat;

public class NumberToWords {

	private static final String[] tensNames = { "", " Sepuluh", " Dua Puluh",
			" Tiga Puluh", " Empat Puluh", " Lima Puluh", " Enam Puluh",
			" Tujuh Puluh", " Lapan Puluh", " Sembilan Puluh" };

	private static final String[] numNames = { "", " Satu", " Dua", " Tiga",
			" Empat", " Lima", " Enam", " Tujuh", " Lapan", " Sembilan",
			" Sepuluh", " Sebelas", " Dua Belas", " Tiga Belas",
			" Empat Belas", " Lima Belas", " Enam Belas", " Tujuh Belas",
			" Lapan Belas", " Sembilan Belas" };

	private static String convertLessThanOneThousand(int number) {
		String soFar;

		if (number % 100 < 20) {
			soFar = numNames[number % 100];
			number /= 100;
		} else {
			soFar = numNames[number % 10];
			number /= 10;

			soFar = tensNames[number % 10] + soFar;
			number /= 10;
		}
		if (number == 0)
			return soFar;
		return numNames[number] + " Ratus " + soFar;
	}

	public static String convert(Long number) {
		// 0 to 999 999 999 999
		if (number == 0) {
			return "kosong";
		}

		String snumber = Long.toString(number);
		// pad with "0"
		String mask = "000000000000";
		DecimalFormat df = new DecimalFormat(mask);
		snumber = df.format(number);

		// XXXnnnnnnnnn
		int billions = Integer.parseInt(snumber.substring(0, 3));
		// nnnXXXnnnnnn
		int millions = Integer.parseInt(snumber.substring(3, 6));
		// nnnnnnXXXnnn
		int hundredThousands = Integer.parseInt(snumber.substring(6, 9));
		// nnnnnnnnnXXX
		int thousands = Integer.parseInt(snumber.substring(9, 12));

		String tradBillions;
		switch (billions) {
		case 0:
			tradBillions = "";
			break;
		case 1:
			tradBillions = convertLessThanOneThousand(billions) + " billion ";
			break;
		default:
			tradBillions = convertLessThanOneThousand(billions) + " billion ";
		}
		String result = tradBillions;

		String tradMillions;
		switch (millions) {
		case 0:
			tradMillions = "";
			break;
		case 1:
			tradMillions = convertLessThanOneThousand(millions) + " million ";
			break;
		default:
			tradMillions = convertLessThanOneThousand(millions) + " million ";
		}
		result = result + tradMillions;

		String tradHundredThousands;
		switch (hundredThousands) {
		case 0:
			tradHundredThousands = "";
			break;
		case 1:
			tradHundredThousands = "Satu Ribu ";
			break;
		default:
			tradHundredThousands = convertLessThanOneThousand(hundredThousands)
					+ " Ribu ";
		}
		result = result + tradHundredThousands;

		String tradThousand;
		tradThousand = convertLessThanOneThousand(thousands);
		result = result + tradThousand;

		// remove extra spaces!
		return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
	}

	/**
	 * testing
	 * 
	 * @param args
	 */
	// added by hilda
	public static String convertNumberToWords(String input) {

		String duit = "";
		String rightHandSide = "";
		String lefHandSide = "";
		int lengthOfInput = input.length();
		int index = input.indexOf('.');

		if (index > -1) {
			rightHandSide = input.substring(index + 1, lengthOfInput);

			lefHandSide = input.substring(0, index);
			if (lefHandSide.length() == 0) {
				lefHandSide = lefHandSide + "0";
			}
			if (rightHandSide.length() == 1) {
				rightHandSide = rightHandSide + "0";
			}
			if (rightHandSide.equals("00")) {
				duit = NumberToWords.convert(Long.parseLong(lefHandSide));
			} else {
				if (lefHandSide.equals("0")) {
					duit = NumberToWords.convert(Long.parseLong(rightHandSide))
							+ " Sen";
				} else {
					duit = NumberToWords.convert(Long.parseLong(lefHandSide))
							+ " dan "
							+ NumberToWords.convert(Long
									.parseLong(rightHandSide)) + " Sen";
				}
			}
		} else {
			duit = NumberToWords.convert(Long.parseLong(input));
		}

		return duit;
	}

	public static void main(String[] args) {

		String number = "11388.66";
		System.out.println(number + " : " + convertNumberToWords(number));
		
	}
}
