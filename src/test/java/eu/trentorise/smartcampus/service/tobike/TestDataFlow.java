package eu.trentorise.smartcampus.service.tobike;

import it.sayservice.platform.servicebus.test.DataFlowTestHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.google.protobuf.Message;

import eu.trentorise.smartcampus.service.tobike.data.message.Tobike.Stazione;
import eu.trentorise.smartcampus.service.tobike.impl.GetStazioniDataFlow;

public class TestDataFlow extends TestCase {
	
	public void testRun() throws Exception {
		DataFlowTestHelper helper = new DataFlowTestHelper();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("user", "");
		parameters.put("password", "");
		parameters.put("code", "");
		
		Map<String, Object> out1 = helper.executeDataFlow("smartcampus.service.tobike", "GetStazioni", new GetStazioniDataFlow(), parameters);
		List<Message> data1 = (List<Message>)out1.get("data");
		for (Message msg: data1) {
			System.err.println(((Stazione)msg));
			System.err.println("-----------");
		}
		
	}
}
