package study.git2consul.logic;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.kv.model.PutParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.consul.ConsulProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import study.git2consul.GitToConsulApplication;
import study.git2consul.constant.SyncType;
import study.git2consul.exception.ServiceNotFoundException;
import study.git2consul.properties.Git2ConsulProperties;
import study.git2consul.validate.InputValidate;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;

@Component
public class SynchConfigLogic {

    private static Logger LOG = LoggerFactory
            .getLogger(GitToConsulApplication.class);

    private final int BUFFER_READ_FILE_SIZE = 1024; // byte

    @Autowired
    ConsulClient consulClient;
    @Autowired
    Git2ConsulProperties git2ConsulPropertiesl;
    @Autowired
    ConsulProperties consulProperties;
    @Value("${spring.cloud.consul.token}")
    private String aclToken;

    /**
     * @param releaseVersion x.y.x-<incresing number>
     * @param env            qc|stg|real
     * @return
     * @throws IOException
     */
    public boolean synchPubConfig(String serviceName, String releaseVersion, String env, String workSpaceDir) throws IOException {

        Git2ConsulProperties.ServiceConfig serviceConfig = git2ConsulPropertiesl.findServiceConfigByServiceName(serviceName);

        if (serviceConfig == null) {
            throw new ServiceNotFoundException(serviceName);
        }

        InputValidate.validatePubParams(serviceName, releaseVersion, env, workSpaceDir);

        // read config
        byte[] configData = readConfig(workSpaceDir, env, SyncType.PUB, serviceConfig);

        // update to consul
        return sync2Consul(configData, releaseVersion, env, SyncType.PUB, serviceConfig);
    }

    public boolean synchSecretConfig(String serviceName, String env, String workSpaceDir) throws IOException {

        Git2ConsulProperties.ServiceConfig serviceConfig = git2ConsulPropertiesl.findServiceConfigByServiceName(serviceName);

        if (serviceConfig == null) {
            throw new ServiceNotFoundException(serviceName);
        }

        InputValidate.validateSecretParams(serviceName, env, workSpaceDir);

        // read config
        byte[] configData = readConfig(workSpaceDir, env, SyncType.SECRET, serviceConfig);

        // update to consul
        return sync2Consul(configData, "none", env, SyncType.SECRET, serviceConfig);
    }

    private byte[] readConfig(String workSpaceDir, String env, SyncType syncType, Git2ConsulProperties.ServiceConfig serviceConfig) throws IOException {

        String path = "";
        // public
        if (syncType == SyncType.PUB) {
            path = workSpaceDir + File.separator + serviceConfig.getPubPathGit() + File.separator + env + File.separator
                    + git2ConsulPropertiesl.getGitConfigFileName();
        } else {
            path = workSpaceDir + File.separator + serviceConfig.getSecretPathGit() + File.separator + env + File.separator
                    + git2ConsulPropertiesl.getGitConfigFileName();
        }
        path = path.replaceAll(File.separator + File.separator, File.separator);
        Resource resource = new FileSystemResource(path);

        BufferedInputStream buffInputStr = new BufferedInputStream(resource.getInputStream());
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
    }

    private boolean sync2Consul(byte[] data, String releaseVersion, String env, SyncType syncType, Git2ConsulProperties.ServiceConfig serviceConfig) {

        // get context
        String key = "";
        if (syncType == SyncType.PUB) {
            key = serviceConfig.getPubPathConsul() + File.separator + env + File.separator + releaseVersion + File.separator
                    + git2ConsulPropertiesl.getDataKey();
        } else {
            key = serviceConfig.getSecretPathConsul() + File.separator + env + File.separator + git2ConsulPropertiesl.getDataKey();
        }
        key = key.replaceAll(File.separator + File.separator, File.separator);

        Response<Boolean> resp = consulClient.setKVBinaryValue(key, data, aclToken, new PutParams(), QueryParams.DEFAULT);
        return resp.getValue();
    }
}
