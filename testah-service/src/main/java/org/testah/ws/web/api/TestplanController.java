/**
 * 
 */
package org.testah.ws.web.api;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.testah.model.Testplan;

/**
 * @author JASON
 *
 */
@RestController
public class TestplanController {
	// Temporary data store until I wire up a db.
	private static BigInteger nextId;
	private static Map<BigInteger, Testplan> testPlansMap;

	private static Testplan save(Testplan testplan) {
		if (testPlansMap == null) {
			testPlansMap = new HashMap<>();
			nextId = BigInteger.ONE;
		}
		testplan.setId(nextId);
		testPlansMap.put(testplan.getId(), testplan);
		nextId = nextId.add(BigInteger.ONE);
		return testplan;
	}

	private static Testplan update(Testplan testplan) {
		testPlansMap.remove(testplan.getId());
		testPlansMap.put(testplan.getId(), testplan);
		return testplan;
	}

	private static Boolean delete(BigInteger id) {
		testPlansMap.remove(id);
		return true;
	}

	static {
		Testplan testplan1 = new Testplan();
		testplan1.setName("TestahTest1");

		Testplan testplan2 = new Testplan();
		testplan2.setName("TestahTest2");

		save(testplan1);
		save(testplan2);
	}

	@RequestMapping(value = "/api/testplans", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Testplan>> getTestplans() {
		Collection<Testplan> testplans = testPlansMap.values();
		return new ResponseEntity<Collection<Testplan>>(testplans, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/testplans/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Testplan> getTestplan(@PathVariable("id") BigInteger id) {
		Testplan testplan = testPlansMap.get(id);
		
		if (testplan == null) {
			return new ResponseEntity<Testplan>(testplan, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Testplan>(testplan, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/testplans/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Testplan> putTestplan(@RequestBody Testplan testplan) {
		return new ResponseEntity<Testplan>(update(testplan), HttpStatus.OK);
	}

	@RequestMapping(value = "/api/testplans", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Testplan> createTestplan(@RequestBody Testplan testplan) {
		return new ResponseEntity<Testplan>(save(testplan), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/api/testplans/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Testplan> deleteTestplan(@PathVariable("id") BigInteger id) {
		delete(id);
		return new ResponseEntity<Testplan>(HttpStatus.GONE);
	}

}
