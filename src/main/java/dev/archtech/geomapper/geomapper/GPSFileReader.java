package dev.archtech.geomapper.geomapper;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GPSFileReader implements AutoCloseable{
    CSVReader csvReader;
    FileReader reader;

    public GPSFileReader(File file) throws FileNotFoundException {
        this.reader = new FileReader(file);
        this.csvReader = new CSVReader(reader);
    }

    public void skip(int linesToSkip) throws IOException {
        this.csvReader.skip(linesToSkip);
    }

    public String[] peek() throws IOException {
        return this.csvReader.peek();
    }

    public String[] readNext() throws CsvValidationException, IOException {
        return this.csvReader.readNext();
    }

    @Override
    public void close() throws Exception {
        this.csvReader.close();
        this.reader.close();
    }
}
