
void FUN_ffff0000be0495ac(code *param_1)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  ulong uVar4;
  long lVar5;
  long lVar6;
  
  lVar2 = FUN_ffff0000be05f1bc(0x5000);
  if (lVar2 != 0) {
    lVar5 = lVar2;
    lVar6 = 0;
    do {
      iVar1 = FUN_ffff0000be0611dc
                        (lVar5,(lVar2 + 0x5000) - lVar5,s_0x_x___0x_08x_ffff0000be0aec74,
                         (int)lVar6 + 0x14137200,*(undefined4 *)(lVar6 + -0xffffebec8e00));
      uVar4 = lVar6 + 0x200;
      lVar5 = lVar5 + iVar1;
      lVar6 = lVar6 + 4;
    } while (uVar4 < 0x31d);
    lVar6 = 0;
    do {
      iVar1 = FUN_ffff0000be0611dc
                        (lVar5,(lVar2 + 0x5000) - lVar5,s_0x_x___0x_08x_ffff0000be0aec74,
                         (int)lVar6 + 0x14416200,*(undefined4 *)(lVar6 + -0xffffebbe9e00));
      uVar4 = lVar6 + 0x200;
      lVar5 = lVar5 + iVar1;
      lVar6 = lVar6 + 4;
    } while (uVar4 < 0x31d);
    uVar3 = FUN_ffff0000be05d228(lVar2);
    uVar4 = (*param_1)(uVar3,lVar5 - lVar2);
    if ((uVar4 & 1) == 0) {
      FUN_ffff0000be05fd98(s__INFRA_BUS_HRE__AEE_EXPDB_dump_f_ffff0000be0a85a7);
    }
    thunk_FUN_ffff0000be09937c(lVar2);
    return;
  }
  FUN_ffff0000be05fd98(s__INFRA_BUS_HRE__can_not_alloc_bu_ffff0000be0b6c46);
  return;
}


