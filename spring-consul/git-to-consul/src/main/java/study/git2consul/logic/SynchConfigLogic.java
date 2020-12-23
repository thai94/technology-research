package study.git2consul.logic;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.kv.model.PutParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.consul.ConsulProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import study.git2consul.properties.Git2ConsulProperties;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class SynchConfigLogic {

    @Autowired
    ConsulClient consulClient;

    @Autowired
    Git2ConsulProperties git2ConsulPropertiesl;

    @Autowired
    ConsulProperties consulProperties;

    @Value("${spring.cloud.consul.token}")
    private String aclToken;

    private final String DATA_KEY = "application";

    public int synchPubConfig(String version, String env) throws IOException {

        // read config
        byte[] dataConfig = readConfig(version, env, 1);

        // update to consul
        boolean result = update2Consul(dataConfig, version, env, 1);
        if(result) {
            return 1;
        }
        return 0;
    }

    public int synchSecretConfig(String version, String env) throws IOException {
        // read config
        byte[] dataConfig = readConfig(version, env, 2);

        // update to consul
        boolean result = update2Consul(dataConfig, version, env, 2);
        if(result) {
            return 1;
        }
        return 0;
    }

    private byte[] readConfig(String version, String env, int type) throws IOException {
        Resource resource = null;
        // public
        if(type == 1) {
            resource = new FileSystemResource(git2ConsulPropertiesl.getBaseDir() + git2ConsulPropertiesl.getContextPub() + "/" + env + "/" + DATA_KEY + ".yaml");
        } else {
            resource = new FileSystemResource(git2ConsulPropertiesl.getBaseDir() + git2ConsulPropertiesl.getContextSecret() + "/" + env + "/" + DATA_KEY + ".yaml");
        }

        BufferedInputStream buffInputStr = new BufferedInputStream(resource.getInputStream());
        int rem_byte = buffInputStr.available();
        byte[] data = new byte[rem_byte];
        int position = 0;
        int numberReadByte = -1;
        int block = 1028;
        do {
            block = buffInputStr.available() > 1028 ? 1028 : buffInputStr.available();
            numberReadByte = buffInputStr.read(data, position, block);
            if(numberReadByte > 0) {
                position += numberReadByte;
            }
        } while (numberReadByte > 0);
        return data;
    }

    private boolean update2Consul(byte[] data, String version, String env, int type) {

        // get context
        String context = "";
        if(type == 1) {
            context = getPubContext(version, env);
        } else {
            context = getSecretContext(version, env);
        }

        String key = context + "/" + DATA_KEY;
        Response<Boolean> resp = consulClient.setKVBinaryValue(key, data, aclToken, new PutParams(), QueryParams.DEFAULT);
        return resp.getValue();
    }

    private String getSecretContext(String version, String env) {
        return git2ConsulPropertiesl.getContextSecret() + "/" + env;
    }

    private String getPubContext(String version, String env) {
        String baseContext = git2ConsulPropertiesl.getContextPub() + "/" + env + "/";
        Response<List<String>> keysResp = this.consulClient.getKVKeysOnly(baseContext, "", aclToken);
        List<String> keys = keysResp.getValue();
        Pattern pattern = Pattern.compile(baseContext + version + "-[0-9]+/" + DATA_KEY + "$");
        List<String> filterKeys = new ArrayList<>();
        for(String key : keys) {
            if(pattern.matcher(key).matches()) {
                filterKeys.add(key);
            }
        }
        if(filterKeys.size() == 0) {
            return baseContext + version + "-1";
        }
        Collections.sort(filterKeys);
        String context = filterKeys.get(filterKeys.size() -1);
        int startIdxOfPostFix = context.lastIndexOf("-");
        int endIdxOfPostFix = context.substring(startIdxOfPostFix).indexOf("/");
        int postFixNumber = Integer.valueOf(context.substring(startIdxOfPostFix + 1, startIdxOfPostFix + endIdxOfPostFix));
        return context.substring(0, startIdxOfPostFix + 1) + (postFixNumber + 1);
    }
}
