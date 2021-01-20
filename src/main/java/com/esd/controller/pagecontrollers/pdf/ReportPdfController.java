package com.esd.controller.pagecontrollers.pdf;

import com.esd.controller.annotations.Authentication;
import com.esd.model.data.UserGroup;
import com.esd.model.service.UserDetailsService;
import com.esd.model.service.reports.SystemOverViewReportService;
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
import org.joda.time.LocalDate;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@WebServlet("/admin/reports/pdf")
@Authentication(userGroups = {UserGroup.ADMIN})
public class ReportPdfController extends HttpServlet {

    private UserDetailsService userDetailsService = UserDetailsService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            LocalDate startDate = LocalDate.parse(request.getParameter("startDate"));
            LocalDate endDate = LocalDate.parse(request.getParameter("endDate"));
            HashMap<String, String> reportValMap = SystemOverViewReportService.getInstance().getReportData(startDate, endDate);

            response.setContentType("application/pdf");
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(response.getOutputStream()));

            Document doc = new Document(pdfDoc);
            //set margin
            doc.setMargins(40, 40, 40, 40);

            //get logo
            URL logoPath = ReportPdfController.class.getResource("/images/logo.png");
            Image logo = new Image(ImageDataFactory.create(logoPath));
            logo.setHorizontalAlignment(HorizontalAlignment.RIGHT);
            doc.add(logo);

            doc.add(new Paragraph("SMARTWARE SYSTEM REPORTS\n \n" +
                    "Report Start Date: " + startDate + "\n"+
                    "Report End Date: " + endDate + "\n \n"
            ).setTextAlignment(TextAlignment.LEFT));

            //create table
            float[] columnWidths = {240f, 240f};
            Table table = new Table(columnWidths);

            Iterator reportValIterator = reportValMap.entrySet().iterator();
            while(reportValIterator.hasNext()){
                Map.Entry vals = (Map.Entry)reportValIterator.next();
                table.addCell(new Cell().add(vals.getKey().toString()));
                table.addCell(new Cell().add(vals.getValue().toString()));
                reportValIterator.remove();
            }

            doc.add(table);
            doc.close();

        } catch (Exception e) {
            e.printStackTrace();
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(request.getHeader("referer"));
            request.setAttribute("message", "error could not generate pdf");
            requestDispatcher.forward(request, response);
        }
    }
}