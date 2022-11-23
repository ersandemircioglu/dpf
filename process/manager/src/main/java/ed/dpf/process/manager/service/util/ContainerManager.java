package ed.dpf.process.manager.service.util;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.core.DockerClientBuilder;

public class ContainerManager {
    public void start(String imageName, String volumnMap ) {
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
        CreateContainerResponse createContainerResponse = dockerClient.createContainerCmd(imageName).withBinds(Bind.parse(volumnMap)).exec();
        dockerClient.startContainerCmd(createContainerResponse.getId()).exec();
    }
}
