package org.ak.crossteam.doc.backend;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.base.Throwables;
import org.ak.crossteam.doc.model.Cv;
import org.ak.crossteam.doc.utils.Loader;

import java.io.IOException;
import java.io.InputStream;

public class CvService {
    private Cv cv;

    private CvService() {
        init();
    }

    public void init() {
        // Load cv details from YAML file
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Deserialize Cv object
        try (InputStream in = Loader.getInputStream("cv.yml")) {
            this.cv = yamlMapper.readValue(in, Cv.class);
        } catch (Exception ignore){
            ignore.printStackTrace();
            Throwables.propagateIfPossible(ignore);
        }
    }

    private static final Supplier<CvService>SUPPLIER_INSTANCE = Suppliers.memoize(CvService::new);

    public static CvService get() {
        return SUPPLIER_INSTANCE.get();
    }

    public Cv getCv() throws IOException {
        return cv;
    }
}
