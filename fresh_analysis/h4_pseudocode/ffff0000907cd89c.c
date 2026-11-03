
int FUN_ffff0000907cd89c(int param_1,long param_2,uint param_3)

{
  int iVar1;
  int iVar2;
  ulong uVar3;
  long lVar4;
  
  FUN_ffff00009077e11c(s__s_IN_ffff0000907dd40e,s_read_from_rpmb_ffff0000907e241a);
  iVar1 = FUN_ffff00009073527c();
  if (iVar1 == 2) {
    uVar3 = FUN_ffff000090796b1c();
    lVar4 = FUN_ffff000090796b40(0);
    if (uVar3 == 0x400000) {
      iVar1 = 0x3fc0;
    }
    else {
      if (uVar3 != 0x1000000) {
        FUN_ffff00009077e11c(s_Error___wrong_rpmb_size___0x_x_ffff0000907df25d,uVar3 & 0xffffffff);
        iVar1 = 7;
        goto LAB_ffff0000907cd98c;
      }
      iVar1 = (int)((ulong)(lVar4 + 0xffffffc000) >> 8);
    }
    if (0xff < param_3) {
      uVar3 = 0;
      do {
        iVar2 = FUN_ffff0000907cd9cc(iVar1 + param_1 + (int)uVar3,param_2);
        if (iVar2 != 0) {
          FUN_ffff00009077e11c
                    (s_Error___rpmb_read_fail__block_in_ffff0000907ef229,uVar3 & 0xffffffff);
          return iVar2;
        }
        uVar3 = uVar3 + 1;
        param_2 = param_2 + 0x100;
      } while (param_3 >> 8 != uVar3);
    }
    FUN_ffff00009077e11c(s__s_OUT_ffff0000907fa94e,s_read_from_rpmb_ffff0000907e241a);
    iVar1 = 0;
  }
  else {
    iVar1 = 5;
LAB_ffff0000907cd98c:
    FUN_ffff00009077e11c(s_Error___rpmb_init_misecids_base__ffff0000907fc9da);
  }
  return iVar1;
}


