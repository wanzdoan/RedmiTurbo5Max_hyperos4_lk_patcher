
undefined8 FUN_ffff000090789cc0(long param_1,uint *param_2)

{
  int iVar1;
  undefined8 uVar2;
  long lVar3;
  ulong uVar4;

  FUN_ffff00009077ea84(s__s_IN_ffff0000907d585f,s_mi_get_magic_ffff0000907e7a16);
  if ((param_1 == 0) || (*param_2 < 8)) {
    uVar2 = 1;
  }
  else {
    iVar1 = FUN_ffff00009073585c();
    if (iVar1 == 2) {
      lVar3 = FUN_ffff00009079679c();
      if (lVar3 == 0x400000) {
        uVar4 = 0x3fe0;
        DAT_ffff0000908874f8 = 1;
      }
      else {
        if (lVar3 != 0x1000000) {
          FUN_ffff00009077ea84(s_wrong_rpmb_size___0x_x_ffff0000907e2aa3,lVar3);
          return 7;
        }
        lVar3 = FUN_ffff0000907967c0(0);
        uVar4 = lVar3 + 0xffffffe000U >> 8;
      }
      uVar2 = FUN_ffff000090789db0(uVar4,param_1,8);
      if ((int)uVar2 == 0) {
        *param_2 = 8;
        FUN_ffff00009077ea84(s__s_OUT_ffff0000907f3795,s_mi_get_magic_ffff0000907e7a16);
        uVar2 = 0;
      }
    }
    else {
      uVar2 = 5;
    }
  }
  return uVar2;
}
