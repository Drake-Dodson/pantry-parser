package com.example.pantryparserbackend.Utils;

import com.example.pantryparserbackend.Requests.RecipeIngredientRequest;

import java.util.HashMap;

public class ImportUtil {
	public static HashMap<String, Double> fractions = new HashMap<String, Double>() {{
		put("½", 0.5);
		put("¼", 0.25);
		//put("", 0.333);
		put("¾", 0.75);
		//put("", 0.666);
	}};

	public static int safeParseInt(String s) {
		try {
			return Integer.parseInt(s);
		} catch(NumberFormatException ex) {
			return 0;
		}
	}

	public static double safeParseDouble(String s) {
		try {
			return Double.parseDouble(s);
		} catch(NumberFormatException ex) {
			return 0;
		}
	}

	public static int safeTimeParse(String s) {
		try {
			String[] c = s.split(" ");
			int time = 0;
			time += c[1].equals("hr") ? ImportUtil.safeParseInt(c[0]) * 60 : ImportUtil.safeParseInt(c[0]);
			time += c.length > 2 ? ImportUtil.safeParseInt(c[2]) : 0;

			return time;
		} catch (Exception ex) {
			return 0;
		}
	}

	public static RecipeIngredientRequest safeIngredientParse(String s) {
		s = s.replaceAll("[â€‰Â…›]", "");
		String[] chunks = s.trim().split(" ", 3);
		try {
			double quantity = 0;
			String units = "";
			if (fractions.get(chunks[0].trim()) != null) {
				quantity += fractions.get(chunks[0].trim());
			} else {
				quantity += Integer.parseInt(chunks[0].trim());
			}
			if(fractions.get(chunks[1].trim()) != null){
				quantity += fractions.get(chunks[1].trim());
				String[] newChunks = chunks[2].trim().split(" ", 2);
				chunks[1] = newChunks[0].trim();
				chunks[2] = newChunks[1].trim();
			}

			if(chunks[1].contains("(")) {
				units = chunks[1].trim() + " ";
				String[] newChunks = chunks[2].split(" ", 3);
				units += newChunks[0].trim() + " ";
				chunks[1] = newChunks[1].trim();
				chunks[2] = newChunks[2].trim();
			}
			units += chunks[1].trim();

			String name = chunks[2].split(",")[0].trim();
			return new RecipeIngredientRequest(name, quantity, units);
		} catch (ArrayIndexOutOfBoundsException | NumberFormatException ex) {
			return new RecipeIngredientRequest(s.split(",")[0].trim(), 0, "");
		}
	}
}
