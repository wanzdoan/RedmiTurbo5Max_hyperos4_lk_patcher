// ffff00009075e6e8  size=312  FUN_ffff00009075e6e8

void FUN_ffff00009075e6e8(code *param_1)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  ulong uVar4;
  long lVar5;
  long lVar6;
  
  lVar2 = FUN_ffff00009077d24c(0x5000);
  if (lVar2 != 0) {
    lVar5 = lVar2;
    lVar6 = 0;
    do {
      iVar1 = FUN_ffff00009077f434
                        (lVar5,(lVar2 + 0x5000) - lVar5,s_0x_x___0x_08x_ffff0000907dff6f,
                         (int)lVar6 + 0x14137200,*(undefined4 *)(lVar6 + -0xffffebec8e00));
      uVar4 = lVar6 + 0x200;
      lVar5 = lVar5 + iVar1;
      lVar6 = lVar6 + 4;
    } while (uVar4 < 0x31d);
    lVar6 = 0;
    do {
      iVar1 = FUN_ffff00009077f434
                        (lVar5,(lVar2 + 0x5000) - lVar5,s_0x_x___0x_08x_ffff0000907dff6f,
                         (int)lVar6 + 0x14416200,*(undefined4 *)(lVar6 + -0xffffebbe9e00));
      uVar4 = lVar6 + 0x200;
      lVar5 = lVar5 + iVar1;
      lVar6 = lVar6 + 4;
    } while (uVar4 < 0x31d);
    uVar3 = FUN_ffff00009077b06c(lVar2);
    uVar4 = (*param_1)(uVar3,lVar5 - lVar2);
    if ((uVar4 & 1) == 0) {
      FUN_ffff00009077de54(s__INFRA_BUS_HRE__AEE_EXPDB_dump_f_ffff0000907d68d8);
    }
    thunk_FUN_ffff0000907c2ad8(lVar2);
    return;
  }
  FUN_ffff00009077de54(s__INFRA_BUS_HRE__can_not_alloc_bu_ffff0000907eb447);
  return;
}


