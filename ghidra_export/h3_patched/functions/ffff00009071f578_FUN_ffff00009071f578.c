// ffff00009071f578  size=144  FUN_ffff00009071f578

undefined8 FUN_ffff00009071f578(long param_1)

{
  int iVar1;
  undefined8 uVar2;
  
  if (param_1 == 0) {
    FUN_ffff00009077de54
              (s__s__d___LK_AB___s_check_args_fai_ffff0000907f5edf,
               s_check_suffix_with_slot_ffff0000907d7656,0x61,
               s_check_suffix_with_slot_ffff0000907d7656);
  }
  else {
    uVar2 = FUN_ffff00009077ede4(param_1,&DAT_ffff0000907d3dfd);
    if ((int)uVar2 == 0) {
      return uVar2;
    }
    iVar1 = FUN_ffff00009077ede4(param_1,&DAT_ffff0000907ed92b);
    if (iVar1 == 0) {
      return 1;
    }
    FUN_ffff00009077de54
              (s__s__d___LK_AB__unknown_slot_suff_ffff0000907fb2bb,
               s_check_suffix_with_slot_ffff0000907d7656,0x6a);
  }
  return 0xffffffff;
}


