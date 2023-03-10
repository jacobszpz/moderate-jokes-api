package ch.jsan.moderatejokesapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
public class ModerateJokesApiApplication {
	public static String submitServer = "http://localhost:3001";
	public static String deliverServer = "http://localhost:3000";

	public static void main(String[] args) {
		SpringApplication.run(ModerateJokesApiApplication.class, args);
	}

	/**
	 * Retrieve the oldest joke in the queue.
	 * @return The oldest joke retrieved from submit-jokes-api
	 */
	@CrossOrigin
	@GetMapping("/joke")
	public Joke getOldestJoke() {
		String url = submitServer + "/jokes/oldest";
		RestTemplate restTemplate = new RestTemplate();

		Joke oldest = restTemplate.getForObject(url, Joke.class);
		return oldest;
	}

	/**
	 * Accept joke and submit to deliver-jokes-api
	 */
	@CrossOrigin
	@PostMapping(path="/joke/accept", consumes="application/json", produces="application/json")
	public ResponseEntity acceptJoke(@RequestBody Joke submittedJoke) {
		RestTemplate restTemplate = new RestTemplate();
		submittedJoke.type = submittedJoke.type.toLowerCase();
		String url = deliverServer + "/jokes/new";

		// Set the headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// Create the request entity with the headers and body
		HttpEntity<Joke> requestEntity = new HttpEntity<>(submittedJoke, headers);

		// Send the request and get the response
		ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

		String message;

		// Check the response status code
		if (response.getStatusCode() == HttpStatus.OK) {
			message = "Joke posted successfully!";
		} else {
			message = "Failed to post joke.";
		}

		String deleteURL = submitServer + "/jokes/oldest";
		restTemplate.delete(deleteURL);

		return new ResponseEntity(message, HttpStatus.OK);
	}

	@CrossOrigin
	@PostMapping(path="/joke/reject", consumes="application/json", produces="application/json")
	public ResponseEntity rejectJoke() {
		RestTemplate restTemplate = new RestTemplate();
		String deleteURL = submitServer + "/jokes/oldest";
		restTemplate.delete(deleteURL);
		return new ResponseEntity("Sent delete request", HttpStatus.OK);
	}
}
