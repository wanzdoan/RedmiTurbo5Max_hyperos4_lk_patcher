// ffff000090768270  size=248  FUN_ffff000090768270

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff000090768270(undefined4 *param_1)

{
  int iVar1;
  int local_34;
  
  if (param_1 == (undefined4 *)0x0) {
    iVar1 = 0x100006;
  }
  else if ((_DAT_ffff00001800d088 & 1) == 0) {
    local_34 = 0;
    iVar1 = FUN_ffff0000907b36ac(&local_34);
    if (iVar1 == 0 && local_34 == 0) {
      iVar1 = 0;
      *param_1 = 0;
    }
    else {
      iVar1 = 0;
      *param_1 = 1;
    }
  }
  else {
    FUN_ffff00009077de54
              (s___s___detect_dm_verity_device_co_ffff0000907dd183,
               s_get_dm_verity_status_ffff0000907eea43);
    FUN_ffff0000907669ac(0);
    local_34 = 0;
    FUN_ffff0000907b36ac(&local_34);
    iVar1 = FUN_ffff0000907b3730(1);
    if (iVar1 == 0) {
      _DAT_ffff00001800d088 = _DAT_ffff00001800d088 & 0xfffffffe;
      *param_1 = 1;
    }
    else {
      FUN_ffff00009077de54
                (s___s__set_dm_verity_state_fails_0_ffff0000907d9c1e,
                 s_get_dm_verity_status_ffff0000907eea43,iVar1);
    }
  }
  return iVar1;
}


