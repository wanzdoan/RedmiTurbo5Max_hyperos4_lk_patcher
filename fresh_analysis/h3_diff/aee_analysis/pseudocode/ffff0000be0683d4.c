
undefined8 FUN_ffff0000be0683d4(long param_1,uint *param_2)

{
  int iVar1;
  undefined8 uVar2;
  long lVar3;
  ulong uVar4;
  
  FUN_ffff0000be05fd98(s__s_IN_ffff0000be0acfc9,s_mi_get_magic_ffff0000be0b92a7);
  if ((param_1 == 0) || (*param_2 < 8)) {
    uVar2 = 1;
  }
  else {
    iVar1 = FUN_ffff0000be028018();
    if (iVar1 == 2) {
      lVar3 = FUN_ffff0000be074290();
      if (lVar3 == 0x400000) {
        uVar4 = 0x3fe0;
        DAT_ffff0000be132400 = 1;
      }
      else {
        if (lVar3 != 0x1000000) {
          FUN_ffff0000be05fd98(s_wrong_rpmb_size___0x_x_ffff0000be0b5c1a,lVar3);
          return 7;
        }
        lVar3 = FUN_ffff0000be0742b4(0);
        uVar4 = lVar3 + 0xffffffe000U >> 8;
      }
      uVar2 = FUN_ffff0000be0684c4(uVar4,param_1,8);
      if ((int)uVar2 == 0) {
        *param_2 = 8;
        FUN_ffff0000be05fd98(s__s_OUT_ffff0000be0c1274,s_mi_get_magic_ffff0000be0b92a7);
        uVar2 = 0;
      }
    }
    else {
      uVar2 = 5;
    }
  }
  return uVar2;
}


