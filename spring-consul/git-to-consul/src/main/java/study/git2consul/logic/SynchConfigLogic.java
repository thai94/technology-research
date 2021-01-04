package study.git2consul.logic;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.kv.model.PutParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import study.git2consul.Git2ConsulProperties;
import study.git2consul.GitToConsulApplication;

import java.io.BufferedInputStream;
import java.io.IOException;

@Component
public class SynchConfigLogic {

    private static Logger LOG = LoggerFactory
            .getLogger(GitToConsulApplication.class);

    private final int BUFFER_READ_FILE_SIZE = 1024; // byte

    private ConsulClient initConsulClient(Git2ConsulProperties consulProperties) {
        int agentPort = Integer.valueOf(consulProperties.getPort());
        String agentHost = consulProperties.getHost();
        return new ConsulClient(agentHost, agentPort);
    }

    /**
     * @return
     * @throws IOException
     */
    public boolean sync(Git2ConsulProperties properties) throws IOException {

        // read config
        byte[] configData = readConfig(properties);

        // update to consul
        return sync2Consul(configData, properties);
    }

    private byte[] readConfig(Git2ConsulProperties properties) throws IOException {

        LOG.info("Reading config from: " + properties.getYamlFile());
        Resource resource = new FileSystemResource(properties.getYamlFile());

        BufferedInputStream buffInputStr = null;
        try {
            buffInputStr = new BufferedInputStream(resource.getInputStream());
            int totalByte = buffInputStr.available();
            byte[] data = new byte[totalByte];
            int position = 0;
            int numberOfByteRead = -1;
            int bufferSize = 0;
            do {
                bufferSize = buffInputStr.available() > BUFFER_READ_FILE_SIZE ? BUFFER_READ_FILE_SIZE : buffInputStr.available();
                numberOfByteRead = buffInputStr.read(data, position, bufferSize);
                if (numberOfByteRead > 0) {
                    position += numberOfByteRead;
                }
            } while (numberOfByteRead > 0);
            return data;
        } finally {
            if (buffInputStr != null) {
                buffInputStr.close();
            }
        }
    }

    private boolean sync2Consul(byte[] data, Git2ConsulProperties properties) {

        // get context
        LOG.info("Sync config to: " + properties.getConsulPath());
        Response<Boolean> resp = initConsulClient(properties).setKVBinaryValue(properties.getConsulPath(), data, properties.getToken(), new PutParams(), QueryParams.DEFAULT);
        return resp.getValue();
    }
}
