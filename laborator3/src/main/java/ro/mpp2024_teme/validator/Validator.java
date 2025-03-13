package ro.mpp2024_teme.validator;

import ro.mpp2024_teme.utils.exceptions.ValidationException;

public interface Validator {

    void validate(Object entity) throws ValidationException;
}
