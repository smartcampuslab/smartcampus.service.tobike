/*******************************************************************************
 * Copyright 2012-2014 Trento RISE
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 ******************************************************************************/
package eu.trentorise.smartcampus.service.tobike.scripts;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import com.google.protobuf.Message;

import eu.trentorise.smartcampus.service.tobike.data.message.Tobike.Stazione;
import eu.trentorise.smartcampus.service.tobike.ws.TOBikeUtente;

public class TobikeScript {

	private static ObjectMapper mapper = new ObjectMapper();
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Message> getStazioni(String user, String password, String code) throws Exception {
		List<Message> result = new ArrayList<Message>();
		
		TOBikeUtente wsc = new TOBikeUtente();
		String s =  wsc.getTOBikeUtenteSoap().elencoStazioniPerComuneJSON(user, password, code);
		s = s.substring(1, s.length() - 1);
		
		String stazioni[] = s.split("\\|");
		
		for (String st: stazioni) {
			String data[] = st.split(";");
			
			Stazione.Builder builder = Stazione.newBuilder();
			builder.setCodice(data[0]);
			builder.setNome(data[1]);
			builder.setIndirizzo(data[2]);
			// ???
			builder.setLatitude(Double.parseDouble(data[4]));
			builder.setLongitude(Double.parseDouble(data[5]));
			
			String stato = data[6];
			
			builder.setStato(stato);
			
			int posti = stato.replaceAll("[[^0]]", "").length();
			int biciclette =stato.replaceAll("[[^4]]", "").length();
			
			builder.setPosti(posti);
			builder.setBiciclette(biciclette);
			
			Stazione stazione = builder.build();
			
			result.add(stazione);
		}
		
		return result;
	}
	
}
