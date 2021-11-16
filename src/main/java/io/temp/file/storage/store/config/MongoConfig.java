package io.temp.file.storage.store.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories
public class MongoConfig /* extends AbstractMongoClientConfiguration */ {

    // @Autowired
    // private MappingMongoConverter mongoConverter;

    // @Override
    // protected String getDatabaseName() {
    // return null;
    // }

    // @Bean
    // public GridFsTemplate gridFsTemplate() throws Exception {
    // return new GridFsTemplate(mongoDbFactory(), mongoConverter);
    // }
}
