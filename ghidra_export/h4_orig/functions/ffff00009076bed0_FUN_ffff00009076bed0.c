// ffff00009076bed0  size=196  FUN_ffff00009076bed0

void FUN_ffff00009076bed0(undefined1 param_1)

{
  uint uVar1;
  uint uVar2;
  long lVar3;
  undefined1 local_24 [4];
  
  local_24[0] = param_1;
  lVar3 = FUN_ffff000090740610(1,5);
  if (lVar3 != 0) {
    FUN_ffff00009074056c(lVar3,0x1e29,0xb,0xf,0);
    uVar1 = FUN_ffff00009074056c(lVar3,0x1e28,0,0xf,0);
    uVar2 = FUN_ffff0000907404b4(lVar3,0x1cce,local_24,1);
    FUN_ffff00009077e11c
              (s__LK_LCM__LCM__s_ret__d_en__d_sta_ffff0000907deae8,
               s_mt6373_ldo_3p0_enable_ffff0000907f36a4,uVar2 | uVar1,local_24[0]);
    return;
  }
  FUN_ffff00009077e11c
            (s__LK_LCM__LCM__ERROR_get_mt6373_d_ffff0000907d1ce8,
             s_mt6373_ldo_3p0_enable_ffff0000907f36a4);
  return;
}


