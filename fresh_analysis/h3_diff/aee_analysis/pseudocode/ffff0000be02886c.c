
uint FUN_ffff0000be02886c(long param_1,long param_2)

{
  int iVar1;
  long lVar2;
  uint uVar3;
  
  if ((param_1 == 0) || (param_2 == 0)) {
    FUN_ffff0000be05fd98(s_can_pass_NULL_header_0x_p__and_e_ffff0000be0be314,param_1,param_2);
    uVar3 = 0xffffffea;
  }
  else {
    lVar2 = FUN_ffff0000be05de5c(s_bootdevice_ffff0000be0a45a8);
    if (lVar2 == 0) {
      uVar3 = 0xffffffed;
    }
    else {
      FUN_ffff0000be01a730(&LAB_ffff0000be0d6628,0xffffffff);
      FUN_ffff0000be0278c8(lVar2,param_1,param_2);
      FUN_ffff0000be027c4c(lVar2,param_1,param_2);
      FUN_ffff0000be0624a4(s_bootdevice_ffff0000be0a45a8,0);
      FUN_ffff0000be01a7c8(&LAB_ffff0000be0d6628);
      iVar1 = FUN_ffff0000be0283e0();
      uVar3 = iVar1 >> 0x1f & 0xfffffffb;
      FUN_ffff0000be05def0(lVar2);
    }
  }
  return uVar3;
}


