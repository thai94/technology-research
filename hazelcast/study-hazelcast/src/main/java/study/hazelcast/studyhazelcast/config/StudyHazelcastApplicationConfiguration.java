package study.hazelcast.studyhazelcast.config;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudyHazelcastApplicationConfiguration {

    HazelcastProperties hazelcastProperties;

    public StudyHazelcastApplicationConfiguration(HazelcastProperties hazelcastProperties) {
        this.hazelcastProperties = hazelcastProperties;
    }

    @Bean
    HazelcastInstance hazelcastInstance() {

        Config hazelCastConf = new Config();
//        hazelCastConf.getNetworkConfig().setPort(hazelcastProperties.port).setPortAutoIncrement(false);
//        hazelCastConf.setProperty("hazelcast.shutdownhook.policy", "GRACEFUL");
//        JoinConfig join = hazelCastConf.getNetworkConfig().getJoin();
//        join.getMulticastConfig().setEnabled(false);
//        join.getTcpIpConfig().setEnabled(true).clear().setMembers(Arrays.asList(hazelcastProperties.tcpIpMembers.split(";")));
//        hazelCastConf.setInstanceName(hazelcastProperties.instanceName);

        hazelCastConf.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        hazelCastConf.getNetworkConfig().getJoin().getKubernetesConfig().setEnabled(true)
                .setProperty("namespace", hazelcastProperties.namespace)
                .setProperty("service-name", hazelcastProperties.serviceName);
        return Hazelcast.newHazelcastInstance(hazelCastConf);
    }
}
