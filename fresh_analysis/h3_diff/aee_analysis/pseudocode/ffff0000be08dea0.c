
undefined8 FUN_ffff0000be08dea0(undefined4 param_1)

{
  int iVar1;
  undefined8 uVar2;
  undefined8 local_60;
  undefined8 local_58;
  undefined8 uStack_50;
  undefined8 local_48;
  undefined8 uStack_40;
  undefined8 local_38;
  undefined8 uStack_30;
  undefined4 local_28;
  
  iVar1 = FUN_ffff0000be08da1c();
  if (iVar1 == 0) {
    FUN_ffff0000be08d598();
    DAT_ffff0000be1b7c50 = param_1;
    FUN_ffff0000be08d654();
    local_28 = 0;
    local_38 = 0;
    uStack_30 = 0;
    local_48 = 0;
    uStack_40 = 0;
    local_58 = 0;
    uStack_50 = 0;
    local_60 = 0;
    FUN_ffff0000be091ff8(s_seccfg_ffff0000be0a4fd5,&local_60,0x3c,0x3c);
    uVar2 = 0;
  }
  else {
    FUN_ffff0000be05fd98(s___s__seccfg_not_found__ffff0000be0bca61,s_SEC_CFG_ffff0000be0c6d63);
    FUN_ffff0000be05fd98(s___s__fatal_error__ffff0000be0a9c9b,s_SEC_CFG_ffff0000be0c6d63);
    uVar2 = 0x103000;
  }
  return uVar2;
}


