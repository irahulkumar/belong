package com.belong.util;

import com.belong.model.NumberV1;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CSVLoaderTest {

    public static String FILE_PATH = "src/main/resources/Numbers.csv";
    public static String INVALID_FILE_PATH = "abc";

    @Test
    @DisplayName("CSVLoader reads all records")
    void loadSuccess() throws Exception {
        List<NumberV1> numberV1List = CSVLoader.getAllNumbers(FILE_PATH);
        assertEquals(6, numberV1List.size());
    }

    @Test
    @DisplayName("Invalid filename throws exception")
    void loadFailure() {
        Throwable exception = assertThrows(FileNotFoundException.class, () -> CSVLoader.getAllNumbers(INVALID_FILE_PATH));
        String expected = "The system cannot find the file specified";
        String actual = exception.getMessage();
        assertTrue(actual.contains(expected));
    }
}
