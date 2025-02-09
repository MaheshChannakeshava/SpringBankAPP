package com.mahi.Banking_Demo.service;

import com.mahi.Banking_Demo.dto.EmailDetails;

public interface EmailService {

    void sendEmailALerts(EmailDetails emailDetails);
    void sendEmailWithAttachments(EmailDetails emailDetails);
}
