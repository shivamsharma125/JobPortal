package com.shivam.jobportal.services;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.shivam.jobportal.dtos.JobDto;
import com.shivam.jobportal.dtos.JobFilterRequestDto;
import com.shivam.jobportal.models.Job;
import com.shivam.jobportal.repositories.JobRepository;
import com.shivam.jobportal.utils.JobUtils;
import com.shivam.jobportal.utils.search.jobs.ElasticSearchFilterQuery;
import com.shivam.jobportal.utils.search.jobs.JpaFilterSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SearchService implements ISearchService {
    private final JobRepository jobRepository;
    private final ElasticsearchClient elasticsearchClient;

    public SearchService(JobRepository jobRepository, ElasticsearchClient elasticsearchClient) {
        this.jobRepository = jobRepository;
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public List<JobDto> filterJobs(JobFilterRequestDto requestDto) {
        try {
            return searchJobsUsingElasticSearch(requestDto);
        } catch (Exception e){
            System.out.println("ElasticSearch search failed, falling back to JPA search: " + e.getMessage());
            return searchUsingJpa(requestDto);
        }
    }

    private List<JobDto> searchJobsUsingElasticSearch(JobFilterRequestDto requestDto) throws IOException {
        SearchRequest.Builder searchRequestBuilder = new SearchRequest.Builder()
                .index("jobs");

        if (requestDto.getPage() != null && requestDto.getPage() >= 0 &&
                requestDto.getSize() != null && requestDto.getSize() > 0) {
            searchRequestBuilder
                    .from(requestDto.getPage() * requestDto.getSize())
                    .size(requestDto.getSize());
        }

        List<Query> queries = ElasticSearchFilterQuery.searchJobsQueries(requestDto);

        searchRequestBuilder.query(q -> q.bool(b -> b
                .must(queries)
        ));

        String sortBy = "salary".equalsIgnoreCase(requestDto.getSortBy()) ? "minSalary" : "postedAt";
        SortOrder sortOrder = "asc".equalsIgnoreCase(requestDto.getDirection()) ? SortOrder.Asc : SortOrder.Desc;

        searchRequestBuilder.sort(s -> s.field(f -> f.field(sortBy).order(sortOrder)));

        SearchResponse<JobDto> searchResponse = elasticsearchClient.search(searchRequestBuilder.build(), JobDto.class);

        return searchResponse.hits().hits().stream().map(Hit::source).toList();
    }

    private List<JobDto> searchUsingJpa(JobFilterRequestDto request){
        Sort sort = "salary".equalsIgnoreCase(request.getSortBy())
                ? Sort.by("maxSalary")
                : Sort.by("postedAt");

        sort = "desc".equalsIgnoreCase(request.getDirection())
                ? sort.descending()
                : sort.ascending();

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        Page<Job> jobPage = jobRepository.findAll(JpaFilterSpecification.filterJobs(request), pageable);
        return jobPage.stream().map(JobUtils::from).toList();
    }

}
