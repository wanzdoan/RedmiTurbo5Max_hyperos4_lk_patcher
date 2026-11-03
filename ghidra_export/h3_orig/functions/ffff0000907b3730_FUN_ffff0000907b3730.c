// ffff0000907b3730  size=168  FUN_ffff0000907b3730

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907b3730(undefined4 param_1)

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
  
  iVar1 = FUN_ffff0000907b31b8();
  if (iVar1 == 0) {
    FUN_ffff0000907b2d34();
    _DAT_ffff00009090d6d0 = param_1;
    FUN_ffff0000907b2df0();
    local_28 = 0;
    local_38 = 0;
    uStack_30 = 0;
    local_48 = 0;
    uStack_40 = 0;
    local_58 = 0;
    uStack_50 = 0;
    local_60 = 0;
    FUN_ffff0000907b7b60(s_seccfg_ffff0000907d1c33,&local_60,0x3c,0x3c);
    uVar2 = 0;
  }
  else {
    FUN_ffff00009077de54(s___s__seccfg_not_found__ffff0000907f3aa3,s_SEC_CFG_ffff000090803332);
    FUN_ffff00009077de54(s___s__fatal_error__ffff0000907d890a,s_SEC_CFG_ffff000090803332);
    uVar2 = 0x103000;
  }
  return uVar2;
}


