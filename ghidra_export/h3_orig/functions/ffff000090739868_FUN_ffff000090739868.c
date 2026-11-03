// ffff000090739868  size=416  FUN_ffff000090739868

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090739868(undefined4 *param_1,undefined4 param_2)

{
  undefined8 uVar1;
  long lVar2;
  long local_30 [4];
  
  local_30[2] = 0;
  local_30[3] = 0;
  local_30[0] = 0;
  local_30[1] = 0;
  if (_DAT_ffff00009086de38 == 0) {
    *param_1 = param_2;
  }
  else {
    lVar2 = (long)param_1 - _DAT_ffff00009086de28;
    if (DAT_ffff0000908193a0 == lVar2) {
      uVar1 = 0;
    }
    else if (DAT_ffff0000908193a8 == lVar2) {
      uVar1 = 1;
    }
    else if (DAT_ffff0000908193b0 == lVar2) {
      uVar1 = 2;
    }
    else if (DAT_ffff0000908193b8 == lVar2) {
      uVar1 = 3;
    }
    else if (DAT_ffff0000908193c0 == lVar2) {
      uVar1 = 4;
    }
    else if (DAT_ffff0000908193c8 == lVar2) {
      uVar1 = 5;
    }
    else if (DAT_ffff0000908193d0 == lVar2) {
      uVar1 = 6;
    }
    else if (DAT_ffff0000908193d8 == lVar2) {
      uVar1 = 7;
    }
    else if (DAT_ffff0000908193e0 == lVar2) {
      uVar1 = 8;
    }
    else {
      if (DAT_ffff0000908193e8 != lVar2) {
        FUN_ffff00009077de54
                  (s__s__writing_to_reg_0x_lx__is_not_ffff0000907f4783,
                   s_dbgtop_drm_update_status_ffff0000907d461c);
        return;
      }
      uVar1 = 9;
    }
    FUN_ffff00009071ea6c(0xc2000408,0,1,uVar1,param_2,0,0,0,local_30);
    if (local_30[0] != 0) {
      FUN_ffff00009077de54
                (s__s__reg_update_failed__ffff0000907e61e1,
                 s_dbgtop_drm_update_status_ffff0000907d461c);
    }
  }
  return;
}


