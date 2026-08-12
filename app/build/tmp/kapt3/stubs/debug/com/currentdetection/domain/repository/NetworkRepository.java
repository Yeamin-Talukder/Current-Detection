package com.currentdetection.domain.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\t0\bH&J\u0014\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\t0\bH&J\u0016\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\r\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006\u00a8\u0006\u000e"}, d2 = {"Lcom/currentdetection/domain/repository/NetworkRepository;", "", "deleteNetwork", "", "network", "Lcom/currentdetection/domain/models/Network;", "(Lcom/currentdetection/domain/models/Network;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllNetworks", "Lkotlinx/coroutines/flow/Flow;", "", "getEnabledNetworks", "insertNetwork", "", "updateNetwork", "app_debug"})
public abstract interface NetworkRepository {
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.currentdetection.domain.models.Network>> getAllNetworks();
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.currentdetection.domain.models.Network>> getEnabledNetworks();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertNetwork(@org.jetbrains.annotations.NotNull()
    com.currentdetection.domain.models.Network network, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateNetwork(@org.jetbrains.annotations.NotNull()
    com.currentdetection.domain.models.Network network, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteNetwork(@org.jetbrains.annotations.NotNull()
    com.currentdetection.domain.models.Network network, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}