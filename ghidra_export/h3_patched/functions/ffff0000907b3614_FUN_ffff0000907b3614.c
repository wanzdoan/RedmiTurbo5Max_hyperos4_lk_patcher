// ffff0000907b3614  size=152  FUN_ffff0000907b3614

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907b3614(undefined4 param_1)

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
    _DAT_ffff00009090d6cc = param_1;
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
    FUN_ffff00009077de54
              (s___s__seccfg_not_found___0x_x__ffff0000907e6fc2,s_SEC_UNLOCK_ffff00009080334f,iVar1)
    ;
    uVar2 = 0x103000;
  }
  return uVar2;
}


