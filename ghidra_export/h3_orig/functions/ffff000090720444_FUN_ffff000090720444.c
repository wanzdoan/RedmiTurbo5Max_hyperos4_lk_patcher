// ffff000090720444  size=148  FUN_ffff000090720444

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090720444(undefined4 param_1)

{
  int iVar1;
  
  _DAT_ffff0000908605a0 = FUN_ffff00009077d24c(4);
  if (_DAT_ffff0000908605a0 != 0) {
    FUN_ffff00009077eb88(_DAT_ffff0000908605a0,0,4);
    FUN_ffff00009077f38c(_DAT_ffff0000908605a0,&DAT_ffff000090801d01,param_1);
    iVar1 = FUN_ffff000090741a20(s_off_mode_charge_ffff0000907fc98e,_DAT_ffff0000908605a0);
    if (iVar1 != 0) {
      FUN_ffff00009077de54(s_set_off_mode_charge_failed_ffff0000907e0e03);
    }
    thunk_FUN_ffff0000907c2ad8(_DAT_ffff0000908605a0);
    return;
  }
  FUN_ffff00009077de54(s_off_mode_charge_malloc_failed_ffff0000907e43d1);
  return;
}


