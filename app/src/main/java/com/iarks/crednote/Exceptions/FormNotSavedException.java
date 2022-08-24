package com.iarks.crednote.Exceptions;

public class FormNotSavedException extends Exception {
    public FormNotSavedException(String please_save_organization_form) {
        super(please_save_organization_form);
    }
}
