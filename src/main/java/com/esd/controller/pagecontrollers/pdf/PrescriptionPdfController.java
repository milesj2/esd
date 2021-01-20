package com.esd.controller.pagecontrollers.pdf;

import com.esd.model.dao.DaoConsts;
import com.esd.model.data.persisted.Prescription;
import com.esd.model.data.persisted.UserDetails;
import com.esd.model.service.PrescriptionService;
import com.esd.model.service.UserDetailsService;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/prescriptions/pdf")
public class PrescriptionPdfController extends HttpServlet {

    private UserDetailsService userDetailsService = UserDetailsService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            //get invoice
            Prescription prescription = PrescriptionService.getInstance().getPrescriptionById(Integer.parseInt(request.getParameter(DaoConsts.ID)));
            UserDetails userDetails = userDetailsService.getUserDetailsByUserID(prescription.getPatientId());
            UserDetails employee = userDetailsService.getUserDetailsByID(prescription.getEmployeeId());

            response.setContentType("application/pdf");
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(response.getOutputStream()));

            Document doc = new Document(pdfDoc);
            //set margin
            doc.setMargins(60, 40, 60, 40);

            //get logo
            //Image logo = new Image(ImageDataFactory.create(getServletContext().getContextPath()+"/res/images/logo.png"));
            Image logo = new Image(ImageDataFactory.create("C:\\Users\\Trent Meier\\Desktop\\Enterprise Systems Development\\" +
                    "Current\\SmartWare\\src\\main\\webapp\\res\\images\\logo.png"));
            logo.setHorizontalAlignment(HorizontalAlignment.RIGHT);
            doc.add(logo);

            //add content
            doc.add(new Paragraph("SMARTWARE\n \n" +
                    "Perscription For:" + userDetails.getFirstName()+" "+
                    userDetails.getLastName()+"\n"+
                    userDetails.getAddressLine1()+"\n"+
                    userDetails.getTown()+"\n"+
                    userDetails.getPostCode()+"\n \n"
            ).setTextAlignment(TextAlignment.LEFT));

            doc.add(new Paragraph("INVOICE")).setTextAlignment(TextAlignment.RIGHT);

            //create table
            float[] columnWidths = {240f, 240f};
            Table table = new Table(columnWidths);
            //first column
            table.addCell(new Cell().add("Date"));
            table.addCell(new Cell().add(prescription.getIssueDate().toString()));
            table.addCell(new Cell().add("Prescription Issuing Doctor"));
            table.addCell(new Cell().add(employee.getFirstName()+ " "+employee.getLastName()));
            table.addCell(new Cell().add("Prescription Details"));
            table.addCell(new Cell().add(prescription.getPrescriptionDetails()));
            table.addCell(new Cell().add("Prescription  Issuing Doctor"));
            table.addCell(new Cell().add(employee.getFirstName()+ " "+employee.getLastName()));
            table.addCell(new Cell().add("Originating Date"));
            table.addCell(new Cell().add(prescription.getOriginatingPrescriptionId().toString()));

            table.setHorizontalAlignment(HorizontalAlignment.CENTER);
            doc.add(table);

            doc.add(new Paragraph("\nPlease Submit to SMARTCARE Pharmacy for Prescription")
            ).setTextAlignment(TextAlignment.LEFT);
            doc.close();

        } catch (Exception e) {
            e.printStackTrace();
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(request.getHeader("referer"));
            request.setAttribute("message", "error could not generate pdf");
            requestDispatcher.forward(request, response);
        }
    }
}
