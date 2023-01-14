package com.dentist.app.shared;

import com.dentist.app.service.response.AppointmentResponse;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class CSVHelper {
    public static ByteArrayInputStream appointmentsToCSV(List<AppointmentResponse> appointmentResponses) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
            List<String> headers = Arrays.asList(
                    "Date",
                    "Time",
                    "Patient Name",
                    "Doctor Name",
                    "Status"
            );
            csvPrinter.printRecord(headers);
            for (AppointmentResponse appointmentResponse : appointmentResponses) {
                List<String> data = Arrays.asList(
                        appointmentResponse.getDate().format(DateTimeFormatter.ISO_DATE),
                        String.valueOf(appointmentResponse.getTime()),
                        appointmentResponse.getPatient().getPatientName(),
                        appointmentResponse.getDoctor().getDoctorName(),
                        String.valueOf(appointmentResponse.getStatus())
                );
                csvPrinter.printRecord(data);
            }
            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }
}
