package org.omsf.report.service;

import java.util.List;
import java.util.Map;

import org.omsf.report.dao.LogStoreRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

//leejongseop
@Service
@RequiredArgsConstructor
public class LogStoreServiceImpl implements LogStoreService {

	private final LogStoreRepository logStoreRepository;
	
	@Override
	public List<Map<String, Object>> getLogListByStoreNo(int storeNo) {
		return logStoreRepository.getLogListByStoreNo(storeNo);
	}

	@Override
	public void updateStore(int logId) {
		logStoreRepository.updateStore(logId);
	}

}