// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

// [START drive_quickstart]
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

/* class to demonstarte use of Drive files list API */
public class Drive {
    /** Application name. */
    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    /** Directory to store authorization tokens for this application. */
    private static final String TOKENS_DIRECTORY_PATH = "resources";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "credentials.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = Drive.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("499177653486-th25ahgh4e11nbt4abbsio42mqvbjj07.apps.googleusercontent.com");
        //returns an authorized Credential object.
        return credential;
    }

    public static void main(String... args) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        com.google.api.services.drive.Drive service = new com.google.api.services.drive.Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        FileList result = service.files().list()
                .setQ("name contains 'imagenesBot' and mimeType = 'application/vnd.google-apps.folder'")
                .setPageSize(100)
                .setSpaces("drive")
                .setFields("nextPageToken, files(id, name)")
                .execute();
        List<File> ficheros = result.getFiles();

        if (ficheros == null || ficheros.isEmpty()) {
            System.out.println("No files found.");
        } else {
            String dirImagenes = null;
            System.out.println("Files:");
            for (File file : ficheros) {
                System.out.printf("%s (%s)\n", file.getName(), file.getId());
                dirImagenes = file.getId();
            }
            // busco la imagen en el directorio
            FileList resultImagenes = service.files().list()
                    .setQ("name contains 'kirbyCono' and parents in '" + dirImagenes + "'")
                    .setSpaces("drive")
                    .setFields("nextPageToken, files(id, name)")
                    .execute();
            List<File> filesImagenes = resultImagenes.getFiles();
            for (File fichero : filesImagenes) {
                System.out.printf("Imagen: %s\n", fichero.getName());
                // guardamos el 'stream' en el fichero aux.jpeg tiene que existir
                OutputStream outputStream = new FileOutputStream("D:/david/Documents/imagenesB/your_aux.jpeg");
                service.files().get(fichero.getId())
                        .executeMediaAndDownloadTo(outputStream);
                outputStream.flush();
                outputStream.close();
            }
        }

        FileList resultado = service.files().list()
                .setQ("name contains 'imagenesBot' and mimeType = 'application/vnd.google-apps.folder'")
                .setPageSize(100)
                .setSpaces("drive")
                .setFields("nextPageToken, files(id, name)")
                .execute();
        List<File> files = result.getFiles();
        if (files == null || files.size() == 0) {
            System.out.println("No files found.");
        } else {
            String dirPDF = null;
            System.out.println("ficheros:");
            for (File file : files) {
                System.out.printf("%s (%s)\n", file.getName(), file.getId());
                dirPDF = file.getId();
            }
            FileList resultadoFichero = service.files().list()
                    .setQ("name contains 'ExamenCOD' and parents in '" + dirPDF + "'")
                    .setSpaces("drive")
                    .setFields("nextPageToken, files(id, name)")
                    .execute();
            List<File> ficheroDoc = resultadoFichero.getFiles();
            for (File file : ficheroDoc) {
                System.out.printf("Documento: %s\n", file.getName());
                // guardamos el 'stream' en el fichero aux.jpeg tiene que existir
                String fileID = file.getId();
                OutputStream outputStream = new FileOutputStream("D:/david/Documents/imagenesB/examenPDF.pdf");
                service.files().export(fileID,"application/pdf")
                        .executeMediaAndDownloadTo(outputStream);
                outputStream.flush();
                outputStream.close();
            }
        }
    }
}
// [END drive_quickstart]
