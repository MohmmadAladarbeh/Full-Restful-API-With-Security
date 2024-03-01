package com.example.restfulapisecurity.controller;


import com.example.restfulapisecurity.model.Agent;
import com.example.restfulapisecurity.service.AgentService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rest/agent")
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<Agent>> getAll() throws Exception {
        List<Agent> agentList = agentService.findAll();
        return ResponseEntity.ok(agentList);
    }

    @PostMapping (value = "/add-agent")
    public ResponseEntity <Agent> addNewAgent (@RequestBody Agent agent) throws Exception{
        Agent newAgent = agentService.save(agent);

        return ResponseEntity.ok(newAgent);
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<Agent> getAgentById (@PathVariable Long id) throws Exception{
        Agent agent = agentService.findById(id);
        return ResponseEntity.ok(agent);
    }
}