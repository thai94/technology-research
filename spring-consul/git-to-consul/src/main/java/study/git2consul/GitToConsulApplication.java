package study.git2consul;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import study.git2consul.logic.SynchConfigLogic;

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
     * 0: version
     * 1: env
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        LOG.info("==> Starting");
        LOG.info(String.format("Params: %s <=> %s", args[0], args[1]));

        int resultPub = synchConfigLogic.synchPubConfig(args[0], args[1]);
        int resultSecret = synchConfigLogic.synchSecretConfig(args[0], args[1]);
        LOG.info("==> Ended");
        if (resultPub == 1 && resultSecret == 1) {
            System.exit(1);
            return;
        }
        System.exit(0);
    }
}
