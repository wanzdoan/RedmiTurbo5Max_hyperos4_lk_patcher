// ffff00009076bc08  size=196  FUN_ffff00009076bc08

void FUN_ffff00009076bc08(undefined1 param_1)

{
  uint uVar1;
  uint uVar2;
  long lVar3;
  undefined1 local_24 [4];
  
  local_24[0] = param_1;
  lVar3 = FUN_ffff000090740378(1,5);
  if (lVar3 != 0) {
    FUN_ffff0000907402d4(lVar3,0x1e29,0xb,0xf,0);
    uVar1 = FUN_ffff0000907402d4(lVar3,0x1e28,0,0xf,0);
    uVar2 = FUN_ffff00009074021c(lVar3,0x1cce,local_24,1);
    FUN_ffff00009077de54
              (s__LK_LCM__LCM__s_ret__d_en__d_sta_ffff0000907dea2e,
               s_mt6373_ldo_3p0_enable_ffff0000907f3501,uVar2 | uVar1,local_24[0]);
    return;
  }
  FUN_ffff00009077de54
            (s__LK_LCM__LCM__ERROR_get_mt6373_d_ffff0000907d1cab,
             s_mt6373_ldo_3p0_enable_ffff0000907f3501);
  return;
}


