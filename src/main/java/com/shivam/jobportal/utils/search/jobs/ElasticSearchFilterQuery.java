package com.shivam.jobportal.utils.search.jobs;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import com.shivam.jobportal.dtos.JobFilterRequestDto;

import java.util.ArrayList;
import java.util.List;

public class ElasticSearchFilterQuery {

    public static List<Query> searchJobsQueries(JobFilterRequestDto requestDto) {

        List<Query> queries = new ArrayList<>();

        // Fuzzy search on position
        if (requestDto.getPosition() != null) {
            List<String> fields = new ArrayList<>(List.of("position", "description"));
            if (requestDto.getSkills() == null || requestDto.getSkills().isEmpty()) {
                fields.add("skills");
            }

            queries.add(new MultiMatchQuery.Builder()
                    .fields(fields)
                    .query(requestDto.getPosition())
                    .fuzziness("AUTO")
                    .build()._toQuery());
        }

        // Fuzzy search on skills
        if (requestDto.getSkills() != null && !requestDto.getSkills().isEmpty()) {
            List<Query> skillQueries = requestDto.getSkills().stream()
                    .map(skill -> new FuzzyQuery.Builder()
                            .field("skills")
                            .value(skill)
                            .fuzziness("AUTO")
                            .build()._toQuery())
                    .toList();
            queries.addAll(skillQueries);
        }

        // Filter by experience range (min and max)
        if (requestDto.getMinExperience() != null || requestDto.getMaxExperience() != null) {
            RangeQuery.Builder experienceRangeQuery = new RangeQuery.Builder();
            if (requestDto.getMinExperience() != null) {
                experienceRangeQuery
                        .field("minExperience")
                        .gte(JsonData.of(requestDto.getMinExperience()));
            }
            if (requestDto.getMaxExperience() != null) {
                experienceRangeQuery
                        .field("maxExperience")
                        .lte(JsonData.of(requestDto.getMaxExperience()));
            }
            queries.add(experienceRangeQuery.build()._toQuery());
        }

        // Filter by salary range
        if (requestDto.getMinSalary() != null || requestDto.getMaxSalary() != null) {
            RangeQuery.Builder salaryRangeQuery = new RangeQuery.Builder();
            if (requestDto.getMinSalary() != null) {
                salaryRangeQuery
                        .field("minSalary")
                        .gte(JsonData.of(requestDto.getMinSalary()));
            }
            if (requestDto.getMaxSalary() != null) {
                salaryRangeQuery
                        .field("maxSalary")
                        .lte(JsonData.of(requestDto.getMaxSalary()));
            }
            queries.add(salaryRangeQuery.build()._toQuery());
        }

        // Filter by notice period
        if (requestDto.getNoticePeriod() != null) {
            queries.add(new RangeQuery.Builder()
                    .field("noticePeriod")
                    .gte(JsonData.of(requestDto.getNoticePeriod()))
                    .build()
                    ._toQuery());
        }

        // Filter by location
        if (requestDto.getLocation() != null) {
            queries.add(new TermQuery.Builder()
                    .field("location.keyword")
                    .value(requestDto.getLocation())
                    .build()
                    ._toQuery());
        }

        // Filter by remote status
        if (requestDto.getIsRemote() != null) {
            queries.add(new TermQuery.Builder()
                    .field("isRemote")
                    .value(requestDto.getIsRemote())
                    .build()
                    ._toQuery());
        }

        // Filter by job type
        if (requestDto.getJobType() != null) {
            queries.add(new TermQuery.Builder()
                    .field("jobType.keyword")
                    .value(requestDto.getJobType())
                    .build()
                    ._toQuery());
        }

        // Filter by experience level
        if (requestDto.getExperienceLevel() != null) {
            queries.add(new TermQuery.Builder()
                    .field("experienceLevel.keyword")
                    .value(requestDto.getExperienceLevel())
                    .build()
                    ._toQuery());
        }

        return queries;
    }
}
