package study.git2consul;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import study.git2consul.constant.SyncType;
import study.git2consul.exception.InvalidNumberOfParamsException;
import study.git2consul.exception.InvalidParamsException;
import study.git2consul.logic.SynchConfigLogic;
import vn.zalopay.base.utils.GsonUtils;

@SpringBootApplication
public class GitToConsulApplication implements CommandLineRunner {

    private static Logger LOG = LoggerFactory
            .getLogger(GitToConsulApplication.class);
    @Autowired
    SynchConfigLogic synchConfigLogic;

    public static void main(String[] args) {
        SpringApplication.run(GitToConsulApplication.class, args);
    }

    /**
     * @param args args[0] serviceName
     *             args[1] releaseVersion x.y.z-incresingNumber
     *             args[2] env
     *             args[3] workSpaceDir
     *             args[4] syncType. pup|secret|all
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {

        if (args.length != 5) {
            throw new InvalidNumberOfParamsException(GsonUtils.toJsonString(args));
        }

        String serviceName = args[0];
        String releaseVersion = args[1];
        String env = args[2];
        String workSpaceDir = args[3];
        SyncType syncType = SyncType.getEnum(args[4]);
        if (syncType == null) {
            throw new InvalidParamsException("syncType", args[4], "pup|secret|all");
        }

        if(syncType == SyncType.PUB) {
            boolean result = synchConfigLogic.synchPubConfig(serviceName, releaseVersion, env, workSpaceDir);
            if(!result) {
                System.exit(-1);
            }
            System.exit(1);
        } else if(syncType == SyncType.SECRET) {
            boolean result = synchConfigLogic.synchSecretConfig(serviceName, env, workSpaceDir);
            if(!result) {
                System.exit(-1);
            }
            System.exit(1);
        } else {
            boolean resultPub = synchConfigLogic.synchPubConfig(serviceName, releaseVersion, env, workSpaceDir);
            boolean resultSecret = synchConfigLogic.synchSecretConfig(serviceName, env, workSpaceDir);
            if(!resultPub || !resultSecret) {
                System.exit(-1);
            }
            System.exit(1);
        }
    }
}
