package study.git2consul.controller;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.kv.model.PutParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.consul.ConsulProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.git2consul.properties.Git2ConsulProperties;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@RestController
public class SynchConfigController {

    @Autowired
    ConsulClient consul;

    @Autowired
    Git2ConsulProperties git2ConsulPropertiesl;

    @Autowired
    ConsulProperties consulProperties;

    @Value("${spring.cloud.consul.token}")
    private String aclToken;

    private final String DATA_KEY = "application";

    @RequestMapping("/synch-config/{version}/{env}")
    public String index(@PathVariable("version") String version, @PathVariable("env") String env) throws IOException {

        // read config
        byte[] dataConfig = readConfig(version, env);

        // update to consul
        boolean result = update2Consul(dataConfig, version, env);
        if(result) {
            return "success";
        }
        return "failure";
    }

    private byte[] readConfig(String version, String env) throws IOException {
        Resource resource = new ClassPathResource(git2ConsulPropertiesl.getContextPub() + "/" + env + "/" + DATA_KEY + ".yaml");
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

    private boolean update2Consul(byte[] data, String version, String env) {

        // get context
        String context = getContext(version, env);
        String key = context + "/" + DATA_KEY;
        Response<Boolean> resp = consul.setKVBinaryValue(key, data, aclToken, new PutParams(), QueryParams.DEFAULT);
        return resp.getValue();
    }

    private String getContext(String version, String env) {
        String baseContext = git2ConsulPropertiesl.getContextPub() + "/" + env + "/";
        Response<List<String>> keysResp = this.consul.getKVKeysOnly(baseContext, "", aclToken);
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
