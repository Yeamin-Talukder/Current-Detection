package com.currentdetection.data.local;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0006\bg\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0007\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00060\bH\'J\u0014\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\n0\bH\'J\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00060\nH\u00a7@\u00a2\u0006\u0002\u0010\u0004J$\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\n0\b2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000eH\'J\u0016\u0010\u0010\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\u0012J\u0016\u0010\u0013\u001a\u00020\u00032\u0006\u0010\u0011\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\u0012\u00a8\u0006\u0014"}, d2 = {"Lcom/currentdetection/data/local/PowerEventDao;", "", "clearAllEvents", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getActiveOutageEvent", "Lcom/currentdetection/data/local/entities/PowerEventEntity;", "getActiveOutageEventFlow", "Lkotlinx/coroutines/flow/Flow;", "getAllEvents", "", "getAllEventsList", "getEventsInRange", "startTime", "", "endTime", "insertEvent", "event", "(Lcom/currentdetection/data/local/entities/PowerEventEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateEvent", "app_debug"})
@androidx.room.Dao()
public abstract interface PowerEventDao {
    
    @androidx.room.Query(value = "SELECT * FROM power_events ORDER BY startTime DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.currentdetection.data.local.entities.PowerEventEntity>> getAllEvents();
    
    @androidx.room.Query(value = "SELECT * FROM power_events WHERE endTime IS NULL ORDER BY startTime DESC LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getActiveOutageEvent(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.currentdetection.data.local.entities.PowerEventEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM power_events WHERE endTime IS NULL ORDER BY startTime DESC LIMIT 1")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.currentdetection.data.local.entities.PowerEventEntity> getActiveOutageEventFlow();
    
    @androidx.room.Query(value = "SELECT * FROM power_events WHERE startTime >= :startTime AND startTime < :endTime ORDER BY startTime DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.currentdetection.data.local.entities.PowerEventEntity>> getEventsInRange(long startTime, long endTime);
    
    @androidx.room.Query(value = "SELECT * FROM power_events ORDER BY startTime DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllEventsList(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.currentdetection.data.local.entities.PowerEventEntity>> $completion);
    
    @androidx.room.Query(value = "DELETE FROM power_events")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clearAllEvents(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertEvent(@org.jetbrains.annotations.NotNull()
    com.currentdetection.data.local.entities.PowerEventEntity event, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateEvent(@org.jetbrains.annotations.NotNull()
    com.currentdetection.data.local.entities.PowerEventEntity event, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}