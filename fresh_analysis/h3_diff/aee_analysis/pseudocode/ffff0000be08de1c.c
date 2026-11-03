
undefined8 FUN_ffff0000be08de1c(undefined4 *param_1)

{
  undefined8 uVar1;
  undefined4 uVar2;
  
  if (param_1 == (undefined4 *)0x0) {
    return 0x100006;
  }
  uVar1 = FUN_ffff0000be08da1c();
  uVar2 = DAT_ffff0000be1b7c50;
  if ((int)uVar1 != 0) {
    FUN_ffff0000be05fd98(s___s__seccfg_not_found__ffff0000be0bca61,s_SEC_CFG_ffff0000be0c6d63);
    FUN_ffff0000be05fd98(s___s__fatal_error__ffff0000be0a9c9b,s_SEC_CFG_ffff0000be0c6d63);
    uVar1 = 0x103000;
    uVar2 = 0;
  }
  *param_1 = uVar2;
  return uVar1;
}


