package no.ntnu.idatt2106.krisefikser.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.ntnu.idatt2106.krisefikser.config.TestSecurityConfig;
import no.ntnu.idatt2106.krisefikser.dto.UserUpdateDTO;
import no.ntnu.idatt2106.krisefikser.dto.UserResponseDTO;
import no.ntnu.idatt2106.krisefikser.model.Household;
import no.ntnu.idatt2106.krisefikser.model.User;
import no.ntnu.idatt2106.krisefikser.security.JwtAuthFilter;
import no.ntnu.idatt2106.krisefikser.security.JwtUtil;
import no.ntnu.idatt2106.krisefikser.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper mapper;

	@MockitoBean
	UserService userService;

	@MockitoBean
	PasswordEncoder passwordEncoder;

	@MockitoBean
	JwtUtil jwtUtil;

	@MockitoBean
	JwtAuthFilter jwtAuthFilter;

	@Nested
	@DisplayName("GET /api/users/me")
	class GetUserInfoTests {
		@Test
		@WithMockUser(username = "leo123")
		@DisplayName("returns 200 and the user info when found")
		void getUserInfo_found() throws Exception {
			User testUser = new User();
			testUser.setId(42);
			testUser.setUsername("leo123");
			testUser.setEmail("leo@gmail.com");
			testUser.setFirstName("Leo");
			testUser.setLastName("Mordi");

			Household hh = new Household();
			hh.setId(99);
			hh.setName("Leos lekeland");
			hh.setMemberCount(2);
			testUser.setHousehold(hh);
			when(userService.getUserByUsername("leo123")).thenReturn(Optional.of(testUser));

			mvc.perform(get("/api/users/me"))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.id").value(42))
					.andExpect(jsonPath("$.username").value("leo123"))
					.andExpect(jsonPath("$.email").value("leo@gmail.com"))
					.andExpect(jsonPath("$.firstName").value("Leo"))
					.andExpect(jsonPath("$.lastName").value("Mordi"))
					.andExpect(jsonPath("$.householdId").value(99))
					.andExpect(jsonPath("$.householdName").value("Leos lekeland"));
		}

		@Test
		@WithMockUser(username = "leo123")
		@DisplayName("returns 404 when not found")
		void getUserInfo_notFound() throws Exception {
			when(userService.getUserByUsername("leo123")).thenReturn(Optional.empty());

			mvc.perform(get("/api/users/me"))
					.andExpect(status().isNotFound());
		}
	}

	@Nested
	@DisplayName("DELETE /api/users/{id}")
	class DeleteUserTests {
		@Test
		@WithMockUser
		@DisplayName("returns 204 when user exists and is deleted")
		void deleteUser_success() throws Exception {
			User user = new User();
			user.setId(7);
			when(userService.getUserById(7)).thenReturn(Optional.of(user));

			mvc.perform(delete("/api/users/7")).andExpect(status().isNoContent());

			verify(userService).deleteById(7);
		}

		@Test
		@WithMockUser
		@DisplayName("returns 404 when user does not exist")
		void deleteUser_notFound() throws Exception {
			when(userService.getUserById(7)).thenReturn(Optional.empty());

			mvc.perform(delete("/api/users/7")).andExpect(status().isNotFound());

			verify(userService, never()).deleteById(anyInt());
		}
	}

	@Nested
	@DisplayName("PUT /api/users")
	class UpdateUserTests {
		@Test
		@WithMockUser(username = "Charles321")
		@DisplayName("returns 200 + updated user when no conflicts")
		void updateUser_success() throws Exception {
			// 1) current logged‑in user
			User existing = new User();
			existing.setId(99);
			existing.setUsername("Charles321");
			existing.setEmail("old@mail.com");

			when(userService.getUserByUsername("Charles321")).thenReturn(Optional.of(existing));
			// 2) no email/username conflicts
			when(userService.emailExists("new@mail.com")).thenReturn(false);
			when(userService.usernameExists("newCharles654")).thenReturn(false);

			// 3) stub updateUser → DTO
			UserResponseDTO dto = new UserResponseDTO();
			dto.setId(99);
			dto.setUsername("newCharles654");
			dto.setEmail("new@mail.com");
			dto.setFirstName("Charles");
			dto.setLastName("Leclerc");
			when(userService.updateUser(eq(99), any(UserUpdateDTO.class))).thenReturn(dto);

			// 4) build request payload
			UserUpdateDTO req = new UserUpdateDTO();
			req.setUsername("newCharles654");
			req.setEmail("new@mail.com");
			req.setFirstName("Charles");
			req.setLastName("Leclerc");

			mvc.perform(put("/api/users")
					.contentType(MediaType.APPLICATION_JSON)
					.content(mapper.writeValueAsString(req)))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.id").value(99))
					.andExpect(jsonPath("$.username").value("newCharles654"))
					.andExpect(jsonPath("$.email").value("new@mail.com"));
		}

		@Test
		@WithMockUser(username = "charlie")
		@DisplayName("returns 409 when email already taken")
		void updateUser_emailConflict() throws Exception {
			User existing = new User();
			existing.setId(99);
			existing.setUsername("charlie");
			existing.setEmail("old@example.com");
			when(userService.getUserByUsername("charlie"))
					.thenReturn(Optional.of(existing));
			when(userService.emailExists("dup@example.com")).thenReturn(true);

			UserUpdateDTO req = new UserUpdateDTO();
			req.setEmail("dup@example.com");

			mvc.perform(put("/api/users")
					.contentType(MediaType.APPLICATION_JSON)
					.content(mapper.writeValueAsString(req)))
					.andExpect(status().isConflict());
		}

		@Test
		@WithMockUser(username = "charlie")
		@DisplayName("returns 409 when username already taken")
		void updateUser_usernameConflict() throws Exception {
			User existing = new User();
			existing.setId(99);
			existing.setUsername("charlie");
			existing.setEmail("old@example.com");
			when(userService.getUserByUsername("charlie"))
					.thenReturn(Optional.of(existing));
			when(userService.emailExists("new@example.com")).thenReturn(false);
			when(userService.usernameExists("alice")).thenReturn(true);

			UserUpdateDTO req = new UserUpdateDTO();
			req.setEmail("new@example.com");
			req.setUsername("alice");

			mvc.perform(put("/api/users")
					.contentType(MediaType.APPLICATION_JSON)
					.content(mapper.writeValueAsString(req)))
					.andExpect(status().isConflict());
		}

		@Test
		@WithMockUser(username = "charlie")
		@DisplayName("returns 404 when logged in user not found")
		void updateUser_notFound() throws Exception {
			when(userService.getUserByUsername("charlie")).thenReturn(Optional.empty());

			UserUpdateDTO req = new UserUpdateDTO();
			req.setEmail("whatever@example.com");

			mvc.perform(put("/api/users")
					.contentType(MediaType.APPLICATION_JSON)
					.content(mapper.writeValueAsString(req)))
					.andExpect(status().isNotFound());
		}
	}
}
