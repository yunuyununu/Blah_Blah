package com.example.blah.mapper;

import java.util.List;

import com.example.blah.domain.AlarmDTO;

public interface AlarmMapper {

	List<AlarmDTO> alarmList(int a_u_idx);
	
	void isRead(int a_u_idx);
	
	// void deleteAlarm(int a_idx);
}
