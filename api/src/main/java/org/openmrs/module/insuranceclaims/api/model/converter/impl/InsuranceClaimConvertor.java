/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.insuranceclaims.api.model.converter.impl;

import org.openmrs.module.insuranceclaims.api.model.InsuranceClaim;
import org.openmrs.module.insuranceclaims.api.model.converter.SimpleObjectConvertor;
import org.openmrs.ui.framework.SimpleObject;

/**
 *
 * @author riya
 */
public class InsuranceClaimConvertor implements SimpleObjectConvertor<InsuranceClaim> {

    @Override
    public SimpleObject convert(InsuranceClaim claim) {
        SimpleObject claimObj = new SimpleObject();
        claimObj.put("uuid", claim.getUuid());
        claimObj.put("claimCode", claim.getClaimCode());
        claimObj.put("dateFrom", claim.getDateFrom());
        claimObj.put("dateTo", claim.getDateTo());
        claimObj.put("externalId", claim.getExternalId());
        claimObj.put("guaranteeId", claim.getGuaranteeId());
        claimObj.put("location", CommonObjectConvertor.getLocationObject(claim.getLocation()));
        claimObj.put("adjustment", claim.getAdjustment());
        claimObj.put("totalApproved", claim.getApprovedTotal());
        claimObj.put("bill", new BillConvertor().convert(claim.getBill()));
        claimObj.put("totalClaimed", claim.getClaimedTotal());
        claimObj.put("dateProcessed", claim.getDateProcessed());
        claimObj.put("explaination", claim.getExplanation());
        claimObj.put("status", claim.getStatus().name());
        claimObj.put("rejectionReason", claim.getRejectionReason());
        claimObj.put("visit", claim.getVisitType().getId());
        claimObj.put("provider", CommonObjectConvertor.getProviderObject(claim.getProvider()));
        claimObj.put("patient", CommonObjectConvertor.getPatientObject(claim.getPatient()));
        return claimObj;
    }
    
    
}
