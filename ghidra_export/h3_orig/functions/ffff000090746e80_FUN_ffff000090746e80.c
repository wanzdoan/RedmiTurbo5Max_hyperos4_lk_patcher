// ffff000090746e80  size=116  FUN_ffff000090746e80

int FUN_ffff000090746e80
              (undefined8 param_1,undefined8 param_2,undefined8 param_3,undefined8 param_4,
              uint param_5)

{
  uint uVar1;
  int iVar2;
  uint local_14;
  
  uVar1 = (param_5 & 0xff00ff00) >> 8 | (param_5 & 0xff00ff) << 8;
  local_14 = uVar1 >> 0x10 | uVar1 << 0x10;
  iVar2 = FUN_ffff00009076e40c(param_1,param_2,param_4,&local_14,4);
  if (iVar2 < 0) {
    FUN_ffff00009077de54
              (s___s__setprop__s__s_fail__ret__d_ffff0000907eaf52,
               s_connsys_set_fdt_prop_u32_ffff0000907dfc19,param_3,param_4,iVar2);
  }
  return iVar2;
}


