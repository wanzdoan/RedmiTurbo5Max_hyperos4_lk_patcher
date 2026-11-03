
void FUN_ffff0000be02abb8(undefined4 *param_1,undefined4 param_2)

{
  undefined8 uVar1;
  long lVar2;
  long local_30 [4];
  
  local_30[2] = 0;
  local_30[3] = 0;
  local_30[0] = 0;
  local_30[1] = 0;
  if (DAT_ffff0000be118190 == 0) {
    *param_1 = param_2;
  }
  else {
    lVar2 = (long)param_1 - DAT_ffff0000be118180;
    if (DAT_ffff0000be0d66f8 == lVar2) {
      uVar1 = 0;
    }
    else if (DAT_ffff0000be0d6700 == lVar2) {
      uVar1 = 1;
    }
    else if (DAT_ffff0000be0d6708 == lVar2) {
      uVar1 = 2;
    }
    else if (DAT_ffff0000be0d6710 == lVar2) {
      uVar1 = 3;
    }
    else if (DAT_ffff0000be0d6718 == lVar2) {
      uVar1 = 4;
    }
    else if (DAT_ffff0000be0d6720 == lVar2) {
      uVar1 = 5;
    }
    else if (DAT_ffff0000be0d6728 == lVar2) {
      uVar1 = 6;
    }
    else if (DAT_ffff0000be0d6730 == lVar2) {
      uVar1 = 7;
    }
    else if (DAT_ffff0000be0d6738 == lVar2) {
      uVar1 = 8;
    }
    else {
      if (DAT_ffff0000be0d6740 != lVar2) {
        FUN_ffff0000be05fd98
                  (s__s__writing_to_reg_0x_lx__is_not_ffff0000be0bd14d,
                   s_dbgtop_drm_update_status_ffff0000be0a6df9);
        return;
      }
      uVar1 = 9;
    }
    FUN_ffff0000be01d644(0xc2000408,0,1,uVar1,param_2,0,0,0,local_30);
    if (local_30[0] != 0) {
      FUN_ffff0000be05fd98
                (s__s__reg_update_failed__ffff0000be0b2f66,
                 s_dbgtop_drm_update_status_ffff0000be0a6df9);
    }
  }
  return;
}


