package com.currentdetection.ui.checkers;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00004\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\u001a&\u0010\u0000\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0006\u0010\u0007\u001a:\u0010\b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\u000e2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00010\u0010H\u0007\u001a$\u0010\u0012\u001a\u00020\u00012\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00010\u000e2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00010\u000eH\u0007\u001a\b\u0010\u0015\u001a\u00020\u0001H\u0007\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u0016"}, d2 = {"AnimatedWifiIcon", "", "modifier", "Landroidx/compose/ui/Modifier;", "tint", "Landroidx/compose/ui/graphics/Color;", "AnimatedWifiIcon-4WTKRHQ", "(Landroidx/compose/ui/Modifier;J)V", "CheckerCard", "network", "Lcom/currentdetection/data/local/entities/NetworkEntity;", "isConnected", "", "onDelete", "Lkotlin/Function0;", "onEdit", "Lkotlin/Function1;", "", "CheckersScreen", "onBack", "onAddChecker", "ConnectedBadge", "app_debug"})
public final class CheckersScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void CheckersScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onAddChecker) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void CheckerCard(@org.jetbrains.annotations.NotNull()
    com.currentdetection.data.local.entities.NetworkEntity network, boolean isConnected, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDelete, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onEdit) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void ConnectedBadge() {
    }
}