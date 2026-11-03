
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000be051714(undefined4 *param_1)

{
  int iVar1;
  int iVar2;
  int local_34;
  
  iVar2 = _DAT_ffff00001800d088;
  if (param_1 == (undefined4 *)0x0) {
    iVar2 = 0x100006;
  }
  else {
    if (_DAT_ffff00001800d088 == 0x444d2d45) {
      FUN_ffff0000be05fd98
                (s___s___detect_dm_verity_device_co_ffff0000be0acf01,
                 s_get_dm_verity_status_ffff0000be0b9184);
      FUN_ffff0000be0502c0(0);
    }
    FUN_ffff0000be05fd98
              (s___s__d__boot_misc2___0x_x_ffff0000be0bb4bf,s_get_dm_verity_status_ffff0000be0b9184,
               0x62,_DAT_ffff00001800d088);
    local_34 = 0;
    iVar1 = FUN_ffff0000be08de1c(&local_34);
    if (iVar2 == 0x444d2d45) {
      iVar2 = FUN_ffff0000be08dea0(1);
      if (iVar2 == 0) {
        _DAT_ffff00001800d088 = 0;
        FUN_ffff0000be05fd98
                  (s___s__d__boot_misc2___0x_x_ffff0000be0bb4bf,
                   s_get_dm_verity_status_ffff0000be0b9184,0x73,0);
        *param_1 = 1;
      }
      else {
        FUN_ffff0000be05fd98
                  (s___s__set_dm_verity_state_fails_0_ffff0000be0aabc1,
                   s_get_dm_verity_status_ffff0000be0b9184,iVar2);
      }
    }
    else if (iVar1 == 0 && local_34 == 0) {
      iVar2 = 0;
      *param_1 = 0;
    }
    else {
      iVar2 = 0;
      *param_1 = 1;
    }
  }
  return iVar2;
}


