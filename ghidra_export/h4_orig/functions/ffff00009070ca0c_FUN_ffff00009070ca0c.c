// ffff00009070ca0c  size=192  FUN_ffff00009070ca0c

void FUN_ffff00009070ca0c(undefined8 param_1,long param_2,int param_3)

{
  int iVar1;
  undefined8 local_48;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_30;
  ulong uStack_28;
  
  local_30 = 0;
  uStack_28 = 0;
  local_40 = 0;
  uStack_38 = 0;
  local_48 = 0;
  if ((param_2 == 0) || (param_3 == 0)) {
    FUN_ffff000090709c9c(s_Token_is_empty__failed__ffff0000907f3fbc);
  }
  iVar1 = FUN_ffff000090789450(param_2,param_3);
  if (iVar1 != 0) {
    FUN_ffff000090709c9c(s_Verified_failed__ffff0000907d7427);
  }
  iVar1 = FUN_ffff0000907b3c84(3);
  if (iVar1 != 0) {
    FUN_ffff000090709c9c(s_Set_seccfg_state_failed__ffff0000907f3fd4);
  }
  FUN_ffff00009077f6fc(&local_48,0x28,s_Set_mtk_lock_state_successed__ffff0000907e41e5);
  uStack_28 = uStack_28 & 0xffffffffffffff;
  FUN_ffff000090709bec(&local_48);
  FUN_ffff000090709cdc(&DAT_ffff0000907df338);
  FUN_ffff00009077cea0(3000000);
  FUN_ffff000090709cf4();
  return;
}


