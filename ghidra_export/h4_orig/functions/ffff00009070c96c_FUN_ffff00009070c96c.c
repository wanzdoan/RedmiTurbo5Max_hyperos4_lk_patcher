// ffff00009070c96c  size=160  FUN_ffff00009070c96c

void FUN_ffff00009070c96c(void)

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
  iVar1 = FUN_ffff0000907b3c28(&local_14);
  if (iVar1 != 0) {
    FUN_ffff000090709c9c(s_Get_seccfg_state_failed__ffff0000907dd8cb);
  }
  FUN_ffff00009077f6fc(&local_40,0x28,s_seccfg_state_is__u_ffff0000907f5cbb,local_14);
  uStack_20 = uStack_20 & 0xffffffffffffff;
  FUN_ffff000090709bec(&local_40);
  FUN_ffff00009077f6fc(&local_40,0x28,s_mtkrpmb_state_is__u__ffff0000907d7412,4);
  uStack_20 = uStack_20 & 0xffffffffffffff;
  FUN_ffff000090709bec(&local_40);
  FUN_ffff000090709cdc(&DAT_ffff0000907df338);
  return;
}


