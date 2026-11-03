
void FUN_ffff00009073789c(undefined4 param_1,long param_2)

{
  long *plVar1;
  
  switch(param_1) {
  case 0:
    plVar1 = &DAT_ffff00009086ddc8;
    break;
  case 1:
    plVar1 = &DAT_ffff00009086ddf8;
    break;
  case 2:
    DAT_ffff00009086de28 = param_2;
    DAT_ffff00009086de30 = param_2 + 6;
    DAT_ffff00009086de38 = param_2 + 0xc;
    DAT_ffff00009086de40 = param_2 + 0x12;
    DAT_ffff00009086de48 = param_2 + 0x18;
    DAT_ffff00009086de50 = param_2 + 0x1e;
    DAT_ffff00009086de58 = param_2 + 0x24;
    DAT_ffff00009086de60 = param_2 + 0x2a;
    DAT_ffff00009086de68 = param_2 + 0x30;
    DAT_ffff00009086de70 = param_2 + 0x36;
    DAT_ffff00009086de78 = param_2 + 0x3c;
    DAT_ffff00009086de80 = param_2 + 0x42;
    DAT_ffff00009086de88 = param_2 + 0x48;
    return;
  case 3:
    DAT_ffff00009086de90 = param_2;
    return;
  default:
    FUN_ffff00009077e11c
              (s__RTC__s__d__unknown_field_type___ffff0000907e142b,
               s_rtc_set_regmap_field_ffff0000907efb69,0xc1);
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


