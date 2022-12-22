package ed.dpf.process.manager.controller;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import ed.dpf.process.manager.service.ProcessManager;
import lombok.extern.slf4j.Slf4j;

@Controller
@RabbitListener(queues = "${dpf.process.queue}")
@Slf4j
public class ProcessQueueListener {

    @Autowired
    private ProcessManager processManager;

    @RabbitHandler
    public void receiveMessage(Map<String, Object> record) {
        log.info("#RECORD_RECEIVED# \"{}\"", record.get("filename"));
        processManager.process(record);
    }
}
