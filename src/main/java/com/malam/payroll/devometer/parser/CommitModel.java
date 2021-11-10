package com.malam.payroll.devometer.parser;

record CommitModel(String commit,
                   String author,
                   String date,
                   String message,
                   String changedFiles) {
}