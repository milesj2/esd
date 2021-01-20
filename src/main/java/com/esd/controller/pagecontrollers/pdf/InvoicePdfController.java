package com.esd.controller.pagecontrollers.pdf;

import com.esd.controller.annotations.Authentication;
import com.esd.controller.utils.AuthenticationUtils;
import com.esd.controller.utils.UrlUtils;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.*;
import com.esd.model.service.InvoiceService;
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

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;

@WebServlet("/invoices/pdf")
@Authentication(userGroups = {UserGroup.ALL})
public class InvoicePdfController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            //get invoice
            Invoice invoice = InvoiceService.getInstance().getInvoiceById(Integer.parseInt(request.getParameter("selectedInvoiceId")));
            UserDetails userDetails = UserDetailsService.getInstance().getUserDetailsByUserID(invoice.getPatientId());

            if(UserGroup.patients.contains(AuthenticationUtils.getCurrentUserGroup(request))){
                if(userDetails.getId() != invoice.getPatientId()){
                    response.sendRedirect(UrlUtils.error(request, HttpServletResponse.SC_FORBIDDEN));
                    return;
                }
            }

            double sum = 0.00;

            response.setContentType("application/pdf");
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(response.getOutputStream()));

            Document doc = new Document(pdfDoc);
            //set margin
            doc.setMargins(60, 40, 60, 40);

            //get logo
            URL logoPath = InvoicePdfController.class.getResource("/images/logo.png");
            Image logo = new Image(ImageDataFactory.create(logoPath));
            logo.setHorizontalAlignment(HorizontalAlignment.RIGHT);
            doc.add(logo);

            //add content
            doc.add(new Paragraph("SMARTWARE\n \n" +
                            "To:\n" + userDetails.getFirstName()+ " "+
                            userDetails.getLastName()+"\n"+
                            userDetails.getAddressLine1()+"\n"+
                            userDetails.getTown()+"\n"+
                            userDetails.getPostCode()+"\n \n"
                            ).setTextAlignment(TextAlignment.LEFT));

            doc.add(new Paragraph("INVOICE")).setTextAlignment(TextAlignment.RIGHT);

            //create table
            float[] columnWidths = {120f, 120f, 120f, 120f};
            Table table = new Table(columnWidths);
            //first column
            table.addCell(new Cell().add("Item"));
            table.addCell(new Cell().add("Item Description"));
            table.addCell(new Cell().add("Item Quantity"));
            table.addCell(new Cell().add("Item Cost"));

            //list invoice items
            for(InvoiceItem item: invoice.getItems()){
                table.addCell(new Cell().add(Integer.toString(invoice.getId())));
                table.addCell(new Cell().add(item.getDescription()));
                table.addCell(new Cell().add(Integer.toString(item.getQuantity())));
                table.addCell(new Cell().add(Double.toString(item.getCost())));
                sum += item.getCost() * item.getQuantity();
            }

            table.addCell(new Cell().add(""));
            table.addCell(new Cell().add(""));
            table.addCell(new Cell().add("Total Due:"));
            table.addCell(new Cell().add(Double.toString(sum)));

            table.addCell(new Cell().add(""));
            table.addCell(new Cell().add(""));
            table.addCell(new Cell().add("Status: "));
            table.addCell(new Cell().add(invoice.getInvoiceStatus().toString()));

            doc.add(table);

            doc.add(new Paragraph("\nPlease Make Payable to Smart Care Health Services")
                    ).setTextAlignment(TextAlignment.LEFT);
            doc.close();

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(UrlUtils.error(request, response.SC_INTERNAL_SERVER_ERROR));
        }
    }
}
