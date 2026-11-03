
void FUN_ffff0000be04cbfc(code *param_1)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  ulong uVar4;
  long lVar5;
  long lVar6;
  
  lVar2 = FUN_ffff0000be05f1bc(0x400);
  if (lVar2 != 0) {
    lVar5 = lVar2;
    lVar6 = 0;
    do {
      iVar1 = FUN_ffff0000be0611dc
                        (lVar5,(lVar2 + 0x400) - lVar5,s_0x_x___0x_08x_ffff0000be0aec74,
                         (int)lVar6 + 0x1046128c,*(undefined4 *)(lVar6 + -0xffffefb9ed74));
      uVar4 = lVar6 + 0x28c;
      lVar5 = lVar5 + iVar1;
      lVar6 = lVar6 + 4;
    } while (uVar4 < 0x2cd);
    lVar6 = 0;
    do {
      iVar1 = FUN_ffff0000be0611dc
                        (lVar5,(lVar2 + 0x400) - lVar5,s_0x_x___0x_08x_ffff0000be0aec74,
                         (int)lVar6 + 0x1056128c,*(undefined4 *)(lVar6 + -0xffffefa9ed74));
      uVar4 = lVar6 + 0x28c;
      lVar5 = lVar5 + iVar1;
      lVar6 = lVar6 + 4;
    } while (uVar4 < 0x2cd);
    uVar3 = FUN_ffff0000be05d228(lVar2);
    uVar4 = (*param_1)(uVar3,lVar5 - lVar2);
    if ((uVar4 & 1) == 0) {
      FUN_ffff0000be05fd98(s__WLA__AEE_EXPDB_dump_fail_ffff0000be0b4779);
    }
    thunk_FUN_ffff0000be09937c(lVar2);
    return;
  }
  FUN_ffff0000be05fd98(s__WLA__can_not_alloc_buffer_ffff0000be0bb3b1);
  return;
}


