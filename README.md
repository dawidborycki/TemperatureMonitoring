# TemperatureMonitoring
This repo contains sample Azure Function app used for a hypothetical temperature monitoring system

## How to use?
1. Setup the SensorsReadings table under the Storage account. Then store your connection string under AzureWebJobsStorage of the local.settings.json
2. Configure SendGrid account, and store your API key under AzureWebJobsSendGridApiKey of the local.settings.json
3. Build and run the app: 
```
mvn clean install
mvn azure-functions:run
```
