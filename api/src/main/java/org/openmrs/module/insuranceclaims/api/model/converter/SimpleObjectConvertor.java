/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.insuranceclaims.api.model.converter;

import org.openmrs.module.insuranceclaims.api.model.AbstractBaseOpenmrsData;
import org.openmrs.ui.framework.SimpleObject;
/**
 *
 * @author riya
 */
public interface SimpleObjectConvertor <T extends AbstractBaseOpenmrsData> {
    
   public SimpleObject convert(T object);
    
}
