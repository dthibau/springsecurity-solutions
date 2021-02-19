package org.formation.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.formation.WithMockManager;
import org.formation.WithMockProductManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTest {

	@Autowired
	MockMvc mockMvc;
	
	@Test
	@WithMockProductManager
	public void productManagerCannotAccessFournisseurs() throws Exception {
		mockMvc.perform(get("/fournisseurs")).andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockManager
	public void managerCanAccessFournisseurs() throws Exception {
		mockMvc.perform(get("/fournisseurs")).andExpect(status().isOk());
	}
	
	@Test
	@WithAnonymousUser
	public void anonymousIsRedirected() throws Exception {
		mockMvc.perform(get("/fournisseurs")).andExpect(status().is3xxRedirection());
	}
	
	@Test
	@WithMockUser
	public void authenticatedCanAccessApi() throws Exception {
		mockMvc.perform(get("/api/produits")).andExpect(status().isOk());
	}
	@Test
	@WithAnonymousUser
	public void anonymousIsForbidden4Api() throws Exception {
		mockMvc.perform(get("/api/produits")).andExpect(status().is3xxRedirection());
	}
	
}
