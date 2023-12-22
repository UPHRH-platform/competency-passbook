package com.sphere.compentencytool.kafka.consumer.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sphere.compentencytool.common.utils.propertiesCache;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


public class Api_services {
    RestTemplate restTemplate = new RestTemplate();

//    ResourceBundle props = ResourceBundle.getBundle("application");
    propertiesCache env=new propertiesCache();
    public Object get_hierarchy(String courseId, JSONArray UserId) {
//      String url = "https://sphere.aastrika.org/api/private/content/v3/hierarchy/"+courseId+"?hierarchyType=detail";
        String url = env.getProperty("get.hierarchy")+ courseId + "?hierarchyType=detail";
        System.out.println(url);
        String Api_key = env.getProperty("Api.key");
        System.out.println(Api_key);
        HttpHeaders header = new HttpHeaders();
        header.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        header.add("Authorization", Api_key);
        ResponseEntity<String> response = null;
        try {
            HttpEntity<String> entity = new HttpEntity<String>("parameters", header);
            response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            String responseStr = response.getBody();
            int begin = responseStr.indexOf("{");
            int end = responseStr.lastIndexOf("}") + 1;
            responseStr = responseStr.substring(begin, end);

            System.out.println("=========******** hierachy API Called return response ************=============");
            System.out.println(responseStr);
            Object course_data = getCourseInfo_parse(responseStr, UserId);
        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
        }

        return response;

    }

    private Object getCourseInfo_parse(String response, JSONArray userId) throws JsonProcessingException {

        JSONObject Competency_object = new JSONObject(response);
        JSONObject result_object = Competency_object.getJSONObject("result");
        JSONObject content_data = (JSONObject) result_object.get("content");
        // Course info
        String Course_name = (String) content_data.get("name");
        String course_Id = (String) content_data.get("identifier");
        // parse competencies_v1 string json
        System.out.println(content_data.get("competencies_v1"));
        String competencyData = (String) content_data.get("competencies_v1");
        JSONArray competencyData1 = new JSONArray(competencyData);

        for (int id = 0; id < userId.length(); id++) {
            System.out.println(userId.get(id));
            String Userid = (String) userId.get(id);

            for (int i = 0; i < competencyData1.length(); i++) {
                System.out.println("competency loop inside ");
                JSONObject compentency_parse = (JSONObject) competencyData1.get(i);

                // competencies_v1 Info
                String cmp_id = compentency_parse.get("competencyId").toString();
                String competencyName = (String) compentency_parse.get("competencyName");
                String competency_level = String.valueOf(compentency_parse.get("level")) ;
                System.out.println("cmp_id = " + cmp_id + "competencyName = " + competencyName + "competency_level =" + competency_level);

                // Call Get_entityById (CompetenceId) Api Service
                JSONObject get_entity_response = get_entityById(cmp_id);
                System.out.println("get_entity_response :" + get_entity_response);


                System.out.println(get_entity_response.get("name"));
                String entity_name = (String) get_entity_response.get("name");
                Integer levelId = (Integer) get_entity_response.get("levelId");

//     Prepare passbook data

                JSONObject mainobj = new JSONObject();
                JSONObject main1 = new JSONObject();
                main1.put("userId", Userid);
                main1.put("typeName", "competency");

                JSONArray competencyDetails = new JSONArray();
                JSONObject competencyDetails_main_obj = new JSONObject();
                competencyDetails_main_obj.put("competencyId", cmp_id);

                JSONObject additionalParams = new JSONObject();
                additionalParams.put("competencyName", competencyName);
//                additionalParams.put("competencyType", (Collection<?>) null);
//                additionalParams.put("competencyArea", (Collection<?>) null);
                competencyDetails_main_obj.put("additionalParams", additionalParams);

                JSONObject acquiredDetails = new JSONObject();

                acquiredDetails.put("acquiredChannel", "course");
                acquiredDetails.put("competencyLevelId", competency_level);
//                acquiredDetails.put("secondaryPassbookId", (Collection<?>) null);
                JSONObject acquiredDetails_additionalParams = new JSONObject();
                acquiredDetails_additionalParams.put("courseId", course_Id);
                acquiredDetails_additionalParams.put("courseName", Course_name);

                acquiredDetails.put("additionalParams", acquiredDetails_additionalParams);
                competencyDetails_main_obj.put("acquiredDetails", acquiredDetails);
                competencyDetails.put(competencyDetails_main_obj);
                main1.put("competencyDetails", competencyDetails);

                mainobj.put("request", main1);
                System.out.println(mainobj.toString(4));
                Passbook_update(Userid, mainobj);
            }
        }
        return null;
    }

    private void Passbook_update(String userId, JSONObject request) throws JsonProcessingException {

        RestTemplate restTemplate = new RestTemplate();
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        restTemplate.setRequestFactory(requestFactory);

        String request_body = request.toString();
        String url = env.getProperty("passbook.update.url");
        String Api_key =env.getProperty("Api.key");
        HttpHeaders header = new HttpHeaders();
        header.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        header.add("Authorization", Api_key);
        header.add("x-authenticated-userid", userId);
        Map<String, Object> mapping = new ObjectMapper().readValue(request_body, HashMap.class);

        System.out.println("mapping : " + mapping);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(mapping, header);
        ResponseEntity<String> passbookResponse = restTemplate.exchange(url, HttpMethod.PATCH, entity, String.class);
        System.out.println(passbookResponse);
        String responseStr = passbookResponse.getBody();
        int begin = responseStr.indexOf("{");
        int end = responseStr.lastIndexOf("}") + 1;
        responseStr = responseStr.substring(begin, end);
        System.out.println(responseStr);
        JSONObject passbook_payload = new JSONObject(responseStr);
        System.out.println(passbook_payload.toString(4));

    }

    private JSONObject get_entityById(String competency_id) throws JsonProcessingException {
        String url = env.getProperty("get.entityById") + competency_id;
        System.out.println("get_entityById fun ");
        HttpHeaders header = new HttpHeaders();
        header.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//        header.add("x-authenticated-user-token", token);

        String req = "{\"filter\":{\"isDetail\":true}}";
        Map<String, Object> mapping = new ObjectMapper().readValue(req, HashMap.class);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(mapping, header);
        ResponseEntity<String> entityResponse = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
//        System.out.println(entityResponse);
        String responseStr = entityResponse.getBody();
        int begin = responseStr.indexOf("{");
        int end = responseStr.lastIndexOf("}") + 1;
        responseStr = responseStr.substring(begin, end);
//        System.out.println(responseStr);
        JSONObject passbook_payload = new JSONObject(responseStr);
        JSONObject passbook_result = passbook_payload.getJSONObject("result");
        return (JSONObject) passbook_result.get("response");

    }

//    private void GetPassbook(JSONArray userId) throws JsonProcessingException {
//
//        String url = "https://sphere.aastrika.org/api/user/v1/passbook";
//        String Api_key = "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJTNHNNVFdjZUZqYkxUWGxiczkzUzk4dmFtODBhdkRPUiJ9.nPOCY0-bVX28iNcxxnYbGpihY3ZzfNwx0-SFCnJwjas";
//        for (int i=0; i<userId.length();i++){
//            System.out.println("Passbook Update loop");
//            System.out.println(userId.get(i));
//            HttpHeaders header = new HttpHeaders();
//            header.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//            header.add("Authorization", Api_key);
//            header.add("x-authenticated-userid", (String) userId.get(i));
//            String req = "{\"request\":{\"typeName\":\"competency\"}}";
//            Map<String, Object> mapping = new ObjectMapper().readValue(req, HashMap.class);
//
//            System.out.println(mapping);
//
//            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(mapping, header);
//            ResponseEntity<String> entityResponse = restTemplate.exchange(url , HttpMethod.POST, entity, String.class);
//            System.out.println(entityResponse);
//            String responseStr = entityResponse.getBody();
//            int begin = responseStr.indexOf("{");
//            int end = responseStr.lastIndexOf("}") + 1;
//            responseStr = responseStr.substring(begin, end);
//            System.out.println(responseStr);
//            JSONObject passbook_payload = new JSONObject(responseStr);
//            JSONObject passbook_result=passbook_payload.getJSONObject("result");
//            System.out.println(passbook_result);
//
//        }


}
