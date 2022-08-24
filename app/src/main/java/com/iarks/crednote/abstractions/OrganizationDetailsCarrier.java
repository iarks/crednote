package com.iarks.crednote.abstractions;

import com.iarks.crednote.Exceptions.FormNotSavedException;
import com.iarks.crednote.models.Organisation;

public interface OrganizationDetailsCarrier {
    Organisation getOrganisationDetails() throws FormNotSavedException;
}
