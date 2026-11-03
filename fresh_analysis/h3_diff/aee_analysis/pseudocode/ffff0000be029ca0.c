
void FUN_ffff0000be029ca0(undefined4 param_1,long param_2)

{
  long *plVar1;
  
  switch(param_1) {
  case 0:
    plVar1 = &DAT_ffff0000be118088;
    break;
  case 1:
    plVar1 = &DAT_ffff0000be1180b8;
    break;
  case 2:
    DAT_ffff0000be1180e8 = param_2;
    DAT_ffff0000be1180f0 = param_2 + 6;
    DAT_ffff0000be1180f8 = param_2 + 0xc;
    DAT_ffff0000be118100 = param_2 + 0x12;
    DAT_ffff0000be118108 = param_2 + 0x18;
    DAT_ffff0000be118110 = param_2 + 0x1e;
    DAT_ffff0000be118118 = param_2 + 0x24;
    DAT_ffff0000be118120 = param_2 + 0x2a;
    DAT_ffff0000be118128 = param_2 + 0x30;
    DAT_ffff0000be118130 = param_2 + 0x36;
    DAT_ffff0000be118138 = param_2 + 0x3c;
    DAT_ffff0000be118140 = param_2 + 0x42;
    DAT_ffff0000be118148 = param_2 + 0x48;
    return;
  case 3:
    DAT_ffff0000be118150 = param_2;
    return;
  default:
    FUN_ffff0000be05fd98
              (s__RTC__s__d__unknown_field_type___ffff0000be0af97e,
               s_rtc_set_regmap_field_ffff0000be0b9cb3,0xc1);
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


