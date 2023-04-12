package com.evision.readcsv.kafka;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
public class KafkaManager {

	public static boolean createTopic(String kafkaUrl, String topicName) {
        try (AdminClient adminClient = AdminClient.create(getAdminProperties(kafkaUrl))) {
            NewTopic newTopic = new NewTopic(topicName, 1,Short.valueOf("1"));
            List<NewTopic> newTopics = new ArrayList<>();
            newTopics.add(newTopic);
            adminClient.createTopics(newTopics).all().get();
            return true;
        }catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
        
    }
	public static Set<String> getTopics(String kafkaUrl) {
        try (AdminClient adminClient = AdminClient.create(getAdminProperties(kafkaUrl))) {
            ListTopicsResult topics = adminClient.listTopics();
            return topics.names().get();
        }catch (Exception e) {
			System.out.println(e.getMessage());
        }
		
        return null;
    }

    public static boolean deleteTopic(String kafkaUrl, String topicName) {
        try (AdminClient adminClient = AdminClient.create(getAdminProperties(kafkaUrl))) {
            List<String> deleteTopic = new ArrayList<>();
            deleteTopic.add(topicName);
            adminClient.deleteTopics(deleteTopic).all().get();
            return true;
        }catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
    }
	
	private static Properties getAdminProperties(String kafkaUrl) {
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl);
        return properties;
    }
}
