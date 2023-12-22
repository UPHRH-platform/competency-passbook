package com.sphere.compentencytool.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {
	@Value("${kafka.topic}")
	private String kafkaTopic;

	@Value("${kafka.groupID}")
	private String kafkaGroupID;

	@Value("${get.hierarchy}")
	private String getHierarchyApi;

	@Value("${Api.key}")
	private String Apikey;

	@Value("${get.entityById}")
	private String getEntityById;
	@Value("${passbook.update.url}")
	private String passbookUpdateUrl;

	@Value("${kafka.bootstrapServers}")
	private String kafkaBootstrapServers;

	public String getKafkaBootstrapServers() {
		return kafkaBootstrapServers;
	}

	public void setKafkaBootstrapServers(String kafkaBootstrapServers) {
		this.kafkaBootstrapServers = kafkaBootstrapServers;
	}

	public String getKafkaTopic() {
		return kafkaTopic;
	}

	public void setKafkaTopic(String kafkaTopic) {
		this.kafkaTopic = kafkaTopic;
	}

	public String getKafkaGroupID() {
		return kafkaGroupID;
	}

	public void setKafkaGroupID() {
		this.kafkaGroupID = kafkaGroupID;
	}

	public String getGetHierarchyApi() {
		return getHierarchyApi;
	}

	public void setGetHierarchyApi(String getHierarchyApi) {
		this.getHierarchyApi = getHierarchyApi;
	}

	public String getApikey() {
		return Apikey;
	}

	public void setApikey(String apikey) {
		Apikey = apikey;
	}

	public String getGetEntityById() {
		return getEntityById;
	}

	public void setGetEntityById(String getEntityById) {
		this.getEntityById = getEntityById;
	}

	public String getPassbookUpdateUrl() {
		return passbookUpdateUrl;
	}

	public void setPassbookUpdateUrl(String passbookUpdateUrl) {
		this.passbookUpdateUrl = passbookUpdateUrl;
	}

	@Value("${user.read.api}")
	private String UserReadApi;
	
	@Value("${content.read.api}")
	private String ContentReadAapi;
	
	@Value("${generate.token}")
	private String GenerateToken;
	
	public String getGenerateToken() {
		return GenerateToken;
	}

	public void setGenerateToken(String generateToken) {
		GenerateToken = generateToken;
	}

	public String getVerifyToken() {
		return VerifyToken;
	}

	public void setVerifyToken(String verifyToken) {
		VerifyToken = verifyToken;
	}

	@Value("${verify.token}")
	private String VerifyToken;
	
	public String getUserReadApi() {
		return UserReadApi;
	}

	public void setUserReadApi(String userReadApi) {
		UserReadApi = userReadApi;
	}

	public String getContentReadAapi() {
		return ContentReadAapi;
	}

	public void setContentReadAapi(String contentReadAapi) {
		ContentReadAapi = contentReadAapi;
	}

	
}
