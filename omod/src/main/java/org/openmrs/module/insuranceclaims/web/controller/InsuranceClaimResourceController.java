package org.openmrs.module.insuranceclaims.web.controller;

import ca.uhn.fhir.context.FhirContext;
import org.openmrs.module.insuranceclaims.api.client.impl.ClaimRequestWrapper;
import org.openmrs.module.insuranceclaims.api.model.Bill;
import org.openmrs.module.insuranceclaims.api.model.InsuranceClaim;
import org.openmrs.module.insuranceclaims.api.model.InsurancePolicy;
import org.openmrs.module.insuranceclaims.api.service.InsuranceClaimService;
import org.openmrs.module.insuranceclaims.api.service.exceptions.ClaimRequestException;
import org.openmrs.module.insuranceclaims.api.service.exceptions.EligibilityRequestException;
import org.openmrs.module.insuranceclaims.api.service.request.ExternalApiRequest;
import org.openmrs.module.insuranceclaims.forms.ClaimFormService;
import org.openmrs.module.insuranceclaims.forms.NewClaimForm;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URISyntaxException;
import org.hl7.fhir.dstu3.model.Claim;
import org.hl7.fhir.exceptions.FHIRException;

import static org.openmrs.module.insuranceclaims.InsuranceClaimsOmodConstants.CLAIM_ALREADY_SENT_MESSAGE;
import static org.openmrs.module.insuranceclaims.InsuranceClaimsOmodConstants.CLAIM_NOT_SENT_MESSAGE;
import org.openmrs.module.insuranceclaims.api.model.converter.impl.BillConvertor;
import org.openmrs.module.insuranceclaims.api.model.converter.impl.InsuranceClaimConvertor;
import org.openmrs.module.insuranceclaims.api.service.fhir.FHIRInsuranceClaimService;

@RestController
@RequestMapping(value = "insuranceclaims/rest/v1")
public class InsuranceClaimResourceController {

    @Autowired
    private ClaimFormService claimFormService;

    @Autowired
    private InsuranceClaimService insuranceClaimService;

    @Autowired
    private ExternalApiRequest externalApiRequest;
    
    @Autowired
    private FHIRInsuranceClaimService fhirInsuranceClaimService;

    @RequestMapping(value = "/claims", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String createClaim(@RequestBody NewClaimForm form,
                                               HttpServletRequest request, HttpServletResponse response) throws ResponseException {
        InsuranceClaim claim = claimFormService.createClaim(form);

        response.setStatus(HttpStatus.ACCEPTED.value());
        
        return new InsuranceClaimConvertor().convert(claim).toJson();
    }

    @RequestMapping(value = "/bills", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String createBill(@RequestBody NewClaimForm form,
                                                 HttpServletRequest request, HttpServletResponse response) throws ResponseException {
        Bill bill = claimFormService.createBill(form);

        response.setStatus(HttpStatus.ACCEPTED.value());
        return new BillConvertor().convert(bill).toJson();
    }

    @RequestMapping(value = "/claims", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String get(@RequestParam(value = "claimUuid") String claimUuid,
                              HttpServletRequest request, HttpServletResponse response) throws ResponseException, FHIRException {
        InsuranceClaim claim = insuranceClaimService.getByUuid(claimUuid);
        Claim claimToSend = fhirInsuranceClaimService.generateClaim(claim);
        
        response.setStatus(HttpStatus.ACCEPTED.value());
        
        return FhirContext.forDstu3().newJsonParser().encodeResourceToString(claimToSend);
    }

    /**
     * Checks if claim is present in external id, if external id does not have information about this
     * claim it will send it to external system, if claim was already submitted it will get update object based on external
     * information.
     * @param claimUuid uuid of claim that will be send to external server
     * @return InsuranceClaim with updated values or error message that occured during processing request
     */
    @RequestMapping(value = "/claims/sendToExternal", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity sendClaimToExternalId(
            @RequestParam(value = "claimUuid", required = true) String claimUuid,
            HttpServletRequest request, HttpServletResponse response) {
        InsuranceClaim claim = insuranceClaimService.getByUuid(claimUuid);

        if (claim.getExternalId() != null) {
            return ResponseEntity.badRequest().body(CLAIM_ALREADY_SENT_MESSAGE);
        }

        try {
            externalApiRequest.sendClaimToExternalApi(claim);
            return new ResponseEntity<>(claim, HttpStatus.ACCEPTED);
        } catch (ClaimRequestException requestException) {
            return new ResponseEntity<>(requestException.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(value = "/claims/getFromExternal", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity getClaimFromExternalId(@RequestParam(value = "claimExternalCode") String claimExternalCode,
                                                 HttpServletRequest request, HttpServletResponse response) {
        ResponseEntity requestResponse;
        try {
             ClaimRequestWrapper wrapper = externalApiRequest.getClaimFromExternalApi(claimExternalCode);
             requestResponse = new ResponseEntity<>(wrapper, HttpStatus.ACCEPTED);
        } catch (URISyntaxException wrongUrl) {
             requestResponse = new ResponseEntity<>(wrongUrl.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
        return requestResponse;
    }

    @RequestMapping(value = "/getPolicyFromExternal", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity getPolicyFromExternal(@RequestParam(value = "policyNumber") String policyNumber,
                                                 HttpServletRequest request, HttpServletResponse response) {
        ResponseEntity requestResponse;
        try {
             InsurancePolicy policy = externalApiRequest.getPatientPolicy(policyNumber);
             requestResponse = new ResponseEntity<>(policy, HttpStatus.ACCEPTED);
        } catch (EligibilityRequestException wrongUrl) {
             requestResponse = new ResponseEntity<>(wrongUrl.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
        return requestResponse;
    }

    /**
     * Uses insurance claim external api to receive ClaimResponse information and then use it to
     * to update this proper values related to this insurance claim (I.e. check if was claim was valuated, check which claim
     * items were approved).
     * @param claimUuid uuid claim which have to be updated witch external server values
     * @return InsuranceClaim with updated values
     */
    @RequestMapping(value = "/claims/updateClaim", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity updateClaim(@RequestParam(value = "claimUuid") String claimUuid,
                                      HttpServletRequest request, HttpServletResponse response) {
        InsuranceClaim claim = insuranceClaimService.getByUuid(claimUuid);

        if (claim.getExternalId() == null) {
            return ResponseEntity.badRequest().body(CLAIM_NOT_SENT_MESSAGE);
        }

        ResponseEntity requestResponse;
        try {
            InsuranceClaim wrapper = externalApiRequest.updateClaim(claim);
            requestResponse =  new ResponseEntity<>(wrapper, HttpStatus.ACCEPTED);
        } catch (ClaimRequestException e) {
            requestResponse =  new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }

        return requestResponse;
    }
}
