package com.malam.payroll.devometer.parser;

import com.malam.payroll.devometer.model.ProfileModel;
import com.malam.payroll.devometer.types.ProgrammingLanguage;
import com.malam.payroll.devometer.types.Seniority;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RepositoryLogParser {

    public static final String CSV_DATE_FORMAT = "EEE MMM dd HH:mm:ss yyyy";

    public List<ProfileModel> parse(String path) {
        List<CommitModel> commits = new ArrayList<>();

        Reader in;
        try {
            in = new FileReader(path);
            CSVFormat format = CSVFormat.Builder.create().setIgnoreEmptyLines(true).setNullString("").setQuote('\"').build();
            Iterable<CSVRecord> records = format.parse(in);
            for (CSVRecord record : records) {
                String commit = record.isSet(0) ? record.get(0) : null;
                String author = record.isSet(1) ? record.get(1) : null;
                String date = record.isSet(2) ? record.get(2) : null;
                String message = record.isSet(3) ? record.get(3) : null;
                String changedFiles = record.isSet(4) ? record.get(4) : null;

                CommitModel commitModel = new CommitModel(commit, author, date, message, changedFiles);
                commits.add(commitModel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, List<CommitModel>> authorCommitsMap = commits.stream()
                .filter(commitModel -> commitModel.author() != null && commitModel.date() != null && commitModel.changedFiles() != null)
                .filter(commitModel -> !commitModel.author().isEmpty() && !commitModel.date().isEmpty() && !commitModel.changedFiles().isEmpty())
                .collect(Collectors.groupingBy(CommitModel::author));
        Set<String> allAuthors = authorCommitsMap.keySet();
        List<RepositoryAuthorStatsModel> repositoryAuthorStatsModels = new ArrayList<>(authorCommitsMap.size());
        for (Map.Entry<String, List<CommitModel>> entry : authorCommitsMap.entrySet()) {
            try {
                RepositoryAuthorStatsModel authorStatsModel = buildRepositoryAuthorStats(entry, allAuthors);
                if (authorStatsModel != null) {
                    repositoryAuthorStatsModels.add(authorStatsModel);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        Function<RepositoryAuthorStatsModel, ProfileModel> statsToProfile = statsModel -> {
            Seniority seniority = Seniority.fromDate(statsModel.firstCommitDate());
            ProgrammingLanguage language = ProgrammingLanguage.fromExtension(statsModel.extension());
            return new ProfileModel(statsModel.author(), seniority, language, statsModel.teamMembers());
        };

        return repositoryAuthorStatsModels.stream().map(statsToProfile).toList();
    }

    private RepositoryAuthorStatsModel buildRepositoryAuthorStats(Map.Entry<String, List<CommitModel>> entry, Set<String> allAuthors) throws ParseException {
        RepositoryAuthorStatsModel authorStatsModel = null;

        String author = entry.getKey();
        List<CommitModel> authorCommits = entry.getValue();
        CommitModel firstCommit = authorCommits.stream()
                .filter(commitModel -> commitModel.date() != null)
                .min(Comparator.comparing(CommitModel::date))
                .orElse(null);

        List<String> teamMembers = allAuthors.stream().filter(otherAuthor -> !otherAuthor.equals(author)).toList();

        Date date = null;
        String extension;
        if (firstCommit != null) {
            String formattedDate = firstCommit.date();
            if (formattedDate != null && !formattedDate.isEmpty()) {
                date = new SimpleDateFormat(CSV_DATE_FORMAT).parse(formattedDate);
            }

            String changedFilesList = firstCommit.changedFiles();
            if (changedFilesList != null && !changedFilesList.trim().isEmpty()) {
                changedFilesList = changedFilesList.trim();
                int spaceIndex = changedFilesList.indexOf(" ");
                String firstFile;
                if (spaceIndex > 0) {
                    firstFile = changedFilesList.substring(0, spaceIndex);
                } else {
                    firstFile = changedFilesList;
                }
                extension = FilenameUtils.getExtension(firstFile);

                authorStatsModel = new RepositoryAuthorStatsModel(author, date, extension, teamMembers);
            }
        }

        return authorStatsModel;
    }
}
