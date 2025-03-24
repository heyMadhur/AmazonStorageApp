# Storage Service

This Spring Boot application provides a REST API for managing file storage for users. It leverages Amazon S3 as the storage backend and allows users to search, download, and optionally upload files from/to their personal storage folders within a single S3 bucket.

## Features

- **Search Files:** Locate files within a user's folder by filtering based on a filename search term.
- **Download Files:** Retrieve files from the S3 bucket.
- **Upload Files (Optional):** Upload files into a user's folder on S3.
- **REST Compliant APIs:** Designed to be testable via Postman or Swagger.
- **S3 Integration:** All files are stored on a single S3 bucket with each user having their own folder.
- **Extensible Design:** Built to be easily extended and maintained.

## Prerequisites

- Java 21
- Maven 
- AWS account with access to S3
- AWS credentials (Access Key and Secret Key)
- An existing S3 bucket where files will be stored

## Configuration

Configure the AWS credentials, region, and S3 bucket name in your `application.properties` (or `application.yml`) file:

```properties
# AWS Credentials
cloud.aws.credentials.access-key=<YOUR_AWS_ACCESS_KEY>
cloud.aws.credentials.secret-key=<YOUR_AWS_SECRET_KEY>
cloud.aws.region.static=<YOUR_AWS_REGION>

# S3 Bucket name
app.s3.bucket=<YOUR_S3_BUCKET_NAME>
```

## Building and Running the Application

1. **Build the project:**

   Open a terminal at the project root and run:
   ```bash
   mvn clean install
   ```

2. **Run the application:**

   You can start the Spring Boot application using:
   ```bash
   mvn spring-boot:run
   ```
   or by running the generated jar:
   ```bash
   java -jar target/StorageApp-0.0.1-SNAPSHOT.jar
   ```

## API Endpoints

### 1. Search Files

- **Endpoint:** `GET /api/files/search`
- **Parameters:**
    - `username` (String): User's folder name.
    - `searchTerm` (String): Term to search in filenames.
- **Response:** A JSON array of file details containing:
    - `filename` – Name of the file (relative to the user's folder).
    - `lastModified` – Timestamp of last modification.
    - `size` – File size in bytes.
- **Example Request:**
  ```bash
  GET http://localhost:8080/api/files/search?username=sandy&searchTerm=logistics
  ```

### 2. Download File

- **Endpoint:** `GET /api/files/download`
- **Parameters:**
    - `username` (String): User's folder name.
    - `filename` (String): Name of the file to download.
- **Response:** Returns the file as an attachment.
- **Example Request:**
  ```bash
  GET http://localhost:8080/api/files/download?username=sandy&filename=example.txt
  ```

### 3. Upload File (Optional)

- **Endpoint:** `POST /api/files/upload`
- **Parameters:**
    - `username` (String): User's folder name.
    - `file` (MultipartFile): The file to be uploaded.
- **Content-Type:** `multipart/form-data`
- **Example Request (using Postman):**
    - Set the request type to POST.
    - URL: `http://localhost:8080/api/files/upload?username=sandy`
    - Under **Body**, select **form-data** and add a key named `file` of type `File` with the file to upload.

## Error Handling

- **File Not Found:** If a file requested for download is not found, a `404 Not Found` status is returned with an appropriate error message.
- **General Errors:** Other exceptions are caught by a global exception handler and return a `500 Internal Server Error` with a descriptive message.

## Testing

- **JUnit:** The project is structured to facilitate testing with JUnit. Write and run your tests using your IDE or Maven commands.
- **Swagger/Postman:** You can test the endpoints using Swagger (if integrated) or Postman.

## Extensibility

The application is designed with extensibility in mind. You can easily add new features or endpoints by extending the service and controller layers. The modular design (using Spring Boot) ensures that new functionalities can be integrated with minimal changes to existing code.
