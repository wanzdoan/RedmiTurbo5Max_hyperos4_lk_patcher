// ffff0000907b3d24  size=112  FUN_ffff0000907b3d24

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907b3d24(undefined4 *param_1)

{
  undefined8 uVar1;
  undefined4 uVar2;
  
  uVar1 = FUN_ffff0000907b3828();
  uVar2 = _DAT_ffff00009090d790;
  if ((int)uVar1 != 0) {
    FUN_ffff00009077e11c(s___s__seccfg_not_found__ffff0000907f3c2f,s_SEC_CFG_ffff00009080355a);
    FUN_ffff00009077e11c(s___s__fatal_error__ffff0000907d898b,s_SEC_CFG_ffff00009080355a);
    uVar1 = 0x103000;
    uVar2 = 0;
  }
  *param_1 = uVar2;
  return uVar1;
}


