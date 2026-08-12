package com.currentdetection.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010\tJ\u0014\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\f0\u000bH\u0016J\u0014\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\f0\u000bH\u0016J\u0016\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0007\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010\tJ\u0016\u0010\u0010\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010\tJ\f\u0010\u0011\u001a\u00020\b*\u00020\u0012H\u0002J\f\u0010\u0013\u001a\u00020\u0012*\u00020\bH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/currentdetection/data/repository/NetworkRepositoryImpl;", "Lcom/currentdetection/domain/repository/NetworkRepository;", "dao", "Lcom/currentdetection/data/local/NetworkDao;", "(Lcom/currentdetection/data/local/NetworkDao;)V", "deleteNetwork", "", "network", "Lcom/currentdetection/domain/models/Network;", "(Lcom/currentdetection/domain/models/Network;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllNetworks", "Lkotlinx/coroutines/flow/Flow;", "", "getEnabledNetworks", "insertNetwork", "", "updateNetwork", "toDomain", "Lcom/currentdetection/data/local/entities/NetworkEntity;", "toEntity", "app_debug"})
public final class NetworkRepositoryImpl implements com.currentdetection.domain.repository.NetworkRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.currentdetection.data.local.NetworkDao dao = null;
    
    public NetworkRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.currentdetection.data.local.NetworkDao dao) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.currentdetection.domain.models.Network>> getAllNetworks() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.currentdetection.domain.models.Network>> getEnabledNetworks() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object insertNetwork(@org.jetbrains.annotations.NotNull()
    com.currentdetection.domain.models.Network network, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object updateNetwork(@org.jetbrains.annotations.NotNull()
    com.currentdetection.domain.models.Network network, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteNetwork(@org.jetbrains.annotations.NotNull()
    com.currentdetection.domain.models.Network network, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final com.currentdetection.domain.models.Network toDomain(com.currentdetection.data.local.entities.NetworkEntity $this$toDomain) {
        return null;
    }
    
    private final com.currentdetection.data.local.entities.NetworkEntity toEntity(com.currentdetection.domain.models.Network $this$toEntity) {
        return null;
    }
}