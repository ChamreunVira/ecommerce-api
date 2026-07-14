package com.kh.vira_dev.ecommerceapi.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendOtp(String to, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true , "UTF-8");
            helper.setTo(to);
            helper.setSubject("Your OTP code");
            helper.setText(mailContentBuilder(otp) , true);
            mailSender.send(message);
        }catch (MessagingException ex) {
            log.info(ex.getLocalizedMessage());
            throw new RuntimeException(ex.getLocalizedMessage());
        }
    }

    private String mailContentBuilder(String otp) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>OTP Verification</title>
                </head>
                <body style="margin:0; padding:0; background-color:#fff7ed; font-family: Arial, sans-serif;">
                
                    <table width="100%" cellpadding="0" cellspacing="0" style="padding: 40px 0;">
                        <tr>
                            <td align="center">
                                <table width="500" cellpadding="0" cellspacing="0"\s
                                       style="background:#ffffff; border-radius:10px; padding:40px; box-shadow:0 2px 8px rgba(0,0,0,0.1); border-top:5px solid #ff8c00;">
                
                                    <tr>
                                        <td align="center">
                                            <h2 style="margin:0; color:#ff8c00;">Email Verification</h2>
                                        </td>
                                    </tr>
                
                                    <tr>
                                        <td style="padding-top:20px; color:#555; font-size:16px; line-height:1.6;">
                                            Hello,
                                            <br><br>
                                            Use the following One-Time Password (OTP) to verify your email address:
                                        </td>
                                    </tr>
                
                                    <tr>
                                        <td align="center" style="padding:30px 0;">
                                            <div style="
                                                display:inline-block;
                                                padding:15px 30px;
                                                font-size:32px;
                                                font-weight:bold;
                                                letter-spacing:8px;
                                                color:#ffffff;
                                                background:#ff8c00;
                                                border-radius:8px;">
                                                {{otp}}
                                            </div>
                                        </td>
                                    </tr>
                
                                    <tr>
                                        <td style="color:#666; font-size:14px; line-height:1.6;">
                                            This code will expire in <strong style="color:#ff8c00;">5 minutes</strong>.
                                            <br><br>
                                            If you did not request this code, please ignore this email.
                                        </td>
                                    </tr>
                
                                    <tr>
                                        <td style="padding-top:30px; color:#999; font-size:12px;" align="center">
                                            © 2026 vSt4re. All rights reserved.
                                        </td>
                                    </tr>
                
                                </table>
                            </td>
                        </tr>
                    </table>
                
                </body>
                </html> 
               """.replace("{{otp}}" , otp);
    }

}
