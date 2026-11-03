
void FUN_ffff0000be053fa8(undefined1 param_1)

{
  undefined4 uVar1;
  long lVar2;
  undefined1 local_4 [4];
  
  local_4[0] = param_1;
  lVar2 = FUN_ffff0000be0303e8(1,5);
  if (lVar2 != 0) {
    FUN_ffff0000be030344(lVar2,0x243,1,1,1);
    uVar1 = FUN_ffff0000be0302fc(lVar2,0x1c31,local_4,1);
    FUN_ffff0000be05fd98
              (s__LK_LCM__LCM__s_ret__d_en__d_sta_ffff0000be0add88,
               s_mt6373_ldo_1p8_enable_ffff0000be0c113e,uVar1,local_4[0]);
    return;
  }
  FUN_ffff0000be05fd98
            (s__LK_LCM__LCM__ERROR_get_mt6373_d_ffff0000be0a4fdc,
             s_mt6373_ldo_1p8_enable_ffff0000be0c113e);
  return;
}


