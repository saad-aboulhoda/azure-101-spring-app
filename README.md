## Azure Infrastructure Setup (Azure CLI)

To host this application, you need to provision the necessary infrastructure using the Azure CLI.

### 1. Create a Resource Group
A resource group is a logical container that contains all resources needed for a project. This makes it easy to manage, secure, and eventually clean up your project resources.
```bash
az group create --name <name> --location spaincentral
```

### 2. Create an App Service Plan
The App Service Plan acts as the Compute Tier for your app. It defines the region, operating system, and hardware (CPU/RAM) your web app will run on.
```bash
az appservice plan create --resource-group <resource-group-name> --name <name> --location spaincentral --is-linux
```

### 3. Create the Web App
This command creates the actual Web App resource inside your App Service Plan. We provision it with a placeholder Linux container (nginx:latest) to initialize it. Our GitHub Actions workflow will replace this with our actual Spring Boot application container later.

```bash
az webapp create --name <name> --plan <appservice-plan-name> --resource-group <resource-group-name> --deployment-container-image-name nginx:latest
```

### Required GitHub Secrets
To make the deployment work, you must configure two secrets in your GitHub repository settings:
* `AZURE_WEBAPP_NAME`: The name of the Web App you created via the Azure CLI.
* `AZURE_WEBAPP_PUBLISH_PROFILE`: The deployment credentials (which can be downloaded from your Web App's dashboard in the Azure Portal).