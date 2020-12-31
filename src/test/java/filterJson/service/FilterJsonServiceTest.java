package filterJson.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.matchers.JUnitMatchers.containsString;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = FilterJsonService.class)
@SpringBootTest
public class FilterJsonServiceTest {

	@Autowired
	private FilterJsonService filterJsonService;

	private JSONArray dataArray;

	private JSONObject dataObj;

	@BeforeEach
	public void init() throws IOException, ParseException, JSONException {
		JSONParser parser = new JSONParser();
		FileReader reader = null;
		
		try {
			reader = new FileReader(
					Thread.currentThread().getContextClassLoader().getResource("dataArray.json").getPath());
			String dataArrayString =  parser.parse(reader).toString();
			dataArray = new JSONArray(dataArrayString);
			
			reader = new FileReader(
					Thread.currentThread().getContextClassLoader().getResource("dataObject.json").getPath());
			String dataObjString =  parser.parse(reader).toString();
			dataObj = new JSONObject(dataObjString);
			
			
		} finally {
			if(reader != null)
				safeClose(reader);
		}
	}
	
	@Test
	public void whenIsJsonThenProjectionIt() throws JSONException {
		String fields = "data.storage,data.bus.cid";
		
		Object result = filterJsonService.projection(dataObj.toString(), fields);
		assertThat(result.toString(), containsString("storage"));
		assertThat(result.toString(), containsString("cid"));

	}

	@Test
	public void whenNestedFieldDoesNotExistFOrJsonThenEmptyJson() throws JSONException {
		String fields = "data.storageThatNotExits,data.busThatNotExists";

		Object result = filterJsonService.projection(dataObj.toString(), fields);
		JSONObject resulObj = new JSONObject(result.toString());
		assertThat(result.toString(), containsString("data"));
		JSONObject dataObj = (JSONObject) resulObj.get("data");
		assertTrue(dataObj.isEmpty());
	}
	
	@Test
	public void whenFieldContainsJsonArrayThenProjectionIt() throws JSONException {
		String fields = "data.fifty,data.bus.lilith.warz,data.total";
		
		Object result = filterJsonService.projection(dataObj.toString(), fields);
		assertThat(result.toString(), containsString("bus"));
		assertThat(result.toString(), containsString("lilith"));
		assertThat(result.toString(), containsString("warz"));
		assertThat(result.toString(), containsString("fifty"));
		assertThat(result.toString(), containsString("total"));

	}
	
	@Test
	public void whenJsonInputIsArrayThenProjectionIt() throws JSONException {
		String fields = "data.bus.cloud";

		
		Object result = filterJsonService.projection(dataArray.toString(), fields);
		assertThat(result.toString(), containsString("bus"));
		assertThat(result.toString(), containsString("cloud"));

	}
	
	@Test
	public void whenJsonInputIsArrayAndfieldsContainsObjectThenProjectionIt() throws JSONException {
		String fields = "data.bus.lilith,data.fifty";
		
		Object result = filterJsonService.projection(dataArray.toString(), fields);
		assertThat(result.toString(), containsString("lilith"));
		assertThat(result.toString(), containsString("fifty"));

	}
	
	@Test
	public void whenNestedFieldDoesNotExistThenEmptyJson() throws JSONException {
		String fields = "data.bus.cloud";

		Object result = filterJsonService.projection(dataArray.toString(), fields);
		assertThat(result.toString(), containsString("cloud"));

	}

	@Test
	public void whenOneNestedFieldsDoesNotExistAndAnotherYesThenOnlyOneFieldIsProjected() throws JSONException {
		String fields = "data.bus.cloud,data.bus.cloudThatNotExists";

		Object result = filterJsonService.projection(dataArray.toString(), fields);
		assertThat(result.toString(), containsString("bus"));

	}

	@Test
	public void whenNestedFieldsDoesNotExistThenEmptyJson() throws JSONException {
		String fields = "data.bus.NotExists,data.bus.cid";

		Object result = filterJsonService.projection(dataArray.toString(), fields);
		assertThat(result.toString(), containsString("cid"));

	}

	@Test
	public void whenSingleFieldDoesNotExistThenEmptyJson() throws JSONException {
		String fields = "header";

		Object result = filterJsonService.projection(dataArray.toString(), fields);
		JSONArray resultArray = new JSONArray(result.toString());
		resultArray.forEach(item -> {
			JSONObject obj = (JSONObject) item;
			assertTrue(obj.isEmpty());
		});
	}

	private static void safeClose(FileReader fis) {
		if (fis != null) {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}