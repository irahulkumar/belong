package com.belong.util;

import com.belong.model.NumberV1;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CSVLoader {
    public static List<NumberV1> getAllNumbers(String filePath) throws Exception{
        final String DELIMITER = ",";
        List<NumberV1> numberV1List = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String row;

            while ((row = br.readLine()) != null) {
                String[] values = row.split(DELIMITER);
                numberV1List.add(createNumberV1ByValue(values));
            }
        } catch (IOException ioe) {
            log.error(ioe.getMessage());
            throw  ioe;
        }
        return numberV1List;
    }

    private static NumberV1 createNumberV1ByValue(String... values) {
        NumberV1 numberV1 = new NumberV1();
        numberV1.setNumber(values[0]);
        numberV1.setCustomer(values[1]);
        numberV1.setIsActive(values[2]);
        return numberV1;
    }
}
