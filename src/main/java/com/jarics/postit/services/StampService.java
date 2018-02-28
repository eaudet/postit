package com.jarics.postit.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.jarics.postit.Note;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Arrays;
import java.util.List;


@Component
public class StampService {

    /**
     * Be sure to specify the name of your application. If the application name is {@code null} or
     * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
     */
    private static final String APPLICATION_NAME = "PostIt";

    private static final String UPLOAD_FILE_PATH = "/Users/erickaudet/dev/postit/pom.xml";
    private static final java.io.File UPLOAD_FILE = new java.io.File(UPLOAD_FILE_PATH);

    /**
     * Directory to store user credentials.
     */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".store/Jarics");
    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    /**
     * Global instance of the scopes required by this quickstart.
     * <p>
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/script-java-quickstart
     */
    private static final List<String> SCOPES = Arrays.asList("https://www.googleapis.com/auth/script.projects");
    private static FileDataStoreFactory dataStoreFactory;
    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport httpTransport;
    /**
     * Global Drive API client.
     */
    private static Drive drive;

    /**
     * Creates an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in = StampService.class.getResourceAsStream("/client_secret_479755789778-mdun3jsgmmbha6pcn7r20mil5s3ll3ks.apps.googleusercontent.com.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES).setDataStoreFactory(new FileDataStoreFactory(DATA_STORE_DIR)).setAccessType("offline").build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Uploads a file using either resumable or direct media upload.
     */
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

    /**
     * Updates the name of the uploaded file to have a "drivetest-" prefix.
     */
    private static com.google.api.services.drive.model.File updateFileWithTestSuffix(String id) throws IOException {
        com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
        fileMetadata.setName("drivetest-" + UPLOAD_FILE.getName());

        Drive.Files.Update update = drive.files().update(id, fileMetadata);
        return update.execute();
    }

    public void annotateAndStore(Note pNote) throws Exception {

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(pNote.getDirectory() + "original_upload.pdf");
            fos.write(pNote.getBytes());
        } catch (IOException e) {
            throw new Exception("You failed to upload because the file was empty.");
        } finally {
            fos.close();
            fos.flush();
        }

//
//        PDDocument wDocument = new PDDocument();
//        try {
//            wDocument.setAllSecurityToBeRemoved(true);
//            wDocument.load(new File(pNote.getDirectory() + "original_upload.pdf"));
//            PDPage page = new PDPage();
//            PDFont font = PDType1Font.HELVETICA_BOLD;
//            PDPageContentStream contents = new PDPageContentStream(wDocument, page);
//            contents.beginText();
//            contents.setFont(font, 30);
//            contents.newLineAtOffset(50, 700);
//            contents.showText(pNote.getNote());
//            contents.endText();
//            contents.close();
//            wDocument.addPage(page);
//        } finally {
//            wDocument.close();
//        }
//        return wDocument;
    }

    private void archive(PDDocument pdDocument, String dir) throws IOException {
        pdDocument.save(dir + "annotated.pdf");
    }

    public void upload() {

        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
            // authorization
            Credential credential = authorize();
            // set up the global Drive instance
            drive = new Drive.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();

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

}
