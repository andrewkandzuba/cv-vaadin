package org.ak.crossteam.doc.backend;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.ak.crossteam.doc.model.Cv;
import org.ak.crossteam.doc.model.Job;
import org.ak.crossteam.doc.utils.DateFormats;
import org.ak.crossteam.doc.utils.Loader;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.InputStream;

public class TestYamlParser {

    @Test
    @Parameters({"resource-file"})
    public void testParseJobs(@Optional("src/main/resources/cv.yml") String resourceFile) throws Exception {
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Cv cv;
        try (InputStream in = Loader.getInputStream(resourceFile)) {
            cv = yamlMapper.readValue(in, Cv.class);
        }

        Assert.assertNotNull(cv);
        Assert.assertNotNull(cv.getJobs());
        Assert.assertTrue(cv.getJobs().size() > 1);

        Job job = cv.getJobs().get(0);

        Assert.assertEquals(job.getTitle(), "Senior Solution Architect");
        Assert.assertEquals(job.getCompany(), "Playtech");
        Assert.assertEquals(job.getLocation(), "Tartu, Estonia");
        Assert.assertEquals(DateFormats.simple.format(job.getStartDate()), "2013-07-01");
        Assert.assertEquals(DateFormats.simple.format(job.getEndDate()), "2015-11-01");
        Assert.assertTrue(job.getSummary().startsWith("Oversee, analyze and design"));
    }
}
