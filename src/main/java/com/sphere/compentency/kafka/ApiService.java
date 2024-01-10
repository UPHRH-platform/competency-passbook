package com.sphere.compentency.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sphere.compentency.utils.AppProperties;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class ApiService {

    @Autowired
    private AppProperties props;
    RestTemplate restTemplate = new RestTemplate();

    public void get_hierarchy(String courseId, String userId) throws IOException, InterruptedException {
        String url = props.getGetHierarchyApi() + courseId + "/?mode=edit";
        try {
            // Make the HTTP request using RestTemplate
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            String responseStr = responseEntity.getBody();

            // Ensure the response contains JSON
            if (responseStr != null && responseStr.contains("{")) {
                // Parse the entire response as JSON
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonResponse = objectMapper.readTree(responseStr);

                getCourseInfo_parse(responseStr, userId);
            } else {
                System.out.println("Invalid or empty JSON response");
            }
        } catch (Exception e) {
            System.out.println("Error making HTTP request: " + e.getMessage());
        }
    }
    private void getCourseInfo_parse(String response, String userId) {
        Map<String, Map<String, Object>> competencyMap;

        try {
            JSONObject competencyObject = new JSONObject(response);
            JSONObject resultObject = competencyObject.getJSONObject("result");
            JSONObject contentData = resultObject.getJSONObject("content");

            // Course info
            String courseName = contentData.getString("name");
            String courseId = contentData.getString("identifier");

            JSONArray seSubjectsArray = contentData.getJSONArray("se_subjects");
            String[] competencyNames = new String[seSubjectsArray.length()];
            for (int i = 0; i < seSubjectsArray.length(); i++) {
                competencyNames[i] = seSubjectsArray.getString(i);
            }

            JSONArray seSubjectIdsArray = contentData.getJSONArray("se_subjectIds");
            String[] competencyIds = new String[seSubjectIdsArray.length()];
            for (int i = 0; i < seSubjectIdsArray.length(); i++) {
                competencyIds[i] = seSubjectIdsArray.getString(i);
            }

            JSONArray sedifficultyLevelIdsArray = contentData.getJSONArray("se_difficultyLevelIds");
            String[] compLevelIds = new String[sedifficultyLevelIdsArray.length()];
            for (int i = 0; i < sedifficultyLevelIdsArray.length(); i++) {
                compLevelIds[i] = sedifficultyLevelIdsArray.getString(i);
            }

            JSONArray sedifficultyLevelsArray = contentData.getJSONArray("se_difficultyLevels");
            String[] compLevelNames = new String[sedifficultyLevelsArray.length()];
            for (int i = 0; i < sedifficultyLevelsArray.length(); i++) {
                compLevelNames[i] = sedifficultyLevelsArray.getString(i);
            }

            // Assuming you have competencyMap initialized somewhere
            String frameworkUrl = props.getFrameworkRead();
            competencyMap = frameworkRead(frameworkUrl);

            JSONArray competencyDetails = new JSONArray();

            for (int i = 0; i < competencyIds.length; i++) {
                String competencyId = competencyIds[i];
                Map<String, Object> compObject = competencyMap.get(competencyId);

                if (compObject != null) {
                    JSONObject competencyDetailsMainObj = new JSONObject();
                    competencyDetailsMainObj.put("competencyId", competencyId);

                    JSONObject additionalParams = new JSONObject();
                    additionalParams.put("competencyName", compObject.get("competencyName"));

                    JSONArray levelIdsArray = new JSONArray();

                    Map<String, Object> levelObjMap = (Map<String, Object>) compObject.get("levels");
                    int[] levelArray = new int[levelObjMap.size()];
                    int count = 0;
                    for (String compLevelId : compLevelIds) {
                        Map<String, Object> levelObj = (Map<String, Object>) levelObjMap.get(compLevelId);

                        if (levelObj != null) {
                            String levelName = (String) levelObj.get("levelName");
                            String levelNumber = levelName.substring(1, 2);
                            try {
                                levelArray[count] = Integer.parseInt(levelNumber);
                            } catch (Exception e) {
                                levelArray[count] = 1;
                            }
                            count++;
                        }
                    }
                    int maxLevel = Arrays.stream(levelArray)
                            .max()
                    .orElseThrow();


                    JSONObject acquiredDetails = new JSONObject();
                    acquiredDetails.put("acquiredChannel", contentData.getString("primaryCategory"));
                    acquiredDetails.put("competencyLevelId", maxLevel);

                    JSONObject acquiredDetailsAdditionalParams = new JSONObject();
                    acquiredDetailsAdditionalParams.put("courseId", courseId);
                    acquiredDetailsAdditionalParams.put("courseName", courseName);
                    acquiredDetails.put("additionalParams", acquiredDetailsAdditionalParams);

                    competencyDetailsMainObj.put("acquiredDetails", acquiredDetails);
                    competencyDetailsMainObj.put("additionalParams", additionalParams);

                    competencyDetails.put(competencyDetailsMainObj);
                }else {
                    System.out.println("Skipped competencyId: " + competencyId + " because compObject is null.");
                }
            }
                // Prepare passbook data
                JSONObject mainObj = new JSONObject();
                JSONObject requestObj = new JSONObject();
                requestObj.put("userId", userId);
                requestObj.put("typeName", "competency");
                requestObj.put("competencyDetails", competencyDetails);

                mainObj.put("request", requestObj);
                Passbook_update(userId,mainObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Map<String, Map<String, Object>> frameworkRead(String frameworkUrl) {
        Map<String, Map<String, Object>> competencyMap = new HashMap<>();

        try {
            // Make a GET request to the framework URL
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(frameworkUrl)).build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            // Parse the response as JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode frameworkJson = objectMapper.readTree(responseBody);

            // Access the framework node in the response
            JsonNode framework = frameworkJson.get("result").get("framework");

            for (JsonNode category : framework.get("categories")) {
//                List<> subjects = framework.stream()                 .filter(name -> name.getName() = "")                 .collect(Collectors.toList());
                // Inside the loop where you process each term
                if (Objects.equals(category.get("code").asText(), "subject")) {
                    for (JsonNode term : category.get("terms")) {
                        String competencyName = term.get("name").asText();
                        String competencyId = term.get("identifier").asText();

                        // Initialize levelsMap for each competency
                        Map<String, Object> levelsMap = new HashMap<>();
                        Map<String, Object> competencyDetails = new HashMap<>();
                        competencyDetails.put("competencyName", competencyName);

                        JsonNode associations = term.get("associations");

                        if (associations != null && associations.isArray()) {
                            for (JsonNode association : associations) {
                                String levelName = association.get("name").asText();
                                String levelId = association.get("identifier").asText();

                                Map<String, String> levelDetails = new HashMap<>();
                                levelDetails.put("levelName", levelName);

                                // Add levelDetails to levelsMap
                                levelsMap.put(levelId, levelDetails);
                            }
                        }
                        competencyDetails.put("levels", levelsMap);
                        // Add competencyDetails to competencyMap using competencyCode as the key
                        competencyMap.put(competencyId, competencyDetails);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return competencyMap;
    }

    public void Passbook_update(String userId,JSONObject request) throws JsonProcessingException {

        RestTemplate restTemplate = new RestTemplate();
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        restTemplate.setRequestFactory(requestFactory);

        HttpHeaders header = new HttpHeaders();
        header.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));


        String request_body = request.toString();
        String url = (props.getPassbookUpdateUrl());
        HashMap mapping = new ObjectMapper().readValue(request_body, HashMap.class);


        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(mapping, header);
        ResponseEntity<String> passbookResponse = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        String responseStr = passbookResponse.getBody();
        assert responseStr != null;
        int begin = responseStr.indexOf("{");
        int end = responseStr.lastIndexOf("}") + 1;
        responseStr = responseStr.substring(begin, end);
        JSONObject passbook_payload = new JSONObject(responseStr);

    }
}

