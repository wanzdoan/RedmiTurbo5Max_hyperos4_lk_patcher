// ffff000090768508  size=304  FUN_ffff000090768508

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff000090768508(undefined4 *param_1)

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
      FUN_ffff00009077e11c
                (s___s___detect_dm_verity_device_co_ffff0000907dd270,
                 s_get_dm_verity_status_ffff0000907eebcb);
      FUN_ffff000090766c44(0);
    }
    FUN_ffff00009077e11c
              (s___s__d__boot_misc2___0x_x_ffff0000907f1d3a,s_get_dm_verity_status_ffff0000907eebcb,
               0x62,_DAT_ffff00001800d088);
    local_34 = 0;
    iVar1 = FUN_ffff0000907b3d1c(&local_34);
    if (iVar2 == 0x444d2d45) {
      iVar2 = FUN_ffff0000907b3da0(1);
      if (iVar2 == 0) {
        _DAT_ffff00001800d088 = 0;
        FUN_ffff00009077e11c
                  (s___s__d__boot_misc2___0x_x_ffff0000907f1d3a,
                   s_get_dm_verity_status_ffff0000907eebcb,0x73,0);
        *param_1 = 1;
      }
      else {
        FUN_ffff00009077e11c
                  (s___s__set_dm_verity_state_fails_0_ffff0000907d9cb5,
                   s_get_dm_verity_status_ffff0000907eebcb,iVar2);
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


