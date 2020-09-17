/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.insuranceclaims.api.model.converter.impl;

import org.openmrs.module.insuranceclaims.api.model.InsurancePolicy;
import org.openmrs.module.insuranceclaims.api.model.converter.SimpleObjectConvertor;
import org.openmrs.ui.framework.SimpleObject;


/**
 *
 * @author riya
 */
public class InsurancePolicyConvertor implements SimpleObjectConvertor<InsurancePolicy> {

    @Override
    public SimpleObject convert(InsurancePolicy object) {
        return new SimpleObject();
    }
    
}
