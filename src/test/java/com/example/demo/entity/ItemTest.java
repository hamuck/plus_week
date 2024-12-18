package com.example.demo.entity;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.demo.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class ItemTest {

	@Autowired
	private ItemRepository itemRepository;

	@Test
	@Transactional
	void createItem() {
		String name = "name";
		String description = "description";
		User owner = new User();
		User manager = new User();

		Item beforeItem = new Item(name, description, owner, manager);
		Item item = itemRepository.save(beforeItem);

		assertNotNull(item.getId(),"id not null");
		assertEquals(item.getName(),name,"name is equals");
		assertEquals(item.getDescription(),description,"description is equals");
		assertEquals(item.getOwner().getId(),owner.getId(),"owner is equals");
		assertEquals(item.getManager().getId(),manager.getId(),"manager is equals");
		assertEquals(item.getStatus(),"PENDING","status is equals");
		assertNotNull(item.getStatus(),"status is null");
	}
}
