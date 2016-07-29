package org.testah.ui.mvc;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.testah.ui.model.Testplan;


@Controller
@RequestMapping("/")
public class TestahController {
	
	public ModelAndView list() {
		Map<BigInteger, Testplan> testplanMap = new ConcurrentHashMap<>();
		Testplan testplan1 = new Testplan();
		testplan1.setId(BigInteger.ONE);
		testplan1.setName("testing");
		testplanMap.put(BigInteger.ONE, testplan1);
		
		Iterable<Testplan> testplans = testplanMap.values();
		return new ModelAndView("testplans/list", "testplans", testplans);
	}
}
