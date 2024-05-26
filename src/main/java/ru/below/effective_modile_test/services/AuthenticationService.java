package ru.below.effective_modile_test.services;

import ru.below.effective_modile_test.dto.AuthenticationRequest;
import ru.below.effective_modile_test.dto.AuthenticationResponse;
import ru.below.effective_modile_test.dto.RegisterRequest;
import ru.below.effective_modile_test.models.Account;
import ru.below.effective_modile_test.models.Email;
import ru.below.effective_modile_test.models.Phone;

public interface AuthenticationService {
    Account convertAccountDTOToAccount(RegisterRequest registerRequest);

    Email convertEmailDTOToEmail(RegisterRequest registerRequest);

    Phone convertPhoneDTOToPhone(RegisterRequest registerRequest);

    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
