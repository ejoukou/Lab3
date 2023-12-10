package org.example.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.example.Histograms.CreatedClosed;
import org.example.Histograms.Date;
import org.example.Histograms.Category;
import org.example.Histograms.Histogram;
import org.example.model.Issue;
import org.example.model.IssueList;
public class IssueAnalyzerService {
    private final IssueService issueService;

    public IssueAnalyzerService(String domain) {
        this.issueService = new IssueService(domain);
    }

    public void drawHistogramTimeSpent(String chartTitle, String project,
                                       String status, Integer maxResults) throws IOException {
        IssueList issueList = issueService.getIssuesForProject(project, status, maxResults);
        Map<Integer, Integer> timeAndIssues = transformData(issueList);

        Date hist = new Date(chartTitle, timeAndIssues);
        hist.draw();
    }

    public void drawHistogramIssueReporter(String chartTitle, String reporter, String project,
                                           String status, Integer maxResults)
            throws IOException {
        IssueList issueList = issueService.getIssuesForProjectAndReporter(reporter, project, status, maxResults);
        Map<Integer, Integer> topUsers = transformData(issueList);

        Date hist = new Date(chartTitle, topUsers);
        hist.draw();
    }

    public void drawHistogramTopUsers(Integer quantityUsers, String chartTitle, String project,
                                      String status, Integer maxResults)
            throws JsonProcessingException {
        IssueList issueList = issueService.getIssuesForProject(project, status, maxResults);
        Map<String, Integer> topUsers = transformDataTopUsers(issueList, quantityUsers);

        Category hist = new Category(chartTitle, topUsers);
        hist.draw();
    }

    public void drawHistogramPriority(String chartTitle, String project,
                                      List<String> priorities, Integer maxResults)
            throws JsonProcessingException {
        Map<String, Integer> issuePriority = new TreeMap<>();
        for (String priority: priorities) {
            IssueList issueList = issueService.getIssuesForProjectAndPriority(priority, project, maxResults);
            issuePriority.put(priority, issueList.getTotal());
        }

        Category hist = new Category(chartTitle, issuePriority);
        hist.draw();
    }

    public void drawHistogramForDaysIssue(String startDate, String endDate, String chartTitle, String project,
                                          Integer maxResult) throws JsonProcessingException {
        IssueList issueListCreated = issueService.getCreatedIssue(startDate, endDate, project, maxResult);
        IssueList issueListClosed = issueService.getClosedIssue(startDate, endDate, project, maxResult);
        String date;

        Map<String, Integer> issueCreated = new TreeMap<>();
        for (Issue issue: issueListCreated.getIssues()) {
            date = dateToLocalDate(issue.getFields().getCreated()).toString();
            if (issueCreated.containsKey(date)) {
                issueCreated.put(date, issueCreated.get(date) + 1);
            } else {
                issueCreated.put(date, 1);
            }
        }

        Map<String, Integer> issueClosed = new TreeMap<>();
        for (Issue issue: issueListClosed.getIssues()) {
            date = dateToLocalDate(issue.getFields().getResolutiondate()).toString();
            if (issueClosed.containsKey(date)) {
                issueClosed.put(date, issueCreated.get(date) + 1);
            } else {
                issueClosed.put(date, 1);
            }
        }

        Map<String, Integer> issueAccumulated = new TreeMap<>();
        Integer accum = 0;
        for (String key: issueCreated.keySet()) {
            Integer quantityCreated = issueCreated.get(key);
            if (issueClosed.containsKey(key)) {
                accum = accum + quantityCreated - issueClosed.get(key);
            } else {
                accum += quantityCreated;
            }
            if (accum < 0) {
                accum = 0;
            }
            issueAccumulated.put(key, accum);
        }

        Histogram hist = new CreatedClosed(chartTitle, issueCreated, issueClosed, issueAccumulated);
        hist.draw();
    }

    private LocalDate dateToLocalDate(java.util.Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private Map<String, Integer> transformDataTopUsers(IssueList issueList, Integer quantityUsers) {
        Map<String, Integer> result = new TreeMap<>();
        for (Issue issue: issueList.getIssues()) {
            if (result.size() > quantityUsers)
                break;
            String reporterName = issue.getFields().getReporter().getName();
            if (result.containsKey(reporterName)) {
                result.put(reporterName, result.get(reporterName) + 1);
            } else {
                result.put(reporterName, 1);
            }
        }
        return result;
    }

    private Map<Integer, Integer> transformData(IssueList issueList) {
        final long MILLI_TO_HOUR = 1000 * 60 * 60;

        Map<Integer, Integer> result = new TreeMap<>();
        for (Issue issue: issueList.getIssues()) {
            java.util.Date created = issue.getFields().getCreated();
            java.util.Date resolutiondate = issue.getFields().getResolutiondate();
            Integer diffDateInHour = (int) ((resolutiondate.getTime() - created.getTime()) / MILLI_TO_HOUR );
            if (diffDateInHour < 0)
                throw new RuntimeException();
            if (result.containsKey(diffDateInHour)) {
                result.put(diffDateInHour, result.get(diffDateInHour) + 1);
            } else {
                result.put(diffDateInHour, 1);
            }
        }
        return result;
    }
}
