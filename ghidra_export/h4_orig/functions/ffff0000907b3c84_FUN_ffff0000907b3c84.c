// ffff0000907b3c84  size=152  FUN_ffff0000907b3c84

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907b3c84(undefined4 param_1)

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
  
  iVar1 = FUN_ffff0000907b3828();
  if (iVar1 == 0) {
    FUN_ffff0000907b33a4();
    _DAT_ffff00009090d78c = param_1;
    FUN_ffff0000907b3460();
    local_28 = 0;
    local_38 = 0;
    uStack_30 = 0;
    local_48 = 0;
    uStack_40 = 0;
    local_58 = 0;
    uStack_50 = 0;
    local_60 = 0;
    FUN_ffff0000907b81d4(s_seccfg_ffff0000907d1c70,&local_60,0x3c,0x3c);
    uVar2 = 0;
  }
  else {
    FUN_ffff00009077e11c
              (s___s__seccfg_not_found___0x_x__ffff0000907e711d,s_SEC_UNLOCK_ffff000090803577,iVar1)
    ;
    uVar2 = 0x103000;
  }
  return uVar2;
}


