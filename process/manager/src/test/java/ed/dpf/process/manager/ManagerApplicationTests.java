package ed.dpf.process.manager;

import org.junit.jupiter.api.Test;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.core.DockerClientBuilder;

class ManagerApplicationTests {


    @SuppressWarnings("deprecation")
    @Test
    void testDockerClient(){
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
        CreateContainerResponse createContainerResponse = dockerClient.createContainerCmd("processor01").withBinds(Bind.parse("/home/user/dpf/process/output:/out")).exec();
        dockerClient.startContainerCmd(createContainerResponse.getId()).exec();
        System.out.println("Done");

    }

}
