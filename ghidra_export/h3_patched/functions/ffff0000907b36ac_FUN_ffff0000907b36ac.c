// ffff0000907b36ac  size=8  FUN_ffff0000907b36ac

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907b36ac(undefined4 *param_1)

{
  undefined8 uVar1;
  undefined4 uVar2;
  
  if (param_1 == (undefined4 *)0x0) {
    return 0x100006;
  }
  uVar1 = FUN_ffff0000907b31b8();
  uVar2 = _DAT_ffff00009090d6d0;
  if ((int)uVar1 != 0) {
    FUN_ffff00009077de54(s___s__seccfg_not_found__ffff0000907f3a8c,s_SEC_CFG_ffff000090803332);
    FUN_ffff00009077de54(s___s__fatal_error__ffff0000907d890a,s_SEC_CFG_ffff000090803332);
    uVar1 = 0x103000;
    uVar2 = 0;
  }
  *param_1 = uVar2;
  return uVar1;
}


