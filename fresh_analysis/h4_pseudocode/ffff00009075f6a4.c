
void FUN_ffff00009075f6a4(code *param_1)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  long lVar4;
  uint uVar5;
  ulong uVar6;
  undefined4 *puVar7;
  
  lVar2 = FUN_ffff00009077d514(0x1000);
  if (lVar2 != 0) {
    uVar6 = 0;
    lVar4 = 0;
    puVar7 = (undefined4 *)&DAT_ffff000000114000;
    do {
      uVar5 = (uint)uVar6;
      uVar6 = uVar6 + 1;
      iVar1 = FUN_ffff00009077f6fc
                        (lVar2 + lVar4,0x1000 - lVar4,s__d__d_ffff0000907e68f1,uVar6 & 0xffffffff,
                         *puVar7);
      lVar4 = lVar4 + iVar1;
      if ((~uVar5 & 3) == 0) {
        iVar1 = FUN_ffff00009077f6fc(lVar2 + lVar4,0x1000 - lVar4,&DAT_ffff0000907f836a);
        lVar4 = lVar4 + iVar1;
      }
      puVar7 = puVar7 + 1;
    } while (uVar6 != 0x100);
    uVar3 = FUN_ffff00009077b334(lVar2);
    uVar6 = (*param_1)(uVar3,lVar4);
    if ((uVar6 & 1) == 0) {
      FUN_ffff00009077e11c(s__Thermal_thermal_log_dump_fail_ffff0000907f8af1);
    }
    thunk_FUN_ffff0000907c314c(lVar2);
    return;
  }
  return;
}


