package com.eduonix.votingsystem.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eduonix.votingsystem.entity.Candidate;
import com.eduonix.votingsystem.entity.Citizen;
import com.eduonix.votingsystem.repositories.CandidateRepo;
import com.eduonix.votingsystem.repositories.CitizenRepo;

@Controller
public class VotingController {
	
	public final Logger logger = Logger.getLogger(VotingController.class);
	
	@Autowired
	CitizenRepo citizenRepo;
	@Autowired
	CandidateRepo candidateRepo;
	
	@RequestMapping("/")
	public String goToVote() {
		logger.info("Returning vote.html file");
		return "vote.html";
	}
	
	@RequestMapping("/doLogin")
	public String doLogin(@RequestParam String name, Model model, HttpSession session) {
		
		
		Citizen citizen = citizenRepo.findByName(name);
		if (citizen == null) {
			return "notIn.html";
		}
		session.setAttribute("citizen", citizen);
		logger.info("getting citizen form database");
		
		if(!citizen.isHasVoted()) {
			
			List<Candidate> candidates = candidateRepo.findAll();
			model.addAttribute("candidates", candidates);
			return "/performVoted.html";
		}
		else {
			return "/alreadyVoted.html";
		}
	}
	@RequestMapping("voteFor")
	public String voteFor(@RequestParam Long id, HttpSession session) {
		
		Citizen ct = (Citizen) session.getAttribute("citizen");
		if(!ct.isHasVoted()) {
		ct.setHasVoted(true);
		Candidate c = candidateRepo.findById((long)id);
		c.setNumberOfVotes(c.getNumberOfVotes()+ 1);
		candidateRepo.save(c);
		citizenRepo.save(ct);
		return "/voted.html";
		}
		return "/arleadyVoted.html";
		
		
	}
	

}
