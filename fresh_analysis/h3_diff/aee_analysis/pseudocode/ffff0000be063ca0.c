
void FUN_ffff0000be063ca0(undefined8 *param_1,undefined4 *param_2)

{
  undefined4 uVar1;
  
  uVar1 = DAT_ffff0000be1321d8;
  if ((param_1 != (undefined8 *)0x0) && (param_2 != (undefined4 *)0x0)) {
    *param_1 = DAT_ffff0000be1321c0;
    *param_2 = uVar1;
    return;
  }
  FUN_ffff0000be063b40
            (s__s__get_mboot_params_default_inf_ffff0000be0abd6d,s__MBOOT_PARAMS__ffff0000be0b7f52);
  FUN_ffff0000be05fd98
            (s__s__get_mboot_params_default_inf_ffff0000be0abd6d,s__MBOOT_PARAMS__ffff0000be0b7f52);
  return;
}


