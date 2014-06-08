package com.foodit.test.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import com.google.common.io.Resources;
import com.threewks.thundr.logger.Logger;

public class FileHelper {

	public static String readFile(String resourceName) {
		URL url = Resources.getResource(resourceName);
		try {
			return IOUtils.toString(new InputStreamReader(url.openStream()));
		} catch (IOException e) {
			Logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
