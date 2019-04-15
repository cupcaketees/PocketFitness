package uk.ac.tees.cupcake.maps;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class DownloadURL {

    private static final String TAG = "DownloadURL";

    /**
     * Retrieves the JSON information through a connection retrieving all the json data from the URL to put in a string to be returned;
     *
     * @param URL - The URL to get the JSON Data
     * @return - Returns the JSON Data
     */
    String ReadURL(String URL) throws IOException {
        String info = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;

        try {
            URL url = new URL(URL);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuffer = new StringBuilder();

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            info = stringBuffer.toString();
            bufferedReader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        Log.d(TAG, "ReadURL: " + info);

        return info;
    }
}
