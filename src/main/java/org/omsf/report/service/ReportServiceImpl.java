package org.omsf.report.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.omsf.report.dao.ReportRepository;
import org.omsf.report.model.Report;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
* @packageName    : org.omsf.report.service
* @fileName       : ReportServiceImpl.java
* @author         : leeyunbin
* @date           : 2024.06.18
* @description    :
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2024.06.18        leeyunbin       최초 생성
*/

@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService{
	private final ReportRepository reportRepository;
	
	@Override
	public void insertReport(Report report) {
		reportRepository.insertReport(report);
	}
	
	@Override
	public Map<Integer, List<Report>> getReportsGroupedByStoreNo() {
		List<Report> reports = reportRepository.getReports();
		
		Map<Integer, List<Report>> groupedReports = reports.stream()
			    .collect(Collectors.groupingBy(Report::getStoreNo));
		
		return groupedReports;
	}

	@Override
	public void deleteReport(int reportNo) {
		reportRepository.deleteReport(reportNo);
	}

}
