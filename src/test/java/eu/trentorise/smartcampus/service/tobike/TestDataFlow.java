package eu.trentorise.smartcampus.service.tobike;

import it.sayservice.platform.client.InvocationException;
import it.sayservice.platform.client.ServiceBusClient;
import it.sayservice.platform.client.ServiceBusListener;
import it.sayservice.platform.client.jms.JMSServiceBusClient;
import it.sayservice.platform.core.message.Core.ActionInvokeParameters;
import it.sayservice.platform.servicebus.test.DataFlowTestHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.ConnectionFactory;

import junit.framework.TestCase;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;

import eu.trentorise.smartcampus.service.tobike.data.message.Tobike.Stazione;
import eu.trentorise.smartcampus.service.tobike.impl.GetStazioniDataFlow;

public class TestDataFlow extends TestCase {
	
	public void testRun() throws Exception {
		DataFlowTestHelper helper = new DataFlowTestHelper("test");
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("user", "rovereto");
		parameters.put("password", "");
		parameters.put("code", "");
		
		Map<String, Object> out1 = helper.executeDataFlow("smartcampus.service.tobike", "GetStazioni", new GetStazioniDataFlow(), parameters);
		List<Message> data1 = (List<Message>)out1.get("data");
		for (Message msg: data1) {
			System.err.println(((Stazione)msg));
			System.err.println("-----------");
		}
		
	}
	
	public void testRemote() throws InvocationException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("user", "rovereto");
		parameters.put("password", "");
		parameters.put("code", "");

		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		ServiceBusClient client = new JMSServiceBusClient(connectionFactory);
		client.setClientId("test_subscriber");
		
		client.setListener(new ServiceBusListener() {
			
			@Override
			public void onServiceEvents(String arg0, String arg1, String arg2, List<ByteString> arg3) {
				System.err.println("RECEIVED "+ arg3.size());
			}
		});
		
		client.subscribeService("smartcampus.service.tobike", "GetStazioni", parameters);
		
		ActionInvokeParameters invokeService = client.invokeService("smartcampus.service.tobike", "GetStazioni", parameters);
		System.err.println(invokeService.getDataCount());
	}
}
