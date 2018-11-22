package com.rabidgremlin.concord.dao;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Fields;
import org.mockito.junit.MockitoJUnitRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PhrasesDaoTest {

//    @Mock
//    private DataSource dataSource;
//
//    @Mock
//    private Connection connection;
//
//    @Mock
//    private PreparedStatement statement;
//
//    @Mock
//    private ResultSet results;
//    private String userId = "ted12";
//    private String label = "OrderTaxi";
//    private String text = "I want to order a cab";
//
//
//    @BeforeClass
//    public static void setUpClass() throws Exception {
//    }
//
//    @Before
//    public void setUp() throws SQLException {
//        when(dataSource.getConnection()).thenReturn(connection);
//        when(dataSource.getConnection(anyString(), anyString())).thenReturn(connection);
//        doNothing().when(connection).commit();
//        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(statement);
//        doNothing().when(statement).setString(anyInt(), anyString());
//        when(statement.execute()).thenReturn(Boolean.TRUE);
//        when(statement.getGeneratedKeys()).thenReturn(results);
//        when(results.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
//        when(results.getInt(Fields.GENERATED_KEYS)).thenReturn(userId);
//    }
//
//    @Test
//    public void testPhrasesUpsert() throws SQLException {
//        PhrasesDao phrasesDao;
//
//    }
}

