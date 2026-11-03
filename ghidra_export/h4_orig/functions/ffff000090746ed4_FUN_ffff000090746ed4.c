// ffff000090746ed4  size=252  FUN_ffff000090746ed4

void FUN_ffff000090746ed4(code *param_1)

{
  long lVar1;
  int iVar2;
  long lVar3;
  long lVar4;
  undefined8 uVar5;
  ulong uVar6;
  long lVar7;
  long lVar8;
  
  lVar3 = FUN_ffff00009077d514(0x1000);
  if (lVar3 == 0) {
    FUN_ffff00009077e11c(s__SRCLKEN_RC__can_not_alloc_buffe_ffff0000907e7ce3);
    return;
  }
  lVar7 = 0;
  lVar4 = lVar3;
  lVar8 = 0;
  do {
    iVar2 = FUN_ffff00009077f6fc
                      (lVar4,0x1000 - lVar8,s_RG__lx____x_ffff0000907f4caf,
                       *(undefined4 **)((long)&DAT_ffff000090806030 + lVar7),
                       **(undefined4 **)((long)&DAT_ffff000090806030 + lVar7));
    lVar1 = lVar8 + iVar2;
    if (0xfff < lVar1) {
      FUN_ffff00009077e11c(s__SRCLKEN_RC__Warning__Buffer_siz_ffff0000907de41a);
      lVar4 = lVar3 + lVar8;
      break;
    }
    lVar4 = lVar3 + lVar1;
    lVar7 = lVar7 + 8;
    lVar8 = lVar1;
  } while (lVar7 != 0x2d8);
  uVar5 = FUN_ffff00009077b334(lVar3);
  uVar6 = (*param_1)(uVar5,lVar4 - lVar3);
  if ((uVar6 & 1) == 0) {
    FUN_ffff00009077e11c(s__SRCLKEN_RC__AEE_EXPDB_dump_fail_ffff0000907f2fb5);
  }
  thunk_FUN_ffff0000907c314c(lVar3);
  return;
}


