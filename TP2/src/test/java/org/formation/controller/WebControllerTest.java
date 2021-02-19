package org.formation.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.formation.controller.mvc.FournisseurController;
import org.formation.jwt.TokenProvider;
import org.formation.model.FournisseurRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = FournisseurController.class)
public class WebControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	TokenProvider tokenProvider;
	
	@MockBean
	UserDetailsService userDetailService;
	
	@MockBean
	FournisseurRepository fournisseurRepository;
	
	@MockBean
	ClientRegistrationRepository clientRegistrationRepository;
	
	
	@Test
	@WithMockUser(username = "manager", roles = "MANAGER")
	public void testAccessFournisseur() throws Exception {
		
		mockMvc.perform(get("/fournisseurs")).andExpect(status().isOk());
		
	}
}
