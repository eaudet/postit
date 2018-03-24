package com.jarics.postit.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@Component
public class StampService {

    /**
     * Be sure to specify the name of your application. If the application name is {@code null} or
     * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
     */
    private static final String APPLICATION_NAME = "PostIt";

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
        InputStream in = StampService.class.getResourceAsStream("/client_secret.json");
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
    private static com.google.api.services.drive.model.File uploadFile(boolean useDirectUpload, File pFile) throws IOException {
//        File fileMetadata = new File();
//        fileMetadata.setName("photo.jpg");
//        java.io.File filePath = new java.io.File("files/photo.jpg");
//        FileContent mediaContent = new FileContent("image/jpeg", filePath);
//        File file = driveService.files().create(fileMetadata, mediaContent)
//                .setFields("id")
//                .execute();
//        System.out.println("File ID: " + file.getId());
        return null;
    }

    public String mergeAndStore(String pNote, String pDirectory, MultipartFile pFileA, MultipartFile pFileB) throws Exception {

        //TODO handle all in streaming no files on disk....this is not scallable.
        //TODO why ReactJs is sending a file much bigger bo

        UUID wUuid = UUID.randomUUID();
        String wUploadFileAName = wUuid + "_" + pFileA.getOriginalFilename();
        String wUploadFileBName = wUuid + "_" + pFileB.getOriginalFilename();
        String wNoteFileName = wUuid + "_note.pdf";
        String wMergedFileName = wUuid + "_merged.pdf";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(wUploadFileAName);
            fos.write(pFileA.getBytes());
            fos.close();
            fos = new FileOutputStream(wUploadFileBName);
            fos.write(pFileB.getBytes());
            fos.close();
            // create note pdf
            // Create a document and add a page to it
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);
            PDFont font = PDType1Font.HELVETICA_BOLD;
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.moveTextPositionByAmount(100, 700);
            contentStream.drawString(pNote);
            contentStream.endText();
            contentStream.close();
            document.save(wNoteFileName);
            document.close();
            // merge
            List<InputStream> locations = new ArrayList<>();
            locations.add(new FileInputStream(wNoteFileName));
            locations.add(new FileInputStream(wUploadFileAName));
            locations.add(new FileInputStream(wUploadFileBName));
            PDFMergerUtility PDFmerger = new PDFMergerUtility();
            OutputStream out = new FileOutputStream(wMergedFileName);
            PDFmerger.addSources(locations);
            PDFmerger.setDestinationStream(out);
            PDFmerger.mergeDocuments();
            System.out.println("Documents merged");
            //delete intermediate files
            File wFile = new File(wNoteFileName);
            wFile.delete();
            wFile = new File(wUploadFileAName);
            wFile.delete();
            wFile = new File(wUploadFileBName);
            wFile.delete();
            //store in destination
            uploadFile(true, new File(wMergedFileName));

        } catch (IOException e) {
            throw new Exception("You failed to upload because the file was empty.");
        } finally {
            return "Done";
        }
    }

    public String annotateAndStore(String pNote, String pDirectory, MultipartFile pFile) throws Exception {

        //TODO handle all in streaming no files on disk....this is not scallable.
        //TODO why ReactJs is sending a file much bigger bo

        UUID wUuid = UUID.randomUUID();
        String wUploadFileName = wUuid + "_" + pFile.getOriginalFilename();
        String wNoteFileName = wUuid + "_note.pdf";
        String wMergedFileName = wUuid + "_merged.pdf";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(wUploadFileName);
            fos.write(pFile.getBytes());
            fos.close();
            // create note pdf
            // Create a document and add a page to it
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);
            PDFont font = PDType1Font.HELVETICA_BOLD;
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.moveTextPositionByAmount(100, 700);
            contentStream.drawString(pNote);
            contentStream.endText();
            contentStream.close();
            document.save(wNoteFileName);
            document.close();
            // merge
            List<InputStream> locations = new ArrayList<>();
            locations.add(new FileInputStream(wNoteFileName));
            locations.add(new FileInputStream(wUploadFileName));
            PDFMergerUtility PDFmerger = new PDFMergerUtility();
            OutputStream out = new FileOutputStream(wMergedFileName);
            PDFmerger.addSources(locations);
            PDFmerger.setDestinationStream(out);
            PDFmerger.mergeDocuments();
            System.out.println("Documents merged");
            //delete intermediate files
            File wFile = new File(wNoteFileName);
            wFile.delete();
            wFile = new File(wUploadFileName);
            wFile.delete();
            //store in destination
            uploadFile(true, new File(wMergedFileName));

        } catch (IOException e) {
            throw new Exception("You failed to upload because the file was empty.");
        } finally {
            return "Done";
        }
    }


}
