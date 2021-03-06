package org.formation.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;

import org.formation.controller.rest.ResultImportDto;
import org.formation.model.Produit;
import org.formation.model.ProduitRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
public class ImportProduitTest {

	@Autowired
	ImportProduitService importProduitService;
	
	@Autowired
	ProduitRepository produitRepository;

	@Test
	@WithMockUser(username = "manager", roles = "MANAGER")
	public void testSample() throws IOException, URISyntaxException {
		Path path = Paths.get(getClass().getClassLoader().getResource("sample.csv").toURI());

		Stream<String> lines = Files.lines(path);
		String csvFile = lines.collect(Collectors.joining("\n"));
		lines.close();

		ResultImportDto result = importProduitService.importLines(csvFile);

		assertEquals(5, result.countProcessed);
		assertEquals(3, result.countErrors);
		
		Produit p = produitRepository.findByReferenceAndFournisseur_Reference("TAN78","BELLE").orElseThrow(
				() -> new EntityNotFoundException() );

		assertEquals(14, p.getAvailability());
	}
	
	@Test
	@WithMockUser(username = "pmanager", roles = "PRODUCT_MANAGER")
	public void aProductManagerCannotImport() throws IOException, URISyntaxException {
		Exception exception = assertThrows(AccessDeniedException.class, () -> importProduitService.importLines(""));
		
		
		 System.out.println(exception.getMessage());
		
	}
}
