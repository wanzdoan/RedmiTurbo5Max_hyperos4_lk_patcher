// ffff000090781968  size=112  FUN_ffff000090781968

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090781968(ulong *param_1,undefined4 *param_2)

{
  undefined4 uVar1;
  
  if ((param_1 != (ulong *)0x0) && (param_2 != (undefined4 *)0x0)) {
    uVar1 = *(undefined4 *)(_DAT_ffff000090887078 + 8);
    *param_1 = (ulong)*(uint *)(_DAT_ffff000090887078 + 4);
    *param_2 = uVar1;
    return;
  }
  FUN_ffff000090781820
            (s__s__get_plat_dbg_info_args_inval_ffff0000907f0427,s__MBOOT_PARAMS__ffff0000907ed058);
  FUN_ffff00009077de54
            (s__s__get_plat_dbg_info_args_inval_ffff0000907f0427,s__MBOOT_PARAMS__ffff0000907ed058);
  return;
}


