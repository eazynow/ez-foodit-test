package com.foodit.test.controller;

import com.foodit.test.service.ReportingService;
import com.threewks.thundr.view.json.JsonView;

public class SummaryController {
	public JsonView getTotalsForSummary(Integer items){
		
		return new JsonView(ReportingService.getTotalsForSummary(items));
	}
}
