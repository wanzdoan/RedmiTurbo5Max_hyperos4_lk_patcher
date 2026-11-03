// ffff00009070c8f4  size=160  FUN_ffff00009070c8f4

void FUN_ffff00009070c8f4(void)

{
  int iVar1;
  undefined8 local_40;
  undefined8 local_38;
  undefined8 uStack_30;
  undefined8 local_28;
  ulong uStack_20;
  undefined4 local_14;
  
  local_28 = 0;
  uStack_20 = 0;
  local_38 = 0;
  uStack_30 = 0;
  local_14 = 4;
  local_40 = 0;
  iVar1 = FUN_ffff0000907b35b8(&local_14);
  if (iVar1 != 0) {
    FUN_ffff000090709d10(s_Get_seccfg_state_failed__ffff0000907dd811);
  }
  FUN_ffff00009077f434(&local_40,0x28,s_seccfg_state_is__u_ffff0000907f5b2d,local_14);
  uStack_20 = uStack_20 & 0xffffffffffffff;
  FUN_ffff000090709c60(&local_40);
  FUN_ffff00009077f434(&local_40,0x28,s_mtkrpmb_state_is__u__ffff0000907d737c,4);
  uStack_20 = uStack_20 & 0xffffffffffffff;
  FUN_ffff000090709c60(&local_40);
  FUN_ffff000090709d50(&DAT_ffff0000907df27e);
  return;
}


