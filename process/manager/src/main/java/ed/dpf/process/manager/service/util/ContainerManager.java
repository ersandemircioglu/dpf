package ed.dpf.process.manager.service.util;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.core.DockerClientBuilder;

public class ContainerManager {
    public void start(String imageName, String inputFolder, String outputFolder) {
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
        CreateContainerCmd createContainerCmd = dockerClient.createContainerCmd(imageName);
        HostConfig hostConfig = new HostConfig();
        hostConfig.withBinds(Bind.parse(inputFolder + ":/input/"), Bind.parse(outputFolder + ":/output/"));
        createContainerCmd.withHostConfig(hostConfig);
        CreateContainerResponse createContainerResponse = createContainerCmd.exec();
        dockerClient.startContainerCmd(createContainerResponse.getId()).exec();
    }
}
