package com.malam.payroll.devometer.parser;

import java.util.Date;
import java.util.List;

record RepositoryAuthorStatsModel(String author, Date firstCommitDate, String extension, List<String> teamMembers) {

}