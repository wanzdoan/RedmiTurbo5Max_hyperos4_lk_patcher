
void FUN_ffff0000be063d0c(ulong *param_1,undefined4 *param_2)

{
  undefined4 uVar1;
  
  if ((param_1 != (ulong *)0x0) && (param_2 != (undefined4 *)0x0)) {
    uVar1 = *(undefined4 *)(DAT_ffff0000be1321e0 + 8);
    *param_1 = (ulong)*(uint *)(DAT_ffff0000be1321e0 + 4);
    *param_2 = uVar1;
    return;
  }
  FUN_ffff0000be063b40
            (s__s__get_plat_dbg_info_args_inval_ffff0000be0ba4c8,s__MBOOT_PARAMS__ffff0000be0b7f52);
  FUN_ffff0000be05fd98
            (s__s__get_plat_dbg_info_args_inval_ffff0000be0ba4c8,s__MBOOT_PARAMS__ffff0000be0b7f52);
  return;
}


