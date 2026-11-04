
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff00009076907c(undefined4 *param_1)

{
  int iVar1;
  int local_34;

  if (param_1 == (undefined4 *)0x0) {
    iVar1 = 0x100006;
  }
  else if ((_DAT_ffff00001800d088 & 1) == 0) {
    local_34 = 0;
    iVar1 = FUN_ffff0000907b3ce0(&local_34);
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
    FUN_ffff00009077ea84
              (s___s___detect_dm_verity_device_co_ffff0000907d5672,
               s_get_dm_verity_status_ffff0000907e778d);
    FUN_ffff000090767ce0(0);
    local_34 = 0;
    FUN_ffff0000907b3ce0(&local_34);
    iVar1 = FUN_ffff0000907b3d64(1);
    if (iVar1 == 0) {
      _DAT_ffff00001800d088 = _DAT_ffff00001800d088 & 0xfffffffe;
      *param_1 = 1;
    }
    else {
      FUN_ffff00009077ea84
                (s___s__set_dm_verity_state_fails_0_ffff0000907d2105,
                 s_get_dm_verity_status_ffff0000907e778d,iVar1);
    }
  }
  return iVar1;
}
