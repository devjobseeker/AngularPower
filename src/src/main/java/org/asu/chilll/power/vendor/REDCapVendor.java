package org.asu.chilll.power.vendor;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.asu.chilll.power.dataview.ResponseDataView;
import org.asu.chilll.power.dataview.redcap.APIConfig;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class REDCapVendor {
	private HttpPost post;
	private HttpResponse resp;
	private HttpClient client;
	private int respCode;
	private BufferedReader reader;
	private StringBuffer result;
	private String line;
	
	public ResponseDataView ping(APIConfig config, JSONArray data) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("token", config.getToken()));
		params.add(new BasicNameValuePair("content", "record"));
		params.add(new BasicNameValuePair("format", "json"));
		params.add(new BasicNameValuePair("type", "eav"));
		//params.add(new BasicNameValuePair("overwriteBehavior", "overwrite"));
		params.add(new BasicNameValuePair("data", data.toJSONString()));
		
		post = new HttpPost(config.getUrl());
		post.setHeader("Content-Type", "application/x-www-form-urlencoded");
		
		try{
			post.setEntity(new UrlEncodedFormEntity(params));
		}catch (final Exception e){
			e.printStackTrace();
		}

		result = new StringBuffer();
		client = HttpClientBuilder.create().build();
		respCode = -1;
		reader = null;
		line = null;
		
		return doPost();
	}
	
	public ResponseDataView importLongitudinalRecords(APIConfig config, JSONArray data) {
		//[{"record": "2", "redcap_event_name":"enrollment_arm_1", "field_name":"first_name","value": "222"}]
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("token", config.getToken()));
		params.add(new BasicNameValuePair("content", "record"));
		params.add(new BasicNameValuePair("format", "json"));
		params.add(new BasicNameValuePair("type", "eav"));
		//params.add(new BasicNameValuePair("overwriteBehavior", "overwrite"));
		params.add(new BasicNameValuePair("data", data.toJSONString()));
		System.out.println("====================================================");
		System.out.println(data.toJSONString());
		System.out.println("====================================================");
		
		post = new HttpPost(config.getUrl());
		post.setHeader("Content-Type", "application/x-www-form-urlencoded");
		
		try{
			post.setEntity(new UrlEncodedFormEntity(params));
		}catch (final Exception e){
			e.printStackTrace();
		}

		result = new StringBuffer();
		client = HttpClientBuilder.create().build();
		respCode = -1;
		reader = null;
		line = null;
		
		return doPost();
	}
	
//	public ResponseDataView importFile(APIConfig config, File file, String recordId, String eventId, String fieldId) {
//		MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
//		multipartEntityBuilder.addBinaryBody("file", file, ContentType.create("application/octet-stream"), file.getName());
//		multipartEntityBuilder.addTextBody("token", config.getToken());
//		multipartEntityBuilder.addTextBody("content", "file");
//		multipartEntityBuilder.addTextBody("action", "import");
//		multipartEntityBuilder.addTextBody("record", "a0b98024e3bdb62d");	//record
//		multipartEntityBuilder.addTextBody("field", "audio_file");	//fieldId
//		multipartEntityBuilder.addTextBody("event", "event_1_arm_1");	//eventId
//		
//		HttpEntity httpEntity = multipartEntityBuilder.build();
//		
//		post = new HttpPost(config.getUrl());
//
//		try{
//			post.setEntity(httpEntity);
//		}catch (final Exception e){
//			e.printStackTrace();
//		}
//
//		result = new StringBuffer();
//		client = HttpClientBuilder.create().build();
//		respCode = -1;
//		reader = null;
//		line = null;
//		
//		return doPost();
//	}
	
	private ResponseDataView doPost(){
		resp = null;

		try{
			System.out.println("=============================DO POST===========================");
			resp = client.execute(post);
			respCode = resp.getStatusLine().getStatusCode();
		}catch (Exception e){
			e.printStackTrace();
			ResponseDataView response = new ResponseDataView(resp.getStatusLine().getStatusCode());
			response.setResponseMsg(e.getMessage());
			return response;
		}

		if(resp != null){
			
			System.out.println("========================= Response CODE: " + respCode + "======================================");
			try{
				reader = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
			}catch (Exception e){
				System.out.println("=======================ERROR============================");
				e.printStackTrace();
				ResponseDataView response = new ResponseDataView(respCode);
				response.setResponseMsg(e.getMessage());
				return response;
			}
		}

		if(reader != null){
			try{
				while((line = reader.readLine()) != null){
					System.out.println(line);
					result.append(line);
				}
			}catch (final Exception e){
				e.printStackTrace();
				ResponseDataView response = new ResponseDataView(respCode);
				response.setResponseMsg(result.toString());
				return response;
			}
		}
		
		System.out.println("respCode: " + respCode);
		System.out.println("result: " + result.toString());
		
		if(respCode != 200) {
			ResponseDataView response = new ResponseDataView(respCode);
			response.setResponseMsg("REDCap API Response: " + result.toString() + " Response Code: " + respCode);
			response.setHasError(true);
			return response;
		}else {
			try {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(result.toString());
				long count = (long)json.get("count");
				ResponseDataView response = new ResponseDataView(respCode);
				response.setResponseMsg(result.toString());
				response.setCount(count);
				return response;
			}catch(Exception e) {
				ResponseDataView response = new ResponseDataView(respCode);
				response.setHasError(true);
				response.setResponseMsg("Please check your URL and API token.");
				return response;
			}
		}
	}
}