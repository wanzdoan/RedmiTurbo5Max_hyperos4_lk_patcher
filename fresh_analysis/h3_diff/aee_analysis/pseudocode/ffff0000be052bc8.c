
void FUN_ffff0000be052bc8(void)

{
  undefined4 uVar1;
  long lVar2;
  
  FUN_ffff0000be05fd98(s__LK_LCM__s____ffff0000be0b6dc4,s_lcm_suspend_power_ffff0000be0a764e);
  FUN_ffff0000be065ed8(0x5a,0);
  FUN_ffff0000be065dc4(0x5a,1);
  FUN_ffff0000be065e34(0x5a,0);
  (*DAT_ffff0000be127048)(1);
  FUN_ffff0000be0541a4(0);
  (*DAT_ffff0000be127048)(1);
  lVar2 = FUN_ffff0000be0303e8(1,4);
  if (lVar2 == 0) {
    FUN_ffff0000be05fd98
              (s__LK_LCM__LCM__ERROR_get_mt6363_d_ffff0000be0b5a90,
               s_mt6363_ldo_1p2_enable_ffff0000be0a3b80);
  }
  else {
    FUN_ffff0000be030344(lVar2,0x1d07,0,1,0);
    uVar1 = FUN_ffff0000be030344(lVar2,0x149f,2,2,0);
    FUN_ffff0000be05fd98
              (s__LK_LCM__LCM__s_ret__d_en__d_ffff0000be0b03c1,
               s_mt6363_ldo_1p2_enable_ffff0000be0a3b80,uVar1,0);
  }
  (*DAT_ffff0000be127048)(1);
  FUN_ffff0000be054044(0);
  return;
}


