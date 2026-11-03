// ffff00009076ba0c  size=156  FUN_ffff00009076ba0c

void FUN_ffff00009076ba0c(undefined1 param_1)

{
  undefined4 uVar1;
  long lVar2;
  undefined1 local_4 [4];
  
  local_4[0] = param_1;
  lVar2 = FUN_ffff000090740378(1,5);
  if (lVar2 != 0) {
    FUN_ffff0000907402d4(lVar2,0x243,1,1,1);
    uVar1 = FUN_ffff00009074021c(lVar2,0x1c31,local_4,1);
    FUN_ffff00009077de54
              (s__LK_LCM__LCM__s_ret__d_en__d_sta_ffff0000907dea2e,
               s_mt6373_ldo_1p8_enable_ffff0000907fa5d1,uVar1,local_4[0]);
    return;
  }
  FUN_ffff00009077de54
            (s__LK_LCM__LCM__ERROR_get_mt6373_d_ffff0000907d1cab,
             s_mt6373_ldo_1p8_enable_ffff0000907fa5d1);
  return;
}


