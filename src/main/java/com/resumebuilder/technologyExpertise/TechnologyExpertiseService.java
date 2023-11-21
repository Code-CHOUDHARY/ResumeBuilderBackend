package com.resumebuilder.technologyExpertise;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resumebuilder.DTO.TechnologyExpertiseDto;
import com.resumebuilder.exception.TechnologyException;
import com.resumebuilder.exception.UserNotFoundException;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class TechnologyExpertiseService {
	private final TechnologyExpertiseRepository technologyExpertiseRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public TechnologyExpertiseService(TechnologyExpertiseRepository technologyExpertiseRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.technologyExpertiseRepository = technologyExpertiseRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Adds technology expertise for the authenticated user.
     *
     * @param principal    The Principal object representing the authenticated user.
     * @param expertiseDto The DTO containing technology expertise information.
     * @return The added technology expertise as DTO.
     * @throws UserNotFoundException If the authenticated user is not found.
     */
    
    public TechnologyExpertiseDto addTechnologyExpertise(Principal principal, TechnologyExpertiseDto expertiseDto) {
        String username = principal.getName();
        Optional<User> optionalUser = userRepository.findByEmail(principal.getName());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Map DTO to entity
            TechnologyExpertise technologyExpertise = modelMapper.map(expertiseDto, TechnologyExpertise.class);

            // Set the user for the technology expertise
            technologyExpertise.setUser(user);
            technologyExpertise.setModified_by(user.getUser_id());

            // Save the technology expertise
            TechnologyExpertise savedTechnologyExpertise = technologyExpertiseRepository.save(technologyExpertise);

            // Map the saved entity back to DTO and return
            return modelMapper.map(savedTechnologyExpertise, TechnologyExpertiseDto.class);
        } else {
            throw new UserNotFoundException("User not found with username: " + username);
        }
    }
    
    
    /**
     * Fetches technology expertise for a user based on user ID.
     *
     * @param userId The ID of the user for whom to fetch technology expertise.
     * @return List of technology expertise as DTOs.
     * @throws UserNotFoundException If the user with the given ID is not found.
     */
    public List<TechnologyExpertiseDto> getTechnologyExpertiseByUserId(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<TechnologyExpertise> technologyExpertiseList = technologyExpertiseRepository.findByUserId(user.getUser_id());

            // Map the entities to DTOs
            return technologyExpertiseList.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } else {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
    }

    private TechnologyExpertiseDto convertToDto(TechnologyExpertise technologyExpertise) {
        return modelMapper.map(technologyExpertise, TechnologyExpertiseDto.class);
    }
}
