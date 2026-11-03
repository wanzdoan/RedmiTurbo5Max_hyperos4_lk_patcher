
void FUN_ffff0000b80212d4(undefined4 param_1,long param_2)

{
  long *plVar1;
  
  switch(param_1) {
  case 0:
    plVar1 = &DAT_ffff0000b811b868;
    break;
  case 1:
    plVar1 = &DAT_ffff0000b811b898;
    break;
  case 2:
    DAT_ffff0000b811b8c8 = param_2;
    DAT_ffff0000b811b8d0 = param_2 + 6;
    DAT_ffff0000b811b8d8 = param_2 + 0xc;
    DAT_ffff0000b811b8e0 = param_2 + 0x12;
    DAT_ffff0000b811b8e8 = param_2 + 0x18;
    DAT_ffff0000b811b8f0 = param_2 + 0x1e;
    DAT_ffff0000b811b8f8 = param_2 + 0x24;
    DAT_ffff0000b811b900 = param_2 + 0x2a;
    DAT_ffff0000b811b908 = param_2 + 0x30;
    DAT_ffff0000b811b910 = param_2 + 0x36;
    DAT_ffff0000b811b918 = param_2 + 0x3c;
    DAT_ffff0000b811b920 = param_2 + 0x42;
    DAT_ffff0000b811b928 = param_2 + 0x48;
    return;
  case 3:
    DAT_ffff0000b811b930 = param_2;
    return;
  default:
    FUN_ffff0000b804c33c
              (s__RTC__s__d__unknown_field_type___ffff0000b808ed30,
               s_rtc_set_regmap_field_ffff0000b80971b9,0xc1);
    return;
  }
  *plVar1 = param_2;
  plVar1[1] = param_2 + 6;
  plVar1[2] = param_2 + 0xc;
  plVar1[3] = param_2 + 0x12;
  plVar1[4] = param_2 + 0x18;
  plVar1[5] = param_2 + 0x1e;
  return;
}


