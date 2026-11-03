// ffff00009070d7dc  size=288  FUN_ffff00009070d7dc

void FUN_ffff00009070d7dc(undefined8 param_1)

{
  char *pcVar1;
  int iVar2;
  uint uVar3;
  undefined8 uVar4;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_30;
  ulong uStack_28;
  
  local_30 = 0;
  uStack_28 = 0;
  local_40 = 0;
  uStack_38 = 0;
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  uVar4 = FUN_ffff00009077f250(&DAT_ffff0000907f3fed);
  iVar2 = FUN_ffff00009077f348(param_1,&DAT_ffff0000907f3fed,uVar4);
  if (iVar2 == 0) {
    FUN_ffff000090709bec(s_Enable_ultra_flash__ffff000090801f15);
    DAT_ffff000090816d70 = '\x01';
  }
  else {
    uVar4 = FUN_ffff00009077f250(&DAT_ffff0000907f938c);
    iVar2 = FUN_ffff00009077f348(param_1,&DAT_ffff0000907f938c,uVar4);
    if (iVar2 == 0) {
      FUN_ffff000090709bec(s_Disable_ultra_flash__ffff0000907f26c7);
      DAT_ffff000090816d70 = '\0';
    }
    else {
      pcVar1 = s_Disabled_ffff0000907ebf62;
      if (DAT_ffff000090816d70 != '\0') {
        pcVar1 = s_Enabled_ffff0000907e7459;
      }
      uVar3 = FUN_ffff00009077f6fc
                        (&local_60,0x40,s_Current_ultra_flash_setting__s_ffff000090801f29,pcVar1);
      if (0x3f < uVar3) {
        FUN_ffff00009077f2ac(&local_60,s_Error__Response_buffer_overflow_ffff0000907dd905,0x3f);
        uStack_28 = uStack_28 & 0xffffffffffffff;
      }
      FUN_ffff000090709bec(&local_60);
    }
  }
  FUN_ffff000090709cdc(&DAT_ffff0000907df338);
  return;
}


