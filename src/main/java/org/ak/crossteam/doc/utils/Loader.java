package org.ak.crossteam.doc.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Loader {
    public static InputStream getInputStream(String resource) throws IOException {
        File f = new File(resource);

        if (f.exists())
            return new FileInputStream(f);

        URL url = Loader.class.getClassLoader().getResource(resource);
        if (url != null)
            return url.openStream();

        return new URL(resource).openStream();
    }
}