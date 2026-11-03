// ffff0000907470a0  size=120  FUN_ffff0000907470a0

int FUN_ffff0000907470a0
              (undefined8 param_1,undefined8 param_2,undefined8 param_3,undefined8 param_4,
              ulong *param_5,ulong *param_6)

{
  ulong uVar1;
  int iVar2;
  
  uVar1 = (*param_5 & 0xff00ff00ff00ff00) >> 8 | (*param_5 & 0xff00ff00ff00ff) << 8;
  uVar1 = (uVar1 & 0xffff0000ffff0000) >> 0x10 | (uVar1 & 0xffff0000ffff) << 0x10;
  *param_6 = uVar1 >> 0x20 | uVar1 << 0x20;
  iVar2 = FUN_ffff00009076e6d4(param_1,param_2,param_4,param_6,8);
  if (iVar2 < 0) {
    FUN_ffff00009077e11c
              (s___s__setprop__s__s_fail__ret__d_ffff0000907eb0d5,
               s_connsys_set_fdt_addr_prop_ffff0000907ec904,param_3,param_4,iVar2);
  }
  return iVar2;
}


