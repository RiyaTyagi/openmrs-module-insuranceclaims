/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.insuranceclaims.api.model.converter.impl;

import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.PersonAddress;
import org.openmrs.PersonName;
import org.openmrs.Provider;
import org.openmrs.Visit;
import org.openmrs.ui.framework.SimpleObject;

/**
 *
 * @author riya
 */
public class CommonObjectConvertor {

    public static SimpleObject getPatientObject(Patient patient) {
        if (patient != null) {
            SimpleObject patientObj = new SimpleObject();
            patientObj.put("uuid", patient.getUuid());
            patientObj.put("display", patient.getPersonName().getFullName());
            patientObj.put("person", getPersonObject(patient.getPerson()));
            patientObj.put("voided", patient.getVoided());
            return patientObj;
        }
        return null;

    }

    public static SimpleObject getLocationObject(Location location) {
        if (location != null) {
            SimpleObject locationObj = new SimpleObject();
            locationObj.put("uuid", location.getUuid());
            locationObj.put("display", location.getDisplayString());

            return locationObj;
        }
        return null;
    }

    public static SimpleObject getVisitObject(Visit visit) {
        if (visit != null) {
            SimpleObject visitObj = new SimpleObject();
            visitObj.put("uuid", visit.getUuid());
            visitObj.put("startDate", visit.getStartDatetime());
            visitObj.put("stopDate", visit.getStopDatetime());
            visitObj.put("type", visit.getVisitType());
            return visitObj;
        }
        return null;
    }

    public static SimpleObject getProviderObject(Provider provider) {
        if (provider != null) {
            SimpleObject providerObj = new SimpleObject();
            providerObj.put("uuid", provider.getUuid());
            providerObj.put("display", provider.getDescription());
            providerObj.put("identifier", provider.getIdentifier());
            providerObj.put("retired", provider.isRetired());
            providerObj.put("person", getPersonObject(provider.getPerson()));

            return providerObj;
        }
        return null;
    }

    public static SimpleObject getPersonObject(Person person) {
        if (person != null) {
            SimpleObject personObj = new SimpleObject();
            personObj.put("uuid", person.getUuid());
            personObj.put("display", person.getPersonName().getFullName());
            personObj.put("gender", person.getGender());
            personObj.put("age", person.getAge());
            personObj.put("birthDate", person.getBirthdate());
            personObj.put("birthtime", person.getBirthtime());
            personObj.put("birthdateEstimated", person.isBirthdateEstimated());
            personObj.put("dead", person.isDead());
            personObj.put("deathDate", person.getDeathDate());
            personObj.put("causeOfDeath", person.getCauseOfDeath());
            personObj.put("voided", person.isVoided());
            personObj.put("deathdateEstimated", person.getDeathdateEstimated());
            personObj.put("preferredName", getPreferredNameObject(person.getPersonName()));
            personObj.put("preferredAddress", getPreferredAddressObject(person.getPersonAddress()));
            return personObj;
        }
        return null;
    }

    public static SimpleObject getPreferredNameObject(PersonName name) {

        if (name != null) {
            SimpleObject nameObj = new SimpleObject();
            nameObj.put("uuid", name.getUuid());
            nameObj.put("display", name.getFullName());
            return nameObj;
        }
        return null;
    }

    public static SimpleObject getPreferredAddressObject(PersonAddress address) {
        if (address != null) {
            SimpleObject addressObj = new SimpleObject();
            addressObj.put("uuid", address.getUuid());
            addressObj.put("display", address.toString());
            return addressObj;
        }
        return null;
    }
}
