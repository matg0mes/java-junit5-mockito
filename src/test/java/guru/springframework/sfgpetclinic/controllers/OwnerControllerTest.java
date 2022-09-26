package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.fauxspring.Model;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";
    private static final String REDIRECT_OWNER_5 = "redirect:/owners/5";

    @Mock
    OwnerService ownerService;

    @Mock
    Model model;

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

    @Nested
    @DisplayName("processFindForm method")
    class ProcessFindForm {
        @BeforeEach
        void setUp() {
            given(ownerService.findAllByLastNameLike(stringArgumentCaptor.capture())).willAnswer(invocation -> {
                List<Owner> owners = new ArrayList<>();
                String name = invocation.getArgument(0);

                if (name.equals("%Buck%")) {
                    owners.add(new Owner(1L, "Joe", "Buck"));
                    return owners;
                } else if (name.equals("%DontFindMe%")) {
                    return owners;
                } else if (name.equals("%FindMe%")) {
                    owners.add(new Owner(1L, "Joe", "Buck"));
                    owners.add(new Owner(2L, "Joe2", "Buck2"));
                    return owners;
                }

                throw new RuntimeException("Invalid Argument");
            });
        }

        @Test
        void processFindFormWildCardsFindMe() {
            // given
            Owner owner = new Owner(1L, "Joe", "FindMe");
            InOrder inOrder = inOrder(ownerService, model);
            // when
            String viewName = ownerController.processFindForm(owner, bindingResult, model);

            // then
            assertThat("%FindMe%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
            assertThat("owners/ownersList").isEqualToIgnoringCase(viewName);

            //inOrder assets
            inOrder.verify(ownerService).findAllByLastNameLike(anyString());
            inOrder.verify(model).addAttribute(anyString(), anyList());
            verifyNoMoreInteractions(model);
        }

        @Test
        void processFindFormWildCardsStringAnnotation() {
            // given
            Owner owner = new Owner(1L, "Joe", "Buck");

            // when
            String viewName = ownerController.processFindForm(owner, bindingResult, null);

            // then
            assertThat("%Buck%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
            assertThat("redirect:/owners/1").isEqualToIgnoringCase(viewName);
            verifyZeroInteractions(model);
        }

        @Test
        void processFindFormWildCardsNotFound() {
            // given
            Owner owner = new Owner(1L, "Joe", "DontFindMe");

            // when
            String viewName = ownerController.processFindForm(owner, bindingResult, null);

            // then
            assertThat("%DontFindMe%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
            assertThat("owners/findOwners").isEqualToIgnoringCase(viewName);
            verifyZeroInteractions(model);
        }
    }
}
