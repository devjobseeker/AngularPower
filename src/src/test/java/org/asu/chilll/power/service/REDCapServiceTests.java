package org.asu.chilll.power.service;

import org.asu.chilll.power.dataview.redcap.APIConfig;
import org.asu.chilll.power.dataview.redcap.SyncRecordDataView;
import org.asu.chilll.power.enums.GameIdType;
import org.asu.chilll.power.enums.SyncDataRecordType;
import org.asu.chilll.power.service.feature.REDCapService;
import org.asu.chilll.power.service.feature.REDCapSyncDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class REDCapServiceTests {
	
//	@Autowired
//	private REDCapService redcapService;
//	
//	@Autowired
//	private REDCapSyncDataService service;
	
//	@Test
//	public void importRecord() {
//		String token1 = "62512698E1121E280309CBA55BE24194";	//longitudinal
//		String token2 = "BB342162E5D5D6D2C7B3C4A1D78A9B46";
//		APIConfig config = new APIConfig(token1, "https://redcap.chs.asu.edu/api/");
//		
////		redcapService.importLongitudinalRecords(config);
//		SyncRecordDataView dv = new SyncRecordDataView();
//		dv.setChildId("aaa");
//		dv.setGrade("K");
//		dv.setCategory(SyncDataRecordType.Detail.toString());
//		dv.setGameId(GameIdType.Digit_Span.toString());
//		dv.setApiConfig(config);
//		service.processData(dv);
//	}
}
