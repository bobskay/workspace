package wang.wangby.dao.routingDataSource;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {
	
	public RoutingDataSource(DataSource defaultTargetDataSource,Map<String,DataSource> datasource) {
		Map map=datasource;
		this.setDefaultTargetDataSource(defaultTargetDataSource);
		this.setTargetDataSources(map);
	}
	
    @Override
    protected Object determineCurrentLookupKey() {
        return DbContextHolder.getDbType();
    }

}