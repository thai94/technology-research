package study.git2consul.validate;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import study.git2consul.exception.InvalidParamsException;

@RunWith(SpringRunner.class)
public class InputValidateTests {

    @Test
    public void test_validateReleaseVersion_success(){
        InputValidate.validateReleaseVersion("2.0.0-1");
        InputValidate.validateReleaseVersion("2.0.0-200");
        InputValidate.validateReleaseVersion("2.0.999-200");
    }

    @Test
    public void test_validateReleaseVersion_fail(){

        int count = 0;
        try {
            InputValidate.validateReleaseVersion("2.0.0");
        } catch (InvalidParamsException ex) {
            count++;
        }

        try {
            InputValidate.validateReleaseVersion("2.0.0-a");
        } catch (InvalidParamsException ex) {
            count++;
        }

        try {
            InputValidate.validateReleaseVersion("2.b.999-200");
        } catch (InvalidParamsException ex) {
            count++;
        }

        Assert.assertEquals(3, count);
    }

    @Test
    public void test_validateEnv_success() {
        InputValidate.validateEnv("dev");
        InputValidate.validateEnv("qc");
        InputValidate.validateEnv("stg");
        InputValidate.validateEnv("staging");
        InputValidate.validateEnv("real");
        InputValidate.validateEnv("production");
    }

    @Test
    public void test_validateEnv_fail() {

        int count = 0;
        try {
            InputValidate.validateEnv("");
        } catch (InvalidParamsException ex) {
            count++;
        }

        try {
            InputValidate.validateEnv("abc");
        } catch (InvalidParamsException ex) {
            count++;
        }

        Assert.assertEquals(2, count);
    }
}
