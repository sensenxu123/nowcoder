package com.sensenxu.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
@Data
@Document(indexName = "discusspost",shards = 6, replicas = 3)
public class discussPost {
    @Id
    private Integer id;
    @Field(type = FieldType.Integer)
    private Integer userId;
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String  title;
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String content;
    @Field(type = FieldType.Integer)
    private Integer type;
    @Field(type = FieldType.Integer)
    private Integer status;
    @Field(type = FieldType.Date)
    private Date createTime;
    @Field(type = FieldType.Integer)
    private Integer commentCount;
    @Field(type = FieldType.Double)
    private double score;

}
