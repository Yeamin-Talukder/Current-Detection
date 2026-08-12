package com.currentdetection.ui.settings;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000&\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0007\u001a&\u0010\u0004\u001a\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00010\u00062\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\u0006H\u0007\u001a,\u0010\b\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\n2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\fH\u0007\u001a\u0018\u0010\r\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u0003H\u0007\u00a8\u0006\u000f"}, d2 = {"SettingsCategory", "", "title", "", "SettingsScreen", "onManageCheckers", "Lkotlin/Function0;", "onExportHistory", "SwitchSettingItem", "checked", "", "onCheckedChange", "Lkotlin/Function1;", "TextSettingItem", "subtitle", "app_debug"})
public final class SettingsScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void SettingsScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onManageCheckers, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onExportHistory) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void SettingsCategory(@org.jetbrains.annotations.NotNull()
    java.lang.String title) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void SwitchSettingItem(@org.jetbrains.annotations.NotNull()
    java.lang.String title, boolean checked, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onCheckedChange) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void TextSettingItem(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String subtitle) {
    }
}