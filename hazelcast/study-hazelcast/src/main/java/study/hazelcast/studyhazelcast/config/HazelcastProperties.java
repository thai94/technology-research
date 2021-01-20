package study.hazelcast.studyhazelcast.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("study.hazelcast")
public class HazelcastProperties {

    public int port;
    public String tcpIpMembers;
    public String instanceName;

    public String namespace;
    public String serviceName;

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getTcpIpMembers() {
        return tcpIpMembers;
    }

    public void setTcpIpMembers(String tcpIpMembers) {
        this.tcpIpMembers = tcpIpMembers;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }
}
