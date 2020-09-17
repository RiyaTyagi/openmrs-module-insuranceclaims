/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.insuranceclaims.api.model.converter.impl;

import org.openmrs.module.insuranceclaims.api.model.Bill;
import org.openmrs.module.insuranceclaims.api.model.converter.SimpleObjectConvertor;
import org.openmrs.ui.framework.SimpleObject;


public class BillConvertor implements SimpleObjectConvertor<Bill> {

    @Override
    public SimpleObject convert(Bill bill) {
        SimpleObject billObject =  new SimpleObject();
        billObject.put("uuid", bill.getUuid());
        billObject.put("startDate", bill.getStartDate());
        billObject.put("endDate", bill.getEndDate());
        billObject.put("patient", bill.getPatient());
        billObject.put("paymentStatus", bill.getPaymentStatus().name());
        billObject.put("totalAmount", bill.getTotalAmount());
        billObject.put("patient", CommonObjectConvertor.getPatientObject(bill.getPatient()));
        billObject.put("paymentType", bill.getPaymentType().name());
        return billObject;
    }
    
}
