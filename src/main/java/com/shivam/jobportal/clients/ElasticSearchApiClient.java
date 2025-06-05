package com.shivam.jobportal.clients;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import com.shivam.jobportal.dtos.JobDto;
import org.springframework.stereotype.Component;

@Component
public class ElasticSearchApiClient {
    private final String JOB_INDEX = "jobs";
    private final ElasticsearchClient client;

    public ElasticSearchApiClient(ElasticsearchClient client) {
        this.client = client;
    }

    public void saveJob(JobDto jobDto) {
        try {
            IndexRequest<JobDto> indexRequest = IndexRequest.of(i -> i
                    .index(JOB_INDEX)
                    .id(String.valueOf(jobDto.getId()))
                    .document(jobDto)
            );

            client.index(indexRequest);
        } catch (Exception e){
            throw new RuntimeException("Failed to save job to Elasticsearch", e);
        }
    }

    public void updateJob(JobDto jobDto) {
        try {
            UpdateRequest<JobDto,JobDto> updateRequest = UpdateRequest.of(u -> u
                    .index(JOB_INDEX)
                    .id(String.valueOf(jobDto.getId()))
                    .doc(jobDto)
            );

            client.update(updateRequest, JobDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update job in Elasticsearch", e);
        }
    }

    public void deleteJobById(Long jobId) {
        try {
            DeleteRequest deleteRequest = DeleteRequest.of(d -> d
                    .index(JOB_INDEX)
                    .id(String.valueOf(jobId))
            );

            client.delete(deleteRequest);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete job from Elasticsearch", e);
        }
    }
}
