// ffff000090746c3c  size=252  FUN_ffff000090746c3c

void FUN_ffff000090746c3c(code *param_1)

{
  long lVar1;
  int iVar2;
  long lVar3;
  long lVar4;
  undefined8 uVar5;
  ulong uVar6;
  long lVar7;
  long lVar8;
  
  lVar3 = FUN_ffff00009077d24c(0x1000);
  if (lVar3 == 0) {
    FUN_ffff00009077de54(s__SRCLKEN_RC__can_not_alloc_buffe_ffff0000907e7b88);
    return;
  }
  lVar7 = 0;
  lVar4 = lVar3;
  lVar8 = 0;
  do {
    iVar2 = FUN_ffff00009077f434
                      (lVar4,0x1000 - lVar8,s_RG__lx____x_ffff0000907f4b2b,
                       *(undefined4 **)((long)&DAT_ffff000090805e08 + lVar7),
                       **(undefined4 **)((long)&DAT_ffff000090805e08 + lVar7));
    lVar1 = lVar8 + iVar2;
    if (0xfff < lVar1) {
      FUN_ffff00009077de54(s__SRCLKEN_RC__Warning__Buffer_siz_ffff0000907de360);
      lVar4 = lVar3 + lVar8;
      break;
    }
    lVar4 = lVar3 + lVar1;
    lVar7 = lVar7 + 8;
    lVar8 = lVar1;
  } while (lVar7 != 0x2d8);
  uVar5 = FUN_ffff00009077b06c(lVar3);
  uVar6 = (*param_1)(uVar5,lVar4 - lVar3);
  if ((uVar6 & 1) == 0) {
    FUN_ffff00009077de54(s__SRCLKEN_RC__AEE_EXPDB_dump_fail_ffff0000907f2e12);
  }
  thunk_FUN_ffff0000907c2ad8(lVar3);
  return;
}


