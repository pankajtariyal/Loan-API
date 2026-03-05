package com.loandemo.Loan.API.uitls;

import com.loandemo.Loan.API.status.DocumentType;
import jdk.internal.dynalink.linker.LinkerServices;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class Common {
    public static final List<DocumentType> documentTypeList = new ArrayList<>();

    {
        documentTypeList.add(DocumentType.PAN);
        documentTypeList.add(DocumentType.SALARY_SLIP);
        documentTypeList.add(DocumentType.ID_PROOF);
    }
}
