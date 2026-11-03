// ffff000090789e08  size=240  FUN_ffff000090789e08

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090789e08(long param_1,uint *param_2)

{
  int iVar1;
  undefined8 uVar2;
  long lVar3;
  ulong uVar4;
  
  FUN_ffff00009077de54(s__s_IN_ffff0000907dd338,s_mi_get_magic_ffff0000907eed74);
  if ((param_1 == 0) || (*param_2 < 8)) {
    uVar2 = 1;
  }
  else {
    iVar1 = FUN_ffff000090734fe4();
    if (iVar1 == 2) {
      lVar3 = FUN_ffff00009079685c();
      if (lVar3 == 0x400000) {
        uVar4 = 0x3fe0;
        _DAT_ffff000090887500 = 1;
      }
      else {
        if (lVar3 != 0x1000000) {
          FUN_ffff00009077de54(s_wrong_rpmb_size___0x_x_ffff0000907ea031,lVar3);
          return 7;
        }
        lVar3 = FUN_ffff000090796880(0);
        uVar4 = lVar3 + 0xffffffe000U >> 8;
      }
      uVar2 = FUN_ffff000090789ef8(uVar4,param_1,8);
      if ((int)uVar2 == 0) {
        *param_2 = 8;
        FUN_ffff00009077de54(s__s_OUT_ffff0000907fa7b2,s_mi_get_magic_ffff0000907eed74);
        uVar2 = 0;
      }
    }
    else {
      uVar2 = 5;
    }
  }
  return uVar2;
}


