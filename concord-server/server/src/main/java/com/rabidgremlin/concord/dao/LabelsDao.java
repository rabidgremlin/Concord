package com.rabidgremlin.concord.dao;

import java.util.List;

import com.rabidgremlin.concord.api.UnlabelledPhrase;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import com.rabidgremlin.concord.api.Label;
import com.rabidgremlin.concord.plugin.SystemLabelStore;
import org.jdbi.v3.sqlobject.transaction.Transaction;

public interface LabelsDao
{
	@SqlUpdate("DELETE from labels")
	void deleteAllLabels();
	
	@SqlUpdate("REPLACE INTO labels(label, shortDescription, longDescription) VALUES (:label, :shortDescription,:longDescription)")
    void upsert(@BindBean Label label);
	
	@SqlQuery("SELECT * FROM labels ORDER BY label")
    @RegisterBeanMapper(Label.class)
    List<Label> getLabels();

	@Transaction
	default void replaceLabels(List<Label> labels) {
		deleteAllLabels();

		for(Label label:labels)
		{
			// skip header
			if (label.getLabel().equals("label")){
				continue;
			}
			upsert(label);
		}
	}
}
