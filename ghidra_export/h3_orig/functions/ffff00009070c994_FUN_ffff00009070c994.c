// ffff00009070c994  size=192  FUN_ffff00009070c994

void FUN_ffff00009070c994(undefined8 param_1,long param_2,int param_3)

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
    FUN_ffff000090709d10(s_Token_is_empty__failed__ffff0000907f3e30);
  }
  iVar1 = FUN_ffff000090789190(param_2,param_3);
  if (iVar1 != 0) {
    FUN_ffff000090709d10(s_Verified_failed__ffff0000907d7391);
  }
  iVar1 = FUN_ffff0000907b3614(3);
  if (iVar1 != 0) {
    FUN_ffff000090709d10(s_Set_seccfg_state_failed__ffff0000907f3e48);
  }
  FUN_ffff00009077f434(&local_48,0x28,s_Set_mtk_lock_state_successed__ffff0000907e4114);
  uStack_28 = uStack_28 & 0xffffffffffffff;
  FUN_ffff000090709c60(&local_48);
  FUN_ffff000090709d50(&DAT_ffff0000907df295);
  FUN_ffff00009077cbd8(3000000);
  FUN_ffff000090709d68();
  return;
}


