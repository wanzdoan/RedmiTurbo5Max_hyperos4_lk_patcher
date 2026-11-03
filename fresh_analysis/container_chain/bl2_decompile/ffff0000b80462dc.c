
void FUN_ffff0000b80462dc(undefined1 param_1)

{
  undefined4 uVar1;
  long lVar2;
  undefined1 local_4 [4];
  
  local_4[0] = param_1;
  lVar2 = FUN_ffff0000b80263f0(1,5);
  if (lVar2 != 0) {
    FUN_ffff0000b802634c(lVar2,0x243,1,1,1);
    uVar1 = FUN_ffff0000b8026304(lVar2,0x1c31,local_4,1);
    FUN_ffff0000b804c33c
              (s__LK_LCM__LCM__s_ret__d_en__d_sta_ffff0000b808d630,
               s_mt6373_ldo_1p8_enable_ffff0000b809ce49,uVar1,local_4[0]);
    return;
  }
  FUN_ffff0000b804c33c
            (s__LK_LCM__LCM__ERROR_get_mt6373_d_ffff0000b8086992,
             s_mt6373_ldo_1p8_enable_ffff0000b809ce49);
  return;
}


