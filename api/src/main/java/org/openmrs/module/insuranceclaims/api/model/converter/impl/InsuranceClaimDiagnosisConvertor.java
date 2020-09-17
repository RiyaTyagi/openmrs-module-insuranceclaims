/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.insuranceclaims.api.model.converter.impl;

import org.openmrs.module.insuranceclaims.api.model.InsuranceClaimDiagnosis;
import org.openmrs.module.insuranceclaims.api.model.converter.SimpleObjectConvertor;
import org.openmrs.ui.framework.SimpleObject;

/**
 *
 * @author riya
 */
public class InsuranceClaimDiagnosisConvertor implements SimpleObjectConvertor<InsuranceClaimDiagnosis> {

    @Override
    public SimpleObject convert(InsuranceClaimDiagnosis object) {
        return new SimpleObject();
    }
    
}
