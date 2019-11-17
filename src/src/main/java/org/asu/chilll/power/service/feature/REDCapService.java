package org.asu.chilll.power.service.feature;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
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
import org.asu.chilll.power.dataview.redcap.APIConfig;
import org.asu.chilll.power.dataview.redcap.records.GameProgressRecord;
import org.asu.chilll.power.enums.GameStatusType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class REDCapService {
	private List<NameValuePair> params;
	private HttpPost post;
	private HttpResponse resp;
	private HttpClient client;
	private int respCode;
	private BufferedReader reader;
	private StringBuffer result;
	private String line;
	private JSONObject record;
	private JSONArray data;
	private String recordID;
	private SecureRandom random;
	
	private final static String dateFormat = "yyyy-MM-dd HH:mm:ss";
	
	@SuppressWarnings("unchecked")
	public void importRecords(APIConfig config) {
		GameProgressRecord dv = new GameProgressRecord();
		dv.setRecord_id(recordID);
		dv.setUid(UUID.randomUUID().toString());
		dv.setChildId("aaa");
		dv.setGameId("DS");
		dv.setGrade("K");
		dv.setCoins(30);
		dv.setCreateDate(new Date());
		dv.setCreateTime(new Date().getTime());
		dv.setLastUpdateDate(new Date());
		dv.setLastUpdateTime(new Date().getTime());
		dv.setGameStatus(GameStatusType.Complete.toString());
		
		DateFormat dateF = new SimpleDateFormat(dateFormat);
		
		random = new SecureRandom();
		recordID = DigestUtils.sha1Hex(new BigInteger(16, random).toString(16)).substring(0, 16);
		record = new JSONObject();
		//record.put("record", dv.getUid());
		record.put("record_id", UUID.randomUUID().toString());
		//record.put("record_id", dv.getUid());
		record.put("uid", dv.getUid());
		record.put("child_id", dv.getChildId());
		record.put("game_id", dv.getGameId());
		record.put("grade", dv.getGrade());
		record.put("coins", 40);
		record.put("game_status", dv.getGameStatus());
		record.put("create_date", dateF.format(dv.getCreateDate()));
		record.put("create_time", dv.getCreateTime());
		record.put("last_update_date", dateF.format(dv.getLastUpdateDate()));
		record.put("last_update_time", dv.getLastUpdateTime());
		
		data = new JSONArray();
		data.add(record);
		System.out.println("==========================================================");
		System.out.println(record);
		System.out.println("==========================================================");
		
		params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("token", config.getToken()));
		params.add(new BasicNameValuePair("content", "record"));
		params.add(new BasicNameValuePair("format", "json"));
		params.add(new BasicNameValuePair("type", "flat"));
		//params.add(new BasicNameValuePair("overwriteBehavior", "overwrite"));
		params.add(new BasicNameValuePair("data", data.toJSONString()));
		
		post = new HttpPost(config.getUrl());
		post.setHeader("Content-Type", "application/x-www-form-urlencoded");
		
		try
		{
			post.setEntity(new UrlEncodedFormEntity(params));
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}

		result = new StringBuffer();
		client = HttpClientBuilder.create().build();
		respCode = -1;
		reader = null;
		line = null;
		
		doPost();
	}
	
	@SuppressWarnings("unchecked")
	public void importLongitudinalRecords(APIConfig config) {
		random = new SecureRandom();
		recordID = "1415d506-6476-4616-9cac-f9f8392a3599";
		record = new JSONObject();
		
		record.put("record", recordID);
		record.put("redcap_event_name", "grade_k_arm_1");
		record.put("field_name", "ds_subject_id");
		record.put("value", "bbb");
		
		data = new JSONArray();
		data.add(record);
		
		JSONObject record1 = new JSONObject();
		record1.put("record", recordID);
		record1.put("redcap_event_name", "grade_k_arm_1");
		record1.put("field_name", "ds_grade");
		record1.put("value", "bbb");
		data.add(record1);
		
		DateFormat dateF = new SimpleDateFormat(dateFormat);
		JSONObject record2 = new JSONObject();
		record2.put("record", recordID);
		record2.put("redcap_event_name", "grade_k_arm_1");
		record2.put("field_name", "ds_create_date");
		record2.put("value", dateF.format(new Date()));
		data.add(record2);
		
		JSONObject record3 = new JSONObject();
		record3.put("record", recordID);
		record3.put("redcap_event_name", "grade_k_arm_1");
		record3.put("field_name", "ds_create_time");
		record3.put("value", "bbb");
		data.add(record3);
		//[{"record": "2", "redcap_event_name":"enrollment_arm_1", "field_name":"first_name","value": "222"}]
		System.out.println("==========================================================");
		System.out.println(record);
		System.out.println("==========================================================");
		
		params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("token", config.getToken()));
		params.add(new BasicNameValuePair("content", "record"));
		params.add(new BasicNameValuePair("format", "json"));
		params.add(new BasicNameValuePair("type", "eav"));
		//params.add(new BasicNameValuePair("overwriteBehavior", "overwrite"));
		params.add(new BasicNameValuePair("data", data.toJSONString()));
		
		post = new HttpPost(config.getUrl());
		post.setHeader("Content-Type", "application/x-www-form-urlencoded");
		
		try
		{
			post.setEntity(new UrlEncodedFormEntity(params));
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}

		result = new StringBuffer();
		client = HttpClientBuilder.create().build();
		respCode = -1;
		reader = null;
		line = null;
		
		doPost();
	}
	
	public void importFile(APIConfig config) {
		File file = new File("C:/Users/hjpei/Desktop/Power2.0/DataRepository/AudioRecordingFiles/DigitSpan/2018_9_19/aaa_20180919172642_1537403202070_DS_ListLength1_TrialIndex1_Practice.wav");

		MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
		multipartEntityBuilder.addBinaryBody("file", file, ContentType.create("application/octet-stream"), file.getName());
		multipartEntityBuilder.addTextBody("token", config.getToken());
		multipartEntityBuilder.addTextBody("content", "file");
		multipartEntityBuilder.addTextBody("action", "import");
		multipartEntityBuilder.addTextBody("record", "a0b98024e3bdb62d");
		multipartEntityBuilder.addTextBody("field", "audio_file");
		//multipartEntityBuilder.addTextBody("event", "event_1_arm_1");
		
		HttpEntity httpEntity = multipartEntityBuilder.build();
		
		post = new HttpPost(config.getUrl());

		try
		{
			post.setEntity(httpEntity);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}

		result = new StringBuffer();
		client = HttpClientBuilder.create().build();
		respCode = -1;
		reader = null;
		line = null;
		
		doPost();
	}
	
	public void doPost(){
		resp = null;

		try
		{
			System.out.println("=============================DO POST===========================");
			resp = client.execute(post);
			respCode = resp.getStatusLine().getStatusCode();
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}

//		if(resp != null)
//		{
//			respCode = resp.getStatusLine().getStatusCode();
//			System.out.println("=========================Response CODE: " + respCode + "======================================");
//			try
//			{
//				reader = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
//			}
//			catch (final Exception e)
//			{
//				System.out.println("=======================ERROR============================");
//				e.printStackTrace();
//			}
//		}

//		if(reader != null)
//		{
//			try
//			{
//				while((line = reader.readLine()) != null)
//				{
//					result.append(line);
//				}
//			}
//			catch (final Exception e)
//			{
//				e.printStackTrace();
//			}
//		}

		System.out.println("respCode: " + respCode);
		//System.out.println("result: " + result.toString());
	}
}
