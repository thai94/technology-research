package study.git2consul;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import study.git2consul.logic.SynchConfigLogic;
import study.git2consul.validate.InputValidate;
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

        try {
            LOG.info("Run with params: " + GsonUtils.toJsonString(args));
            Options options = new Options();
            options.addRequiredOption("h", "host", true, "Consul host");
            options.addRequiredOption("p", "port", true, "Consul port");
            options.addRequiredOption("t", "token", true, "Consul token");
            options.addRequiredOption("", "yml-file", true, "Path to yaml file");
            options.addRequiredOption("", "consul-path", true, "Consul path");

            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            Git2ConsulProperties properties = new Git2ConsulProperties();
            properties.setHost(cmd.getOptionValue("host"));
            properties.setPort(cmd.getOptionValue("port"));
            properties.setToken(cmd.getOptionValue("token"));
            properties.setYamlFile(cmd.getOptionValue("yml-file"));
            properties.setConsulPath(cmd.getOptionValue("consul-path"));

            InputValidate.validateParams(properties);

            boolean result = synchConfigLogic.sync(properties);
            if(result) {
                LOG.info("Finish: success");
                System.exit(1);
            }
            LOG.info("Finish: fail");
            System.exit(-1);

        } catch (Exception ex) {
            LOG.error("git2consul ex!", ex);
            System.exit(-1);
        }
    }
}
