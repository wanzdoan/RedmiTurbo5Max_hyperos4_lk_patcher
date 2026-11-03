// ffff00009076bcd4  size=156  FUN_ffff00009076bcd4

void FUN_ffff00009076bcd4(undefined1 param_1)

{
  undefined4 uVar1;
  long lVar2;
  undefined1 local_4 [4];
  
  local_4[0] = param_1;
  lVar2 = FUN_ffff000090740610(1,5);
  if (lVar2 != 0) {
    FUN_ffff00009074056c(lVar2,0x243,1,1,1);
    uVar1 = FUN_ffff0000907404b4(lVar2,0x1c31,local_4,1);
    FUN_ffff00009077e11c
              (s__LK_LCM__LCM__s_ret__d_en__d_sta_ffff0000907deae8,
               s_mt6373_ldo_1p8_enable_ffff0000907fa76d,uVar1,local_4[0]);
    return;
  }
  FUN_ffff00009077e11c
            (s__LK_LCM__LCM__ERROR_get_mt6373_d_ffff0000907d1ce8,
             s_mt6373_ldo_1p8_enable_ffff0000907fa76d);
  return;
}


