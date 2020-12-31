package filterJson.service;

import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import org.springframework.stereotype.Service;


@Service
public class FilterJsonService {
    public Object projection(Object jsonString, String path) throws JSONException {
        Object jsonObj = new JSONTokener(jsonString.toString()).nextValue();
        String[] pathList = path.split(",");
        if(jsonObj instanceof JSONObject) {
            return handleJsonObject(jsonObj.toString(), pathList);
        } else if (jsonObj instanceof JSONArray) {
            return handleJsonArray(jsonObj.toString(), pathList);
        } else throw new InternalError("Error to parse JSON");
    }

    private Object handleJsonArray(String jsonString, String[] pathList) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonString);
        JSONArray outputArray = new JSONArray();
        for(int i = 0; i< jsonArray.length(); i++) {
            JSONObject singleObj = jsonArray.getJSONObject(i);

            outputArray.put(handleJsonObject(singleObj.toString(), pathList));

        }
        return outputArray;
    }

    private Object handleJsonObject(String jsonString, String[] pathList) throws JSONException {
        JSONObject json = new JSONObject(jsonString);

        JSONObject outputJson = new JSONObject();
        String first = "";
        Object value;
        try {
            for (String path : pathList) {

                if(path.contains(".")) {
                    first = path.split("\\.")[0];
                    value = json.get(first);
                } else {
                    outputJson.put(path, json.get(path));
                    value = null;
                }

                if (value instanceof JSONObject) {
                    outputJson = setJsonObjectValue(json, outputJson, first, path);
                } else if (value instanceof JSONArray) {
                    setJsonArrayValue(outputJson, first, value, path);
                }
            }
            return outputJson;
        } catch(JSONException jEx) {
            return outputJson;
        }
    }

    private void setJsonArrayValue(JSONObject outputJson, String first, Object value, String path) throws JSONException {
        JSONArray valueArray = new JSONArray(value.toString());
        JSONArray outputProjection = new JSONArray();
        String newPath = path.substring(path.indexOf('.')+1);
        for(int i=0; i < valueArray.length(); i++) {
            JSONObject singleElem = valueArray.getJSONObject(i);
            outputProjection.put(projection(singleElem.toString(), newPath));
        }
        outputJson.put(first, outputProjection);
    }

    private JSONObject setJsonObjectValue(JSONObject json, JSONObject outputJson, String first, String path)
            throws JSONException {

        String newPath = path.substring(path.indexOf('.')+1);
        JSONObject firstObj = new JSONObject();
        JSONObject secondObj = new JSONObject();

        if(outputJson.has(first)) {
            firstObj.put(first, outputJson.get(first));
        }

        secondObj.put(first, projection(json.get(first).toString(), newPath));
        return mergeJson(first, firstObj, secondObj);
    }

    private JSONObject mergeJson(String rootKey, JSONObject firstObj, JSONObject secondObj) {
        JSONObject mergedJson = new JSONObject();
        if (firstObj.length() <= 0) {
            return secondObj;
        }

        JSONParser p = new JSONParser(JSONParser.MODE_JSON_SIMPLE);

        try {
            net.minidev.json.JSONObject o1 = (net.minidev.json.JSONObject) p
                    .parse(firstObj.toString());
            net.minidev.json.JSONObject o2 = (net.minidev.json.JSONObject) p
                    .parse(secondObj.toString());
            o1.merge(o2);

            if(o1.containsKey(rootKey)) {
                return new JSONObject(o1);
            }

            mergedJson.put(rootKey, o1);
            return mergedJson;

        } catch (ParseException | JSONException e) {
            throw new InternalError(e.getMessage());
        }
    }
}