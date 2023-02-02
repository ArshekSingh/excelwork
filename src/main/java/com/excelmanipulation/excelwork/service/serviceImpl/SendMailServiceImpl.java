package com.excelmanipulation.excelwork.service.serviceImpl;

import com.excelmanipulation.excelwork.assembler.ExcelAssembler;
import com.excelmanipulation.excelwork.entity.Customers;
import com.excelmanipulation.excelwork.repository.CustomerRepo;
import com.excelmanipulation.excelwork.response.Response;
import com.excelmanipulation.excelwork.service.SendMailService;
import com.excelmanipulation.excelwork.utils.ExcelGeneratorUtil;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

import static com.excelmanipulation.excelwork.utils.Constants.EXCEL_HEADERS;


@Service
@Slf4j
public class SendMailServiceImpl implements SendMailService {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ExcelGeneratorUtil excelGeneratorUtil;

    @Autowired
    private ExcelAssembler excelAssembler;

    @Value("${spring.recipient.email}")
    private String recipient;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public Response sendEmailWithAttachment() {
        List<Customers> customerList = customerRepo.findAll();
        if(!CollectionUtils.isEmpty(customerList)) {
            log.info("Initiating process");
            try {
                byte[] bytes;
                try (HSSFWorkbook workbook = new HSSFWorkbook()) {
                    Map<String, Object> map = excelGeneratorUtil.populateHeaderAndName(EXCEL_HEADERS, "customers.xls");
                    map.put("RESULTS", excelAssembler.prepareCollectionDetailData(customerList));
                    excelGeneratorUtil.buildExcelDocument(map, workbook);
                    bytes = excelGeneratorUtil.downloadDocument(workbook);
                }
                return sendMail(bytes);
            } catch (Exception exception) {
                log.error("Exception occurs while downloading Excel {}", exception.getMessage());
            }
        }
            return new Response("No records found", HttpStatus.BAD_REQUEST);
    }

    public Response sendMail(byte[] bytes) {
            try{
                String to = recipient;
                String subject = "Total Collections of TCR for loanId " ;
                String body = "Please find the attachment for collections of Toffee Coffee Roaster for loanId " + "\n" + "Total Collections : Rs " ;
                return sendMailWithAttachment(to, subject, body, "Customers", bytes);
            }
            catch (Exception exception) {
                    log.error("Exception occurred due to {}", exception.getMessage());
                    return new Response(exception.getMessage(), HttpStatus.BAD_REQUEST);
            }
    }


    public Response sendMailWithAttachment(String to, String subject, String body, String fileName, byte[] ba) {
        MimeMessagePreparator preparator = mimeMessage -> {

            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setFrom(new InternetAddress(sender));
            mimeMessage.setSubject(subject);

            Multipart multiPart = new MimeMultipart();

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);

            if (ba != null && ba.length > 0) {
                DataSource fds = new ByteArrayDataSource(ba, "application/octet-stream");
                MimeBodyPart attachment = new MimeBodyPart();
                attachment.setDataHandler(new DataHandler(fds));
                attachment.setDisposition(Part.ATTACHMENT);
                attachment.setFileName(fileName + ".xls");
                multiPart.addBodyPart(messageBodyPart);
                multiPart.addBodyPart(attachment);

                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
                messageHelper.addAttachment(fileName + ".xls", fds);
            }
            mimeMessage.setContent(multiPart);
        };
        try {
            mailSender.send(preparator);
            return new Response("Mail sent successfully", HttpStatus.OK);
        } catch (MailException ex) {
            log.error(ex.getMessage());
            return new Response(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
