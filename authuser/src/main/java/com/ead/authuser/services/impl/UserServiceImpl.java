package com.ead.authuser.services.impl;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ead.authuser.dto.UserDto;
import com.ead.authuser.exceptions.UserConflictException;
import com.ead.authuser.exceptions.UserNotFoundException;
import com.ead.authuser.mapper.UserMapper;
import com.ead.authuser.mapper.UserMapperRegister;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserRepository;
import com.ead.authuser.services.UserService;
import com.ead.authuser.specification.UserWithEmailSpec;
import com.ead.authuser.specification.UserWithStatusSpec;
import com.ead.authuser.specification.UserWithTypeSpec;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {

	private final UserMapper userMapper;
	private final UserMapperRegister userMapperRegister;
	private final UserRepository userRepository;

	@Override
	public UserDto getOneUser(UUID userId) {
		return userMapper.toDto(findUserById(userId));
	}

	@Override
	public void deleteById(UUID userId) {
		userRepository.deleteById(findUserById(userId).getUserId());
	}

	@Override
	public UserDto updateUser(UUID userId, UserDto userDto) {
		var userModel = findUserById(userId);
		userModel.setFullName(userDto.getFullName());
		userModel.setPhoneNumber(userDto.getPhoneNumber());
		userModel.setCpf(userDto.getCpf());
		return userMapper.toDto(userRepository.save(userModel));
	}

	@Override
	public void updatePassword(UUID userId, UserDto userDto) {
		var userModel = findUserById(userId);

		if (!userDto.getOldPassword().equals(userModel.getPassword())) {
			throw new UserConflictException("Error: Mismatched old password");
		}

		userModel.setPassword(userDto.getPassword());
		userRepository.save(userModel);
	}

	@Override
	public UserDto updateImage(UUID userId, UserDto userDto) {
		var userModel = findUserById(userId);
		userModel.setImageUrl(userDto.getImageUrl());
		return userMapper.toDto(userRepository.save(userModel));
	}

	@Override
	public Page<UserDto> findAll(Pageable pageable) {
		var userModelPage = userRepository.findAll(pageable);
		return userModelPage.map(userMapper::toDto);
	}

	@Override
	public Page<UserDto> findAllByEmailAndStatusAndType(Pageable pageable, UserDto userDto) {
		var userModelPage = userRepository.findAll(new UserWithEmailSpec(userDto.getEmail())
				.and(new UserWithStatusSpec(userDto.getUserStatus())).and(new UserWithTypeSpec(userDto.getUserType())),
				pageable);

		return userModelPage.map(userMapper::toDto);
	}

	@Override
	public Page<UserDto> findAllByEmailOrStatusOrType(Pageable pageable, UserDto userDto) {
		var userModelPage = userRepository.findAll(new UserWithEmailSpec(userDto.getEmail())
				.or(new UserWithStatusSpec(userDto.getUserStatus())).or(new UserWithTypeSpec(userDto.getUserType())),
				pageable);

		return userModelPage.map(userMapper::toDto);
	}

	@Override
	public UserDto save(UserDto userDto) {
		var userModel = userMapperRegister.toModel(userDto);
		return userMapper.toDto(userRepository.save(userModel));
	}

	private UserModel findUserById(UUID userId) {
		return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
	}

}
