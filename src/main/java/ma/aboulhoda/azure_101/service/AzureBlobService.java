package ma.aboulhoda.azure_101.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

@Service
public class AzureBlobService {

  @Value("${azure.storage.connection-string}")
  private String connectionString;

  @Value("${azure.storage.container-name}")
  private String containerName;

  public String uploadImage(MultipartFile file) throws IOException {
    BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
        .connectionString(connectionString)
        .buildClient();

    BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

    // Create container if it doesn't exist and make it public for reading images
    if (!containerClient.exists()) {
      containerClient.create();
    }

    // Generate a unique file name to avoid overwriting
    String originalFilename = file.getOriginalFilename();
    String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFilename;

    BlobClient blobClient = containerClient.getBlobClient(uniqueFileName);

    // Upload the file
    blobClient.upload(file.getInputStream(), file.getSize(), true);

    // Return the public URL of the uploaded image
    return blobClient.getBlobUrl();
  }
}