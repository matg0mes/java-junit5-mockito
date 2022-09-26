package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";
    private static final String REDIRECT_OWNER_5 = "redirect:/owners/5";

    @Mock
    OwnerService ownerService;

    @InjectMocks
    OwnerController ownerController;

    @Mock
    BindingResult bindingResult;

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    @Test
    void processCreationFormHasErrors() {
        // given
        Owner owner = new Owner(1L, "John", "Bob");
        given(bindingResult.hasErrors()).willReturn(true);

        // when
        String result = ownerController.processCreationForm(owner, bindingResult);

        // then
        assertThat(result).isEqualToIgnoringCase(VIEWS_OWNER_CREATE_OR_UPDATE_FORM);
    }

    @Test
    void processCreationFormNotErrors() {
        // given
        Owner owner = new Owner(5L, "John", "Bob");
        given(bindingResult.hasErrors()).willReturn(false);
        given(ownerService.save(any(Owner.class))).willReturn(owner);

        // when
        String result = ownerController.processCreationForm(owner, bindingResult);

        // then
        assertThat(result).isEqualToIgnoringCase(REDIRECT_OWNER_5);
    }

    @Test
    void processFindFormHasErrors() {
        // given
        Owner owner = new Owner(1L, "Joe", "Buck");
        List<Owner> owners = new ArrayList<>();

        given(ownerService.findAllByLastNameLike(stringArgumentCaptor.capture())).willReturn(owners);

        // when
        ownerController.processFindForm(owner, bindingResult, null);

        // then
        assertThat("%Buck%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
    }
}