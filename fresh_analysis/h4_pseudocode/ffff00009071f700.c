
undefined8 FUN_ffff00009071f700(long param_1)

{
  int iVar1;
  undefined8 uVar2;
  
  if (param_1 == 0) {
    FUN_ffff00009077e11c
              (s__s__d___LK_AB___s_check_args_fai_ffff0000907f6080,
               s_check_suffix_with_slot_ffff0000907d76ec,0x61,
               s_check_suffix_with_slot_ffff0000907d76ec);
  }
  else {
    uVar2 = FUN_ffff00009077f0ac(param_1,&DAT_ffff0000907d3e3a);
    if ((int)uVar2 == 0) {
      return uVar2;
    }
    iVar1 = FUN_ffff00009077f0ac(param_1,&DAT_ffff0000907edab3);
    if (iVar1 == 0) {
      return 1;
    }
    FUN_ffff00009077e11c
              (s__s__d___LK_AB__unknown_slot_suff_ffff0000907fb457,
               s_check_suffix_with_slot_ffff0000907d76ec,0x6a);
  }
  return 0xffffffff;
}


