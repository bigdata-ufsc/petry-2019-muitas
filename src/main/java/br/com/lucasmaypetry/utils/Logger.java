package br.com.lucasmaypetry.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Logger {

	@AllArgsConstructor
	@Getter
	public enum Type {
		INFO("[    INFO    ]"),
		ERROR("[   ERROR    ]"),
		WARNING("[  WARNING   ]"),
		CONFIG("[   CONFIG   ]"),
		RUNNING("[  RUNNING   ]");

		private String prefix;
	}

	public static void log(Type type, String message) {
		System.out.println(getCurrentDate() + " " + type.getPrefix() + " " + message);
	}

	public static void log(Type type, Object... objs) {
		String msg = objs[0].toString();

		for (int i = 1; i < objs.length; i++)
			msg += ", " + objs[i].toString();

		System.out.println(getCurrentDate() + " " + type.getPrefix() + " " + msg);
	}
	
	private static String getCurrentDate() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

}
