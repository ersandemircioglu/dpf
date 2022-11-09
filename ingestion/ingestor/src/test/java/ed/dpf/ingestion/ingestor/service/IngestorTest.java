package ed.dpf.ingestion.ingestor.service;

import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ed.dpf.ingestion.ingestor.model.IngestorConfiguration;

class IngestorTest {

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    void setUp() throws Exception {
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    void test() {
        IngestorConfiguration configuration = new IngestorConfiguration();
        configuration.setPattern("(?<instrument>\\w+)_(?<satellite>\\w+)_(?<station>\\w+)_(?<productionTime>\\d{4}\\d{2}\\d{2}_\\d{2}\\d{2}\\d{2})_(?<orbit>\\d{5})_(?<product>\\w+)_(?<level>\\w+).(?<extension>\\w+)");

        Ingestor ingestor = new Ingestor(configuration);
        Map<String, Object> output = ingestor.parse("INST01_SAT01_STA01_20221231_001234_12345_MAP_L1.txt");
        output.entrySet().forEach(s -> System.out.println(s.getKey()+"="+s.getValue()));
    }

}
