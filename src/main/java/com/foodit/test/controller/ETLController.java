package com.foodit.test.controller;

import com.foodit.test.service.ETLService;
import com.threewks.thundr.view.json.JsonView;

public class ETLController {
	
	/**
	 * Runs the ETL process
	 * @return A Json Response on how the ETL went
	 */
	public JsonView performETL() {
		
		return new JsonView(ETLService.performETL());

	}

}
