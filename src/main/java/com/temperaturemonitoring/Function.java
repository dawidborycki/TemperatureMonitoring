package com.temperaturemonitoring;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import java.util.*;

public class Function {

@FunctionName("GetMeanTemperature")
public HttpResponseMessage get(
        @TableInput(name="inputTable",                  
            tableName="SensorReadings", 
            connection="AzureWebJobsStorage",
            take="20") SensorReading[] sensorReadings,
        @HttpTrigger(
            name = "get",
            methods = {HttpMethod.GET},
            authLevel = AuthorizationLevel.ANONYMOUS)
            HttpRequestMessage<Optional<String>> request,                
        final ExecutionContext context) {

        if(sensorReadings.length == 0){
            return request.createResponseBuilder(HttpStatus.OK).
                body("Not enough sensor data").build();
        }        

        double meanTemperature = Arrays.stream(sensorReadings)
            .mapToDouble((s) -> s.getTemperature())
            .average()
            .orElse(-1);

        String responseString = String.format("Mean temperature = %.2f", meanTemperature);

        return request.createResponseBuilder(HttpStatus.OK).body(responseString).build();     
    }

    @FunctionName("CheckTemperature")
    public void checkTemperature(
            @TableInput(name="inputTable",                  
                tableName="SensorReadings", 
                connection="AzureWebJobsStorage",                
                take="1") SensorReading[] sensorReadings,           
            @TimerTrigger(name = "keepAliveTrigger", schedule = "0 */1 * * * *") String timerInfo,
            @SendGridOutput(
                name = "message",
                dataType = "String",           
                apiKey = "AzureWebJobsSendGridApiKey",     
                from = "dawid@borycki.com.pl",
                to = "dawid@borycki.com.pl",                
                subject = "Temperature alert",
                text = "")
                OutputBinding<String> message,
            final ExecutionContext context) {
        
        if(sensorReadings == null || sensorReadings.length == 0) {
            return;
        }  

        final double temperatureThreshold = 35;
        double temperature = sensorReadings[0].getTemperature();

        if(temperature >= temperatureThreshold)
        {
            final String body = PrepareMessageBody(temperature, temperatureThreshold);

            message.setValue(body);            
        }                
    }

    private static String PrepareMessageBody(double temperature, double temperatureThreshold) {
        final String value = String.format("The temperature of %.1f is above the threshold of %.1f", 
                temperature, temperatureThreshold);

        StringBuilder builder = new StringBuilder()
            .append("{")                
            .append("\"content\": [{\"type\": \"text/plain\", \"value\": \"%s\"}]")               
            .append("}");

        return String.format(builder.toString(), value);
    }
}
