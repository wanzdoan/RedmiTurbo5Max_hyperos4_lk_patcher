
void FUN_ffff0000b804f578(ulong *param_1,undefined4 *param_2)

{
  undefined4 uVar1;
  
  if ((param_1 != (ulong *)0x0) && (param_2 != (undefined4 *)0x0)) {
    uVar1 = *(undefined4 *)(DAT_ffff0000b8133c80 + 8);
    *param_1 = (ulong)*(uint *)(DAT_ffff0000b8133c80 + 4);
    *param_2 = uVar1;
    return;
  }
  FUN_ffff0000b804f4a8
            (s__s__get_plat_dbg_info_args_inval_ffff0000b8097803,s__MBOOT_PARAMS__ffff0000b8095af6);
  FUN_ffff0000b804c33c
            (s__s__get_plat_dbg_info_args_inval_ffff0000b8097803,s__MBOOT_PARAMS__ffff0000b8095af6);
  return;
}


