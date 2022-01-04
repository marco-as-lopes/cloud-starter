package org.camunda.msl.delegate;

import org.apache.commons.io.FileUtils;
import org.apache.maven.shared.invoker.*;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

@Component("ConfigureLunchDemoDelegate")
public class ConfigureLunchDemoDelegate implements JavaDelegate {
    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Value("${demo.maven.home}")
    private String mavenHome;

    @Value("${demo.dmn.pomLocation}")
    private String dmnPom;
    @Value("${demo.dmn.appFileLocation}")
    private String dmnApp;
    @Value("${demo.dmn.jarLocation}")
    private String dmnJarFolder;
    @Value("${demo.dmn.dmn-repo}")
    private String dmnRepo;

    @Value("${demo.client.appFileLocation}")
    private String clientApp;
    @Value("${demo.client.pomLocation}")
    private String clientPom;
    @Value("${demo.client.jarLocation}")
    private String clientJarFolder;

    @Value("${demo.email.appFileLocation}")
    private String emailApp;
    @Value("${demo.email.pomLocation}")
    private String emailPom;
    @Value("${demo.email.jarLocation}")
    private String emailJarFolder;

    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("ConfigureLunchDemoDelegate - IN - "+ new Date());

        String clusterId = (String)execution.getVariable("clusterId");
        String clientSecret = (String)execution.getVariable("clientSecret");
        String clientId = (String)execution.getVariable("clientId");
        String region = (String)execution.getVariable("region");

        FileWriter writer = null;
        InputStream inputStream = null;
        Map<String, Object> data = null;
        File dir = null;
        List<File> files = null;
        InvocationRequest request = null;
        Invoker invoker = null;
        InvocationResult result = null;

        Yaml yaml = new Yaml();
        Properties properties = new Properties();
        properties.setProperty("skipTests", "true");

        LOGGER.info("DMN Worker - config"+ new Date());
        inputStream = new FileInputStream(new File(dmnApp));

        data = yaml.load(inputStream);

        {
            Map<String, Object> zeebe = (Map) data.get("zeebe");
            Map<String, Object> client = (Map) zeebe.get("client");
            Map<String, String> cloud = (Map) client.get("cloud");
            cloud.put("clusterId",clusterId);
            cloud.put("clientId",clientId);
            cloud.put("clientSecret",clientSecret);
        }

        writer = new FileWriter(dmnApp);
        yaml.dump(data, writer);

        LOGGER.info("DMN Worker - build"+ new Date());
        request = new DefaultInvocationRequest();
        request.setPomFile( new File( dmnPom ) );
        request.setProperties(properties);
        request.setGoals(Arrays.asList("clean","package"));

        invoker = new DefaultInvoker();
        invoker.setMavenHome(new File(mavenHome));
        result = invoker.execute( request );

        if(result.getExitCode() != 0 )
        {
            throw new BpmnError("Build failed! - DMN");
        }

        LOGGER.info("DMN Worker - run"+ new Date());
        File sourceDirectory = new File(dmnRepo);
        File destinationDirectory = new File(dmnJarFolder+"dmn-repo");
        FileUtils.copyDirectory(sourceDirectory, destinationDirectory);

        dir = new File(dmnJarFolder);
        files = (List<File>) FileUtils.listFiles(dir, new String [] {"jar"} , false);

        Runtime.getRuntime().exec("cmd /c start \"\" java -jar "+ dmnJarFolder+files.get(0).getName(),
                null, new File(dmnJarFolder));

        LOGGER.info("Client - config "+ new Date());
        inputStream = new FileInputStream(new File(clientApp));

        data = yaml.load(inputStream);

        {
            Map<String, String> cloud = (Map) data.get("zeebe.client.cloud");
            cloud.put("clusterId",clusterId);
            cloud.put("clientId",clientId);
            cloud.put("clientSecret",clientSecret);
        }
        writer = new FileWriter(clientApp);
        yaml.dump(data, writer);

        LOGGER.info("Client - build "+ new Date());
        request = new DefaultInvocationRequest();
        request.setPomFile( new File(clientPom) );
        request.setProperties(properties);
        request.setGoals(Arrays.asList("clean","package"));

        invoker = new DefaultInvoker();
        invoker.setMavenHome(new File(mavenHome));
        result = invoker.execute( request );

        if(result.getExitCode() != 0 )
        {
            throw new BpmnError("Build failed! - Demo");
        }

        LOGGER.info("Client - run"+ new Date());
        dir = new File(clientJarFolder);
        files = (List<File>) FileUtils.listFiles(dir, new String [] {"jar"} , false);

        Runtime.getRuntime().exec("cmd /c start \"\" java -jar "+ clientJarFolder+files.get(0).getName(),
                null, new File(clientJarFolder));


        LOGGER.info("Email Worker - config"+ new Date());
        FileInputStream in = new FileInputStream(emailApp);
        Properties props = new Properties();
        props.load(in);
        in.close();

        props.setProperty("zeebe.client.cloud.region",region);
        props.setProperty("zeebe.client.cloud.clusterId",clusterId);
        props.setProperty("zeebe.client.cloud.clientId",clientId);
        props.setProperty("zeebe.client.cloud.clientSecret",clientSecret);

        FileOutputStream out = new FileOutputStream(emailApp);
        props.store(out, null);
        out.close();

        LOGGER.info("Email Worker - build"+ new Date());
        request = new DefaultInvocationRequest();
        request.setPomFile(new File(emailPom) );
        request.setProperties(properties);
        request.setGoals(Arrays.asList("clean","package"));

        invoker = new DefaultInvoker();
        invoker.setMavenHome(new File(mavenHome));
        result = invoker.execute( request );

        if(result.getExitCode() != 0 )
        {
            throw new BpmnError("Build failed! - Email");
        }

        LOGGER.info("Email Worker - run"+ new Date());
        dir = new File(emailJarFolder);
        files = (List<File>) FileUtils.listFiles(dir, new String [] {"jar"} , false);

        Runtime.getRuntime().exec("cmd /c start \"\" java -jar "+ emailJarFolder+files.get(0).getName(),
                null, new File(emailJarFolder));


        LOGGER.info("Open browser"+ new Date());
        Runtime rt = Runtime.getRuntime();
        rt.exec( "rundll32 url.dll,FileProtocolHandler http://localhost:8080/camunda/online/banking/index.html?lang=en");


        LOGGER.info("ConfigureLunchDemoDelegate - OUT");
    }
}
