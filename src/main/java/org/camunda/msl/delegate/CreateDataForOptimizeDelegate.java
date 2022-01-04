package org.camunda.msl.delegate;

import io.camunda.zeebe.client.ZeebeClient;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.msl.ProcessConstants;
import org.camunda.msl.dto.NewApplication;
import org.camunda.msl.dto.Person;
import org.camunda.msl.helper.ZeebeClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.logging.Logger;

@Component("CreateDataForOptimizeDelegate")
public class CreateDataForOptimizeDelegate implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Value("${bpmm.processId}")
    private String bpmnProcessId;
    @Value("${bpmm.name}")
    private String bpmnName;

    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("CreateDataForOptimizeDelegate - IN - "+ new Date());

        int red = (Integer)execution.getVariable("red");
        int yellow = (Integer)execution.getVariable("yellow");
        int green = (Integer)execution.getVariable("green");

        Person person = null;
        NewApplication application = null;

        String clientId = (String)execution.getVariable("clientId");
        String clientSecret = (String)execution.getVariable("clientSecret");
        String clusterId = (String)execution.getVariable("clusterId");
        String region = (String)execution.getVariable("region");

        ZeebeClient client = ZeebeClientFactory.getZeebeClient(clusterId,clientId,clientSecret,region);

        client.newDeployCommand()
                .addResourceFromClasspath(bpmnName)
                .send()
                .join();


       application = new NewApplication();
       person = new Person();
       application.setApplicant(person);

        application.setEmployment("Unemployed");
        application.setCategory("Premium Package");
        application.setPriceIndication("42");
        application.getApplicant().setName("John Doe");
        application.getApplicant().setAge(21);
        application.getApplicant().setBirthday( new GregorianCalendar(2000,Calendar.JANUARY,1).getTime());


        Map<String, Object> variables = new HashMap<>();
        variables.put(ProcessConstants.VAR_NAME_applicationNumber, application.getApplicationNumber());
        variables.put(ProcessConstants.VAR_NAME_approved, false);
        variables.put(ProcessConstants.VAR_NAME_employment, application.getEmployment());
        variables.put(ProcessConstants.VAR_NAME_category, application.getCategory());
        variables.put(ProcessConstants.VAR_NAME_priceIndication, application.getPriceIndication());
        variables.put(ProcessConstants.VAR_NAME_applicantName, application.getApplicant().getName());
        variables.put(ProcessConstants.VAR_NAME_applicantAge, application.getApplicant().getAge());
        variables.put(ProcessConstants.VAR_NAME_application, "CloudStarter");
        variables.put(ProcessConstants.VAR_NAME_application, application);
        variables.put(ProcessConstants.VAR_NAME_documents, "{}");

        for(int idx = 0 ; idx < red; idx++) {
            client
                    .newCreateInstanceCommand()
                    .bpmnProcessId(bpmnProcessId)
                    .latestVersion()
                    .variables(variables)
                    .send()
                    .join();
        }

        application = new NewApplication();
        person = new Person();
        application.setApplicant(person);

        application.setEmployment("Self-employed");
        application.setCategory("Premium Package");
        application.setPriceIndication("42");
        application.getApplicant().setName("John Doe");
        application.getApplicant().setAge(21);
        application.getApplicant().setBirthday( new GregorianCalendar(2000,Calendar.JANUARY,1).getTime());


        variables = new HashMap<>();
        variables.put(ProcessConstants.VAR_NAME_applicationNumber, application.getApplicationNumber());
        variables.put(ProcessConstants.VAR_NAME_approved, false);
        variables.put(ProcessConstants.VAR_NAME_employment, application.getEmployment());
        variables.put(ProcessConstants.VAR_NAME_category, application.getCategory());
        variables.put(ProcessConstants.VAR_NAME_priceIndication, application.getPriceIndication());
        variables.put(ProcessConstants.VAR_NAME_applicantName, application.getApplicant().getName());
        variables.put(ProcessConstants.VAR_NAME_applicantAge, application.getApplicant().getAge());
        variables.put(ProcessConstants.VAR_NAME_application, "CloudStarter");
        variables.put(ProcessConstants.VAR_NAME_application, application);
        variables.put(ProcessConstants.VAR_NAME_documents, "{}");

        for(int idx = 0 ; idx < yellow; idx++) {
            client
                    .newCreateInstanceCommand()
                    .bpmnProcessId(bpmnProcessId)
                    .latestVersion()
                    .variables(variables)
                    .send()
                    .join();
        }

        application = new NewApplication();
        person = new Person();
        application.setApplicant(person);

        application.setEmployment("Freelancer");
        application.setCategory("Premium Package");
        application.setPriceIndication("42");
        application.getApplicant().setName("John Doe");
        application.getApplicant().setAge(33);
        application.getApplicant().setBirthday( new GregorianCalendar(1988,Calendar.JANUARY,1).getTime());

        variables = new HashMap<>();
        variables.put(ProcessConstants.VAR_NAME_applicationNumber, application.getApplicationNumber());
        variables.put(ProcessConstants.VAR_NAME_approved, false);
        variables.put(ProcessConstants.VAR_NAME_employment, application.getEmployment());
        variables.put(ProcessConstants.VAR_NAME_category, application.getCategory());
        variables.put(ProcessConstants.VAR_NAME_priceIndication, application.getPriceIndication());
        variables.put(ProcessConstants.VAR_NAME_applicantName, application.getApplicant().getName());
        variables.put(ProcessConstants.VAR_NAME_applicantAge, application.getApplicant().getAge());
        variables.put(ProcessConstants.VAR_NAME_application, "CloudStarter");
        variables.put(ProcessConstants.VAR_NAME_application, application);
        variables.put(ProcessConstants.VAR_NAME_documents, "{}");

        for(int idx = 0 ; idx < green; idx++) {
            client
                    .newCreateInstanceCommand()
                    .bpmnProcessId(bpmnProcessId)
                    .latestVersion()
                    .variables(variables)
                    .send()
                    .join();
        }

        LOGGER.info("CreateDataForOptimizeDelegate - OUT");
    }
}
