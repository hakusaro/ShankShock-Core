package com.shankshock.nicatronTg.Registration.Engines;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.shankshock.nicatronTg.Registration.Registration;

public class BlacklistEngine {
	private static final String FILENAME = "./shankshock/command-blacklist.txt";
	@SuppressWarnings("unused")
	private final Registration plugin;
	public ArrayList<String> commandBlacklist = new ArrayList<String>();

	public BlacklistEngine(Registration instance) {
		plugin = instance;
		readBlacklist();
		writeBlacklist();
		System.out.println("BlacklistEngine: Loaded " + commandBlacklist.size()
				+ " commands.");
	}

	public void readBlacklist() {
		File f = new File(FILENAME);

		if (f.exists()) {
			try {
				Scanner sc = new Scanner(new FileInputStream(FILENAME));
				String temp = "";
				while (sc.hasNextLine()) {
					temp += "," + sc.nextLine();
				}

				String[] temp2 = temp.split(",");

				for (String s : temp2) {
					if (s.trim().length() > 0) {
						commandBlacklist.add(s);
					}
				}

				sc.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void writeBlacklist() {
		String temp = "";
		for (String s : commandBlacklist) {
			if (temp.equals("")) {
				temp = s;
			} else {
				temp += "\n" + s;
			}
		}

		try {
			FileWriter f = new FileWriter(new File(FILENAME));
			f.write(temp);
			f.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isCommandBlacklisted(String s) {
		if (commandBlacklist.contains(s)) {
			return true;
		} else {
			return false;
		}
	}

	public void addCommandToBlacklist(String s) {
		commandBlacklist.add(s);
		writeBlacklist();
	}

	public void removeCommandFromBlacklist(String s) {
		if (commandBlacklist.contains(s)) {
			commandBlacklist.remove(s);
		}
		writeBlacklist();
	}

	public ArrayList<String> getBlacklist() {
		return commandBlacklist;
	}
}
