// ffff000090737604  size=256  FUN_ffff000090737604

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090737604(undefined4 param_1,long param_2)

{
  long *plVar1;
  
  switch(param_1) {
  case 0:
    plVar1 = (long *)&DAT_ffff00009086dd30;
    break;
  case 1:
    plVar1 = (long *)&DAT_ffff00009086dd60;
    break;
  case 2:
    _DAT_ffff00009086dd90 = param_2;
    _DAT_ffff00009086dd98 = param_2 + 6;
    _DAT_ffff00009086dda0 = param_2 + 0xc;
    _DAT_ffff00009086dda8 = param_2 + 0x12;
    _DAT_ffff00009086ddb0 = param_2 + 0x18;
    _DAT_ffff00009086ddb8 = param_2 + 0x1e;
    _DAT_ffff00009086ddc0 = param_2 + 0x24;
    _DAT_ffff00009086ddc8 = param_2 + 0x2a;
    _DAT_ffff00009086ddd0 = param_2 + 0x30;
    _DAT_ffff00009086ddd8 = param_2 + 0x36;
    _DAT_ffff00009086dde0 = param_2 + 0x3c;
    _DAT_ffff00009086dde8 = param_2 + 0x42;
    _DAT_ffff00009086ddf0 = param_2 + 0x48;
    return;
  case 3:
    _DAT_ffff00009086ddf8 = param_2;
    return;
  default:
    FUN_ffff00009077de54
              (s__RTC__s__d__unknown_field_type___ffff0000907e1343,
               s_rtc_set_regmap_field_ffff0000907ef9e1,0xc1);
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


