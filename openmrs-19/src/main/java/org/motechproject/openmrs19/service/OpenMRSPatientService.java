package org.motechproject.openmrs19.service;

import org.motechproject.openmrs19.domain.OpenMRSConcept;
import org.motechproject.openmrs19.domain.OpenMRSPatient;
import org.motechproject.openmrs19.exception.PatientNotFoundException;

import java.util.Date;
import java.util.List;

/**
 * Interface for handling patients on the OpenMRS server.
 */
public interface OpenMRSPatientService {

    /**
     * Creates the given {@code patient} on the OpenMRS server.
     *
     * @param patient  the patient to be created
     * @return  the created patient
     */
    OpenMRSPatient createPatient(OpenMRSPatient patient);

    /**
     * Updates the patient with the  given {@code currentMotechId} with the information stored in the given
     * {@code patient} (including the new MOTECH ID passed in the given {@code patient}).
     *
     * @param patient  the patient to be used as an update source
     * @param currentMotechId  the current MOTECH ID of the patient to update (used for searching)
     * @return the updated patient
     */
    OpenMRSPatient updatePatient(OpenMRSPatient patient, String currentMotechId);

    /**
     * Updates the patient with the information stored in the given {@code patient}.
     *
     * @param patient  the patient to be used as an update source
     * @return the updated patient
     */
    OpenMRSPatient updatePatient(OpenMRSPatient patient);

    /**
     * Returns the patient with the given {@code uuid}.
     *
     * @param uuid  the UUID of the patient
     * @return the patient with the given UUID, null if the patient doesn't exist
     */
    OpenMRSPatient getPatientByUuid(String uuid);

    /**
     * Returns the patient with the given {@code motechId}.
     *
     * @param motechId  the MOTECH ID of the patient
     * @return the patient with the given MOTECH ID, null if the patient doesn't exist
     */
    OpenMRSPatient getPatientByMotechId(String motechId);

    /**
     * If the {@code motechId} is null this method will return a list of patients with given {@code name}, else it will
     * return a list with a single patient that has the given {@code name} and {@code motechId}. If there are no
     * matching patients an empty list will be returned.
     *
     * @param name  the name of the patient to be searched for
     * @param motechId  the MOTECH ID of the patient to be searched for
     * @return list of matched patients
     */
    List<OpenMRSPatient> search(String name, String motechId);

    /**
     * Marks a patient with the given {@code motechId} as dead with the given {@code dateOfDeath}, {@code causeOfDeath}
     * and a {@code comment}.
     *
     * @param motechId  the MOTECH ID of the patient
     * @param causeOfDeath  the cause of death
     * @param dateOfDeath  the date of death
     * @param comment  the additional information for the cause of death
     * @throws PatientNotFoundException if the patient with the given MOTECH ID doesn't exist
     */
    void deceasePatient(String motechId, OpenMRSConcept causeOfDeath, Date dateOfDeath, String comment) throws PatientNotFoundException;

    /**
     * Deletes the patient with the given {@code uuid}.
     *
     * @param uuid  the UUID of the patient
     * @throws PatientNotFoundException if the patient with the given UUID doesn't exist
     */
    void deletePatient(String uuid) throws PatientNotFoundException;
}
