package com.rabidgremlin.concord.dao;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import com.rabidgremlin.concord.api.Label;

public interface PhrasesDao 
{	
	
	@SqlUpdate("REPLACE INTO phrases(phraseId, text, completed) VALUES (:phraseId, :text,:completed)")
    void insert(@Bind("phraseId") String phraseId, @Bind("text") String text, @Bind("completed") Boolean completed);
	
	//@SqlQuery("SELECT * FROM labels ORDER BY label")
    //@RegisterBeanMapper(Label.class)
    //List<Label> getLabels();
}
