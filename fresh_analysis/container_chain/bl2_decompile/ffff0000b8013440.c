
bool FUN_ffff0000b8013440(void)

{
  int iVar1;
  undefined8 local_30;
  undefined8 uStack_28;
  undefined8 local_20;
  undefined8 uStack_18;
  
  local_20 = 0;
  uStack_18 = 0;
  local_30 = 0;
  uStack_28 = 0;
  iVar1 = FUN_ffff0000b80134bc(0x4c,&local_30,0x20);
  if (iVar1 == 0) {
    FUN_ffff0000b804c33c
              (s__XMCHG_LK_BQ28Z610___s__FG_MAC_C_ffff0000b808700b,
               s_bq28z610_is_gl_battery_ffff0000b8095297,&local_30);
  }
  else {
    FUN_ffff0000b804c33c
              (s__XMCHG_LK_BQ28Z610___s__failed_t_ffff0000b8098ab3,
               s_bq28z610_is_gl_battery_ffff0000b8095297,iVar1);
  }
  return uStack_28._6_1_ == 'A';
}


