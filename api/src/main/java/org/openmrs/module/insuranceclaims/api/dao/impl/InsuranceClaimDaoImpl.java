package org.openmrs.module.insuranceclaims.api.dao.impl;

import org.openmrs.module.insuranceclaims.api.dao.BaseOpenmrsDataDao;
import org.openmrs.module.insuranceclaims.api.dao.InsuranceClaimDao;
import org.openmrs.module.insuranceclaims.api.model.InsuranceClaim;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class InsuranceClaimDaoImpl extends BaseOpenmrsDataDao<InsuranceClaim> implements InsuranceClaimDao {

    public InsuranceClaimDaoImpl() {
        super(InsuranceClaim.class);
    }

    @Override
    public List<InsuranceClaim> getAllInsuranceClaims(Integer patientId) {
        Criteria crit = createCriteria();
        crit.createCriteria("patient")
                .add(Restrictions.eq("patientId", patientId));
        return findAllByCriteria(crit, false);
    }

    @Override
    public InsuranceClaim getByUuid(String uuid) {
        Criteria crit = sessionFactory.getCurrentSession().createCriteria(mappedClass);
        InsuranceClaim claim = (InsuranceClaim) crit.add(Restrictions.eq("uuid", uuid)).uniqueResult(); 
        return claim;
    }

}
