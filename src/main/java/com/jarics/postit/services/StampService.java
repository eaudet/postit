package com.jarics.postit.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import org.apache.pdfbox.multipdf.Overlay;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;


@Component
public class StampService {

    /**
     * Be sure to specify the name of your application. If the application name is {@code null} or
     * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
     */
    private static final String APPLICATION_NAME = "PostIt";

    private static final String UPLOAD_FILE_PATH = "/Users/erickaudet/dev/postit/pom.xml";
    private static final java.io.File UPLOAD_FILE = new java.io.File(UPLOAD_FILE_PATH);

    /** Directory to store user credentials. */
    private static final java.io.File DATA_STORE_DIR =
            new java.io.File(System.getProperty("user.home"), ".store/Jarics");

    private static FileDataStoreFactory dataStoreFactory;

    /** Global instance of the HTTP transport. */
    private static HttpTransport httpTransport;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /** Global Drive API client. */
    private static Drive drive;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/script-java-quickstart
     */
    private static final List<String> SCOPES =
            Arrays.asList("https://www.googleapis.com/auth/script.projects");


    /**
     * Add a note on a pdf and sends it to a destination folder.
     * @param pNotes
     * @param src
     * @param dest
     * @throws IOException
     */
    public void manipulatePdf(String pNotes, String src, String dest) throws IOException {
        //Loading an existing document
        File file = new File("/Users/erickaudet/dev/postit/src/main/resources/original.PDF");
        PDDocument originalFile = new PDDocument();
        originalFile.setAllSecurityToBeRemoved(true);
        originalFile.load(file);

        createOverlay("Hello from hell");

    }

    private PDDocument createOverlay(String message) throws IOException {
            String filename = "/Users/erickaudet/dev/postit/src/main/resources/overlay.pdf";

            PDDocument doc = new PDDocument();
            try {
                PDPage page = new PDPage();
                doc.addPage(page);

                PDFont font = PDType1Font.HELVETICA_BOLD;

                PDPageContentStream contents = new PDPageContentStream(doc, page);
                contents.beginText();
                contents.setFont(font, 30);
                contents.newLineAtOffset(50, 700);
                contents.showText(message);
                contents.endText();
                contents.close();

                doc.save(filename);

            }
            finally {
                doc.close();
            }
            return doc;
    }

    private void uploadLocal(){

    }

    public void upload() {

        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
            // authorization
            Credential credential = authorize();
            // set up the global Drive instance
            drive = new Drive.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(
                    APPLICATION_NAME).build();

//            // run commands
//
//            View.header1("Starting Resumable Media Upload");
//            File uploadedFile = uploadFile(false);
//
//            View.header1("Updating Uploaded File Name");
//            File updatedFile = updateFileWithTestSuffix(uploadedFile.getId());
//
//            View.header1("Starting Resumable Media Download");
//            downloadFile(false, updatedFile);
//
//            View.header1("Starting Simple Media Upload");
//            uploadedFile = uploadFile(true);
//
//            View.header1("Starting Simple Media Download");
//            downloadFile(true, uploadedFile);
//
//            View.header1("Success!");
//            return;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
                StampService.class.getResourceAsStream("/client_secret_479755789778-mdun3jsgmmbha6pcn7r20mil5s3ll3ks.apps.googleusercontent.com.json");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(new FileDataStoreFactory(DATA_STORE_DIR))
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /** Uploads a file using either resumable or direct media upload. */
    private static com.google.api.services.drive.model.File uploadFile(boolean useDirectUpload) throws IOException {
        com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
        fileMetadata.setName("test");

        FileContent mediaContent = new FileContent("image/jpeg", UPLOAD_FILE);

        Drive.Files.Create insert = drive.files().create(fileMetadata, mediaContent);
        MediaHttpUploader uploader = insert.getMediaHttpUploader();
        uploader.setDirectUploadEnabled(useDirectUpload);
        uploader.setProgressListener(new FileUploadProgressListener());
        return insert.execute();
    }

    /** Updates the name of the uploaded file to have a "drivetest-" prefix. */
    private static com.google.api.services.drive.model.File updateFileWithTestSuffix(String id) throws IOException {
        com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
        fileMetadata.setName("drivetest-" + UPLOAD_FILE.getName());

        Drive.Files.Update update = drive.files().update(id, fileMetadata);
        return update.execute();
    }

}
