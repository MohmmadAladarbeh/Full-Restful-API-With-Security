package com.example.restfulapisecurity.service;

import com.example.restfulapisecurity.model.Agent;
import com.example.restfulapisecurity.repository.AgentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentService {
    private final AgentRepository agentRepository;

    public AgentService(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    public Agent findById (Long agentId) throws Exception {
        try {
            Agent agent = agentRepository.findById(agentId).orElse(null);

            if (agent != null) {
                return agent;
            }
            throw new Exception("Agent with '" + agentId + "' Id Not Found" );
        }catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Agent with '" + agentId + "' Id Not Found" );
        }
    }

    public List<Agent> findAll () throws Exception {
        try {
            List<Agent> agentList = agentRepository.findAll();
            if (agentList != null) {
                return agentList;
            }
            throw new Exception("No Data Found");
        }catch (Exception ex)  {
            throw new Exception("No Data Found while get all agents");
        }
    }

    public Agent save(Agent agent) throws Exception {
        try {
           Agent newAgent  = agentRepository.save(agent);
           if (agent != null) {
               return agent;
           }
           throw new Exception("Error While saving the agent");
        }catch (Exception ex) {
            throw new Exception("Error while saving the agent");
        }
    }
}
