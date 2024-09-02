package com.spring.jwt.validator.configuration;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

@Slf4j
public class DynamoDBInitializer implements BeforeAllCallback {

    private static final DockerImageName LOCALSTACK_IMAGE = DockerImageName.parse("localstack/localstack:3");

    private static final LocalStackContainer localStackContainer = new LocalStackContainer(DockerImageName.parse("localstack/localstack"))
            .withCopyFileToContainer(MountableFile.forClasspathResource("setup-development.sh", 0744), "/etc/localstack/init/ready.d/init-dynamodb.sh")
            .withServices(LocalStackContainer.Service.DYNAMODB)
            .waitingFor(Wait.forLogMessage(".*setup-development.sh*", 1));


    @Override
    public void beforeAll(final ExtensionContext context) {
        log.info("Creating localstack container : {}", LOCALSTACK_IMAGE);

        localStackContainer.start();
        addConfigurationProperties();

        log.info("Successfully started localstack container : {}", LOCALSTACK_IMAGE);
    }

    @DynamicPropertySource
    static void addConfigurationProperties() {
        System.setProperty("aws.secretKey", localStackContainer.getAccessKey());
        System.setProperty("aws.accessKey", localStackContainer.getSecretKey());
        System.setProperty("aws.region", localStackContainer.getRegion());
        System.setProperty("aws.dynamodb.endpoint", localStackContainer.getEndpoint().toString());
    }
}
